package com.jworks.englishlens.ui.capture

import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.Region
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.jworks.englishlens.domain.models.OCRResult

/**
 * Coordinate transform chain:
 * 1. OCR bounding box: coordinates in original bitmap (imageWidth x imageHeight)
 * 2. Image is centered in container, then scaled and offset by user zoom/pan
 * 3. Lasso path: screen-space coordinates
 * 4. Hit-test: check if each word's bounding box center is inside the lasso path
 */

fun findWordsInLasso(
    lassoPoints: List<Offset>,
    ocrResult: OCRResult,
    bitmap: Bitmap,
    scale: Float,
    offset: Offset,
    containerSize: Size
): List<String> {
    if (lassoPoints.size < 3) return emptyList()

    val path = Path().apply {
        moveTo(lassoPoints.first().x, lassoPoints.first().y)
        for (i in 1 until lassoPoints.size) {
            lineTo(lassoPoints[i].x, lassoPoints[i].y)
        }
        close()
    }

    val clipRegion = Region(
        0, 0, containerSize.width.toInt(), containerSize.height.toInt()
    )
    val lassoRegion = Region()
    lassoRegion.setPath(path, clipRegion)

    data class WordHit(val text: String, val top: Int, val left: Int)
    val hits = mutableListOf<WordHit>()

    for (detected in ocrResult.texts) {
        for (element in detected.elements) {
            val bounds = element.bounds ?: continue
            if (!element.isWord) continue

            val center = transformPointToScreen(
                x = bounds.exactCenterX(),
                y = bounds.exactCenterY(),
                bitmap = bitmap,
                scale = scale,
                offset = offset,
                containerSize = containerSize
            )

            if (lassoRegion.contains(center.x.toInt(), center.y.toInt())) {
                hits.add(WordHit(element.text, bounds.top, bounds.left))
            }
        }
    }

    // Sort in reading order: top-to-bottom, left-to-right
    return hits
        .sortedWith(compareBy({ it.top }, { it.left }))
        .map { it.text }
}

fun transformPointToScreen(
    x: Float,
    y: Float,
    bitmap: Bitmap,
    scale: Float,
    offset: Offset,
    containerSize: Size
): Offset {
    val imageLeft = (containerSize.width - bitmap.width * scale) / 2f + offset.x
    val imageTop = (containerSize.height - bitmap.height * scale) / 2f + offset.y

    return Offset(
        x = imageLeft + x * scale,
        y = imageTop + y * scale
    )
}

fun transformBoundsToScreen(
    bounds: android.graphics.Rect,
    bitmap: Bitmap,
    scale: Float,
    offset: Offset,
    containerSize: Size
): Rect {
    val topLeft = transformPointToScreen(
        bounds.left.toFloat(), bounds.top.toFloat(),
        bitmap, scale, offset, containerSize
    )
    val bottomRight = transformPointToScreen(
        bounds.right.toFloat(), bounds.bottom.toFloat(),
        bitmap, scale, offset, containerSize
    )
    return Rect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y)
}
