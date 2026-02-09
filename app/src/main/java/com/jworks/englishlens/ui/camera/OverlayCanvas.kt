package com.jworks.englishlens.ui.camera

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.jworks.englishlens.domain.models.AppSettings
import com.jworks.englishlens.domain.models.DetectedText

@Composable
fun TextOverlay(
    detectedTexts: List<DetectedText>,
    imageWidth: Int,
    imageHeight: Int,
    rotationDegrees: Int,
    settings: AppSettings,
    selectedWord: String? = null,
    modifier: Modifier = Modifier
) {
    val highlightColor = remember(settings.highlightColor) { Color(settings.highlightColor) }
    val selectedColor = remember { Color(0xFFFF9800) } // Orange for selected word

    Canvas(modifier = modifier.fillMaxSize()) {
        if (imageWidth <= 0 || imageHeight <= 0) return@Canvas

        val isRotated = rotationDegrees == 90 || rotationDegrees == 270
        val effectiveWidth = (if (isRotated) imageHeight else imageWidth).toFloat()
        val effectiveHeight = (if (isRotated) imageWidth else imageHeight).toFloat()

        // Match PreviewView.ScaleType.FILL_CENTER
        val scale = maxOf(size.width / effectiveWidth, size.height / effectiveHeight)
        val cropOffsetX = (effectiveWidth * scale - size.width) / 2f
        val cropOffsetY = (effectiveHeight * scale - size.height) / 2f

        // Clip bottom edge in partial mode
        val isPartial = settings.partialModeBoundaryRatio < 0.99f
        val clipBottomEdge = if (isPartial) {
            size.height * PartialModeConstants.CAMERA_HEIGHT_RATIO
        } else 0f

        for (detected in detectedTexts) {
            for (element in detected.elements) {
                if (!element.isWord) continue
                val bounds = element.bounds ?: continue
                if (bounds.isEmpty) continue

                val elemLeft = bounds.left * scale - cropOffsetX
                val elemTop = bounds.top * scale - cropOffsetY
                val elemWidth = bounds.width() * scale
                val elemHeight = bounds.height() * scale

                if (elemWidth <= 0 || elemHeight <= 0) continue
                if (elemLeft + elemWidth < -50 || elemLeft > size.width + 50) continue
                if (elemTop + elemHeight < -50 || elemTop > size.height + 50) continue
                if (clipBottomEdge > 0f && elemTop > clipBottomEdge) continue

                val isSelected = selectedWord != null &&
                        element.text.equals(selectedWord, ignoreCase = true)
                val color = if (isSelected) selectedColor else highlightColor

                if (settings.showBoxes || isSelected) {
                    drawWordBox(
                        left = elemLeft,
                        top = elemTop,
                        width = elemWidth,
                        height = elemHeight,
                        color = color,
                        strokeWidth = if (isSelected) settings.strokeWidth * 2 else settings.strokeWidth,
                        filled = isSelected
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawWordBox(
    left: Float,
    top: Float,
    width: Float,
    height: Float,
    color: Color,
    strokeWidth: Float,
    filled: Boolean = false
) {
    val safeWidth = width.coerceAtLeast(0.1f)
    val safeHeight = height.coerceAtLeast(0.1f)

    if (filled) {
        // Semi-transparent fill for selected word
        drawRoundRect(
            color = color.copy(alpha = 0.3f),
            topLeft = Offset(left, top),
            size = Size(safeWidth, safeHeight),
            cornerRadius = CornerRadius(4f, 4f)
        )
    }

    drawRoundRect(
        color = color,
        topLeft = Offset(left, top),
        size = Size(safeWidth, safeHeight),
        cornerRadius = CornerRadius(4f, 4f),
        style = Stroke(width = strokeWidth)
    )
}
