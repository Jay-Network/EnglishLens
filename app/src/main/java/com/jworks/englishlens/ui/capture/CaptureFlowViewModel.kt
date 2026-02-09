package com.jworks.englishlens.ui.capture

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jworks.englishlens.data.repository.DefinitionRepository
import com.jworks.englishlens.domain.analysis.ReadabilityCalculator
import com.jworks.englishlens.domain.models.Definition
import com.jworks.englishlens.domain.models.DetectedText
import com.jworks.englishlens.domain.models.OCRResult
import com.jworks.englishlens.domain.models.ReadabilityMetrics
import com.jworks.englishlens.domain.usecases.ProcessCameraFrameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CaptureState {
    data object Camera : CaptureState()
    data class Annotate(val capturedImage: CapturedImage) : CaptureState()
}

data class CapturedImage(
    val bitmap: Bitmap,
    val ocrResult: OCRResult,
    val timestamp: Long
)

sealed class LookupState {
    data object Idle : LookupState()
    data object Loading : LookupState()
    data class Success(val definition: Definition) : LookupState()
    data class NotFound(val word: String) : LookupState()
    data class Error(val message: String?) : LookupState()
}

sealed class AnalysisMode {
    data object WordLookup : AnalysisMode()
    data object Readability : AnalysisMode()
}

@HiltViewModel
class CaptureFlowViewModel @Inject constructor(
    private val processCameraFrame: ProcessCameraFrameUseCase,
    private val definitionRepository: DefinitionRepository,
    private val readabilityCalculator: ReadabilityCalculator
) : ViewModel() {

    companion object {
        private const val TAG = "CaptureFlowVM"
        private const val MAX_BITMAP_DIMENSION = 2048
    }

    private val _captureState = MutableStateFlow<CaptureState>(CaptureState.Camera)
    val captureState: StateFlow<CaptureState> = _captureState.asStateFlow()

    private val _lookupState = MutableStateFlow<LookupState>(LookupState.Idle)
    val lookupState: StateFlow<LookupState> = _lookupState.asStateFlow()

    private val _analysisMode = MutableStateFlow<AnalysisMode>(AnalysisMode.WordLookup)
    val analysisMode: StateFlow<AnalysisMode> = _analysisMode.asStateFlow()

    private val _readabilityMetrics = MutableStateFlow<ReadabilityMetrics?>(null)
    val readabilityMetrics: StateFlow<ReadabilityMetrics?> = _readabilityMetrics.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _isFlashOn = MutableStateFlow(false)
    val isFlashOn: StateFlow<Boolean> = _isFlashOn.asStateFlow()

    init {
        viewModelScope.launch {
            definitionRepository.preloadCommonWords(100)
        }
    }

    fun toggleFlash() {
        _isFlashOn.value = !_isFlashOn.value
    }

    fun onPhotoCapture(bitmap: Bitmap) {
        _isProcessing.value = true
        viewModelScope.launch {
            try {
                val scaled = downscaleBitmap(bitmap)
                val ocrResult = processCameraFrame.processStaticImage(scaled)
                Log.d(TAG, "Captured: ${ocrResult.texts.size} lines, " +
                        "${ocrResult.texts.sumOf { it.elements.size }} words, " +
                        "${ocrResult.processingTimeMs}ms")
                _captureState.value = CaptureState.Annotate(
                    CapturedImage(scaled, ocrResult, System.currentTimeMillis())
                )
            } catch (e: Exception) {
                Log.e(TAG, "Capture failed", e)
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun onGalleryImport(bitmap: Bitmap) {
        onPhotoCapture(bitmap)
    }

    fun selectWords(words: List<String>) {
        if (words.isEmpty()) return
        _analysisMode.value = AnalysisMode.WordLookup

        val query = if (words.size == 1) words.first() else words.joinToString(" ")
        viewModelScope.launch {
            _lookupState.value = LookupState.Loading

            // Try phrase first, then individual words
            definitionRepository.getDefinition(query)
                .onSuccess { _lookupState.value = LookupState.Success(it) }
                .onFailure {
                    if (words.size > 1) {
                        // Fall back to first word
                        definitionRepository.getDefinition(words.first())
                            .onSuccess { _lookupState.value = LookupState.Success(it) }
                            .onFailure { err -> setFailureState(words.first(), err) }
                    } else {
                        setFailureState(query, it)
                    }
                }
        }
    }

    fun analyzeReadability() {
        val state = _captureState.value
        if (state !is CaptureState.Annotate) return
        val text = state.capturedImage.ocrResult.texts.joinToString(" ") { it.text }
        if (text.isBlank()) return

        _analysisMode.value = AnalysisMode.Readability
        _readabilityMetrics.value = readabilityCalculator.calculate(text)
    }

    fun switchToWordLookup() {
        _analysisMode.value = AnalysisMode.WordLookup
    }

    fun resetToCamera() {
        _captureState.value = CaptureState.Camera
        _lookupState.value = LookupState.Idle
        _analysisMode.value = AnalysisMode.WordLookup
        _readabilityMetrics.value = null
    }

    private fun setFailureState(word: String, error: Throwable) {
        _lookupState.value = if (error is NoSuchElementException) {
            LookupState.NotFound(word)
        } else {
            LookupState.Error(error.message)
        }
    }

    private fun downscaleBitmap(bitmap: Bitmap): Bitmap {
        val maxDim = maxOf(bitmap.width, bitmap.height)
        if (maxDim <= MAX_BITMAP_DIMENSION) return bitmap
        val scale = MAX_BITMAP_DIMENSION.toFloat() / maxDim
        return Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * scale).toInt(),
            (bitmap.height * scale).toInt(),
            true
        )
    }
}
