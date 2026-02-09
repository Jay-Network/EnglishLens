package com.jworks.englishlens.domain.models

data class AppSettings(
    val highlightColor: Long = 0xFF2196F3,      // Blue for word highlights
    val strokeWidth: Float = 2f,
    val labelFontSize: Float = 14f,
    val labelBackgroundAlpha: Float = 0.7f,
    val frameSkip: Int = 1,
    val showDebugHud: Boolean = true,
    val showBoxes: Boolean = true,
    val partialModeBoundaryRatio: Float = 0.25f  // 1.0 = full screen, 0.25 = partial
)
