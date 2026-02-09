package com.jworks.englishlens.ui.camera

import android.graphics.Rect
import android.util.Log
import android.util.Size
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.jworks.englishlens.data.repository.DefinitionRepository
import com.jworks.englishlens.domain.analysis.ReadabilityCalculator
import com.jworks.englishlens.domain.models.AppSettings
import com.jworks.englishlens.domain.models.Definition
import com.jworks.englishlens.domain.models.DetectedText
import com.jworks.englishlens.domain.models.ReadabilityMetrics
import com.jworks.englishlens.domain.repository.SettingsRepository
import com.jworks.englishlens.domain.usecases.ProcessCameraFrameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

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
class CameraViewModel @Inject constructor(
    private val processCameraFrame: ProcessCameraFrameUseCase,
    private val settingsRepository: SettingsRepository,
    private val definitionRepository: DefinitionRepository,
    private val readabilityCalculator: ReadabilityCalculator
) : ViewModel() {

    companion object {
        private const val TAG = "CameraVM"
        private const val STATS_WINDOW = 30
        private const val PERSIST_FRAMES = 3
    }

    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    private val _detectedTexts = MutableStateFlow<List<DetectedText>>(emptyList())
    val detectedTexts: StateFlow<List<DetectedText>> = _detectedTexts.asStateFlow()

    private val _selectedWord = MutableStateFlow<String?>(null)
    val selectedWord: StateFlow<String?> = _selectedWord.asStateFlow()

    private val _lookupState = MutableStateFlow<LookupState>(LookupState.Idle)
    val lookupState: StateFlow<LookupState> = _lookupState.asStateFlow()

    private val _analysisMode = MutableStateFlow<AnalysisMode>(AnalysisMode.WordLookup)
    val analysisMode: StateFlow<AnalysisMode> = _analysisMode.asStateFlow()

    private val _readabilityMetrics = MutableStateFlow<ReadabilityMetrics?>(null)
    val readabilityMetrics: StateFlow<ReadabilityMetrics?> = _readabilityMetrics.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _sourceImageSize = MutableStateFlow(Size(480, 640))
    val sourceImageSize: StateFlow<Size> = _sourceImageSize.asStateFlow()

    private val _rotationDegrees = MutableStateFlow(0)
    val rotationDegrees: StateFlow<Int> = _rotationDegrees.asStateFlow()

    private val _canvasSize = MutableStateFlow(Size(1080, 2400))
    val canvasSize: StateFlow<Size> = _canvasSize.asStateFlow()

    private val _isFlashOn = MutableStateFlow(false)
    val isFlashOn: StateFlow<Boolean> = _isFlashOn.asStateFlow()

    private val _visibleRegion = MutableStateFlow<Rect?>(null)
    val visibleRegion: StateFlow<Rect?> = _visibleRegion.asStateFlow()

    private val _ocrStats = MutableStateFlow(OCRStats())
    val ocrStats: StateFlow<OCRStats> = _ocrStats.asStateFlow()

    private var frameCount = 0
    private val recentTimings = ArrayDeque<Long>(STATS_WINDOW)
    private val modeSwitchPauseFrames = java.util.concurrent.atomic.AtomicInteger(0)
    private var emptyFrameCount = 0
    @Volatile private var cachedFrameSkip = 3

    init {
        viewModelScope.launch {
            settings.collect { cachedFrameSkip = it.frameSkip }
        }
        // Preload common words into memory cache
        viewModelScope.launch {
            definitionRepository.preloadCommonWords(100)
        }
    }

    fun toggleFlash() {
        _isFlashOn.value = !_isFlashOn.value
    }

    fun updateCanvasSize(size: Size) {
        _canvasSize.value = size
    }

    fun selectWord(word: String?) {
        _selectedWord.value = word
        if (word != null) {
            lookupWord(word)
        } else {
            _lookupState.value = LookupState.Idle
        }
    }

    fun dismissDefinition() {
        _selectedWord.value = null
        _lookupState.value = LookupState.Idle
    }

    fun analyzeReadability() {
        val allText = _detectedTexts.value.joinToString(" ") { it.text }
        if (allText.isBlank()) return

        _analysisMode.value = AnalysisMode.Readability
        _readabilityMetrics.value = readabilityCalculator.calculate(allText)
    }

    fun switchToWordLookup() {
        _analysisMode.value = AnalysisMode.WordLookup
        _readabilityMetrics.value = null
    }

    private fun lookupWord(word: String) {
        viewModelScope.launch {
            _lookupState.value = LookupState.Loading
            definitionRepository.getDefinition(word)
                .onSuccess { definition ->
                    _lookupState.value = LookupState.Success(definition)
                }
                .onFailure { error ->
                    if (error is NoSuchElementException) {
                        _lookupState.value = LookupState.NotFound(word)
                    } else {
                        _lookupState.value = LookupState.Error(error.message)
                    }
                }
        }
    }

    fun updatePartialModeBoundaryRatio(ratio: Float) {
        viewModelScope.launch {
            _detectedTexts.value = emptyList()
            modeSwitchPauseFrames.set(15)
            val updated = settings.value.copy(partialModeBoundaryRatio = ratio)
            settingsRepository.updateSettings(updated)
        }
    }

    fun processFrame(imageProxy: ImageProxy) {
        frameCount++

        if (modeSwitchPauseFrames.get() > 0) {
            modeSwitchPauseFrames.decrementAndGet()
            imageProxy.close()
            return
        }

        val frameSkip = cachedFrameSkip
        if (frameCount % frameSkip != 0 || _isProcessing.value) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        _isProcessing.value = true
        val rotation = imageProxy.imageInfo.rotationDegrees
        _rotationDegrees.value = rotation

        val inputImage = InputImage.fromMediaImage(mediaImage, rotation)
        val cropRect = mediaImage.cropRect
        val imageSize = Size(cropRect.width(), cropRect.height())

        if (frameCount % (settings.value.frameSkip * 5) == 0) {
            Log.d(TAG, "Processing: ${imageSize.width}x${imageSize.height}, rotation=$rotation°")
        }

        viewModelScope.launch {
            try {
                val result = processCameraFrame.execute(inputImage, imageSize)

                // Filter to visible region in partial mode
                val filtered = if (settings.value.partialModeBoundaryRatio < 0.99f) {
                    val canvasSize = _canvasSize.value
                    val visibleRegion = calculateVisibleRegion(
                        result.imageSize, canvasSize,
                        settings.value.partialModeBoundaryRatio, rotation
                    )
                    _visibleRegion.value = visibleRegion

                    result.texts.mapNotNull { detected ->
                        val visibleElements = detected.elements.filter { element ->
                            val bounds = element.bounds ?: return@filter false
                            Rect.intersects(bounds, visibleRegion)
                        }
                        if (visibleElements.isEmpty()) null
                        else detected.copy(elements = visibleElements)
                    }
                } else {
                    result.texts.also { _visibleRegion.value = null }
                }

                // Sort by position (top-to-bottom, left-to-right)
                val sorted = filtered.sortedWith(compareBy(
                    { it.bounds?.top ?: Int.MAX_VALUE },
                    { it.bounds?.left ?: Int.MAX_VALUE }
                ))

                // Persist previous results to reduce flicker
                val prevCount = _detectedTexts.value.sumOf { it.elements.size }
                val newCount = sorted.sumOf { it.elements.size }
                if (newCount >= prevCount || emptyFrameCount >= PERSIST_FRAMES) {
                    _detectedTexts.value = sorted
                    emptyFrameCount = if (newCount < prevCount) 1 else 0
                } else {
                    emptyFrameCount++
                }
                _sourceImageSize.value = result.imageSize
                updateStats(result.processingTimeMs, sorted.size)
            } catch (_: Exception) {
                // OCR failed for this frame, keep previous results
            } finally {
                _isProcessing.value = false
                imageProxy.close()
            }
        }
    }

    private fun calculateVisibleRegion(
        imageSize: Size,
        canvasSize: Size,
        displayBoundary: Float,
        rotationDegrees: Int
    ): Rect {
        if (displayBoundary >= 0.99f) {
            return Rect(0, 0, imageSize.width, imageSize.height)
        }

        val isRotated = rotationDegrees == 90 || rotationDegrees == 270
        val effectiveWidth = (if (isRotated) imageSize.height else imageSize.width).toFloat()
        val effectiveHeight = (if (isRotated) imageSize.width else imageSize.height).toFloat()

        if (effectiveWidth <= 0f || effectiveHeight <= 0f) {
            return Rect(0, 0, imageSize.width, imageSize.height)
        }

        val scale = maxOf(
            canvasSize.width / effectiveWidth,
            canvasSize.height / effectiveHeight
        )

        val cropOffsetX = (effectiveWidth * scale - canvasSize.width) / 2f
        val cropOffsetY = (effectiveHeight * scale - canvasSize.height) / 2f

        // Horizontal partial: full width, top portion of height
        val screenRight = canvasSize.width.toFloat()
        val screenBottom = canvasSize.height * PartialModeConstants.CAMERA_HEIGHT_RATIO

        val imageLeft = (cropOffsetX / scale).toInt().coerceAtLeast(0)
        val imageTop = (cropOffsetY / scale).toInt().coerceAtLeast(0)
        val imageRight = ((screenRight + cropOffsetX) / scale).toInt().coerceAtMost(imageSize.width)
        val imageBottom = ((screenBottom + cropOffsetY) / scale).toInt().coerceAtMost(imageSize.height)

        return Rect(imageLeft, imageTop, imageRight, imageBottom)
    }

    private fun updateStats(processingTimeMs: Long, lineCount: Int) {
        if (recentTimings.size >= STATS_WINDOW) recentTimings.removeFirst()
        recentTimings.addLast(processingTimeMs)

        val avgMs = recentTimings.average().toLong()
        val frameSkip = settings.value.frameSkip
        _ocrStats.value = OCRStats(
            lastFrameMs = processingTimeMs,
            avgFrameMs = avgMs,
            framesProcessed = frameCount / frameSkip,
            linesDetected = lineCount
        )

        if (frameCount % (frameSkip * 10) == 0) {
            Log.d(TAG, "OCR stats: avg=${avgMs}ms, last=${processingTimeMs}ms, lines=$lineCount")
        }
    }
}

data class OCRStats(
    val lastFrameMs: Long = 0,
    val avgFrameMs: Long = 0,
    val framesProcessed: Int = 0,
    val linesDetected: Int = 0
)
