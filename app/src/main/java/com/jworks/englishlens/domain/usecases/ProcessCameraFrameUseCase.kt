package com.jworks.englishlens.domain.usecases

import android.util.Log
import android.util.Size
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.jworks.englishlens.domain.models.DetectedText
import com.jworks.englishlens.domain.models.OCRResult
import com.jworks.englishlens.domain.models.TextElement
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class ProcessCameraFrameUseCase @Inject constructor(
    private val textRecognizer: TextRecognizer
) {
    companion object {
        private const val TAG = "OCR"
        private const val MIN_WORD_LENGTH = 1
    }

    suspend fun execute(inputImage: InputImage, imageSize: Size): OCRResult {
        val startTime = System.currentTimeMillis()

        val visionText = withTimeoutOrNull(500L) {
            textRecognizer.process(inputImage).await()
        } ?: return OCRResult(emptyList(), System.currentTimeMillis(), imageSize, 500L)

        val detectedTexts = visionText.textBlocks.flatMap { block ->
            block.lines.mapNotNull { line ->
                val lineText = line.text.trim()
                if (lineText.isEmpty()) return@mapNotNull null

                // Extract word-level elements from the line
                val elements = line.elements.mapNotNull { element ->
                    val word = element.text.trim()
                    // Skip very short non-word tokens (single punctuation, etc.)
                    if (word.length < MIN_WORD_LENGTH) return@mapNotNull null
                    // Keep words that contain at least one letter
                    if (!word.any { it.isLetter() }) return@mapNotNull null

                    TextElement(
                        text = word,
                        bounds = element.boundingBox,
                        isWord = word.all { it.isLetter() || it == '\'' || it == '-' }
                    )
                }

                if (elements.isEmpty()) return@mapNotNull null

                DetectedText(
                    text = lineText,
                    bounds = line.boundingBox,
                    confidence = line.confidence,
                    language = line.recognizedLanguage ?: "en",
                    elements = elements
                )
            }
        }

        val processingTime = System.currentTimeMillis() - startTime
        Log.d(TAG, "OCR: ${detectedTexts.size} lines, ${processingTime}ms, " +
                "words: ${detectedTexts.sumOf { it.elements.size }}")

        return OCRResult(
            texts = detectedTexts,
            timestamp = System.currentTimeMillis(),
            imageSize = imageSize,
            processingTimeMs = processingTime
        )
    }
}
