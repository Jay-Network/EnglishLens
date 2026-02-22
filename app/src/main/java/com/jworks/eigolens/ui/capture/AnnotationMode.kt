package com.jworks.eigolens.ui.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jworks.eigolens.R
import com.jworks.eigolens.ui.camera.DefinitionPanel
import com.jworks.eigolens.ui.camera.DefinitionSkeleton
import com.jworks.eigolens.ui.camera.ReadabilityPanel

@Composable
fun AnnotationMode(
    capturedImage: CapturedImage,
    viewModel: CaptureFlowViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lookupState by viewModel.lookupState.collectAsState()
    val analysisMode by viewModel.analysisMode.collectAsState()
    val readabilityMetrics by viewModel.readabilityMetrics.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        // Top 40%: Interactive image viewer with lasso
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .background(Color.Black)
        ) {
            InteractiveImageViewer(
                capturedImage = capturedImage,
                onWordsSelected = { words -> viewModel.selectWords(words) },
                modifier = Modifier.fillMaxSize()
            )

            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back to camera",
                    tint = Color.White
                )
            }

            // Word count badge
            val wordCount = capturedImage.ocrResult.texts.sumOf { it.elements.size }
            Text(
                text = "$wordCount words detected",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color.Black.copy(alpha = 0.5f), MaterialTheme.shapes.small)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        // Bottom 60%: Results panel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .background(Color(0xFFF8F9FA))
        ) {
            when (analysisMode) {
                is AnalysisMode.WordLookup -> {
                    when (val state = lookupState) {
                        is LookupState.Success -> {
                            DefinitionPanel(
                                definition = state.definition,
                                onDismiss = {},
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        is LookupState.Loading -> {
                            DefinitionSkeleton(modifier = Modifier.fillMaxSize())
                        }
                        is LookupState.NotFound -> {
                            InstructionsPanel(
                                message = "No definition found for \"${state.word}\"",
                                isError = true
                            )
                        }
                        is LookupState.Error -> {
                            InstructionsPanel(
                                message = state.message ?: "An error occurred",
                                isError = true
                            )
                        }
                        is LookupState.Idle -> {
                            InstructionsPanel(
                                message = "Tap the pencil icon on the image, then draw a circle around any word or phrase to look it up"
                            )
                        }
                    }
                }
                is AnalysisMode.Readability -> {
                    readabilityMetrics?.let { metrics ->
                        ReadabilityPanel(
                            metrics = metrics,
                            onBack = { viewModel.switchToWordLookup() },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // READ / WORD toggle FAB
            FloatingActionButton(
                onClick = {
                    if (analysisMode is AnalysisMode.Readability) {
                        viewModel.switchToWordLookup()
                    } else {
                        viewModel.analyzeReadability()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = if (analysisMode is AnalysisMode.Readability)
                    Color(0xFF4CAF50) else Color(0xFF2196F3),
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(
                        if (analysisMode is AnalysisMode.Readability)
                            R.drawable.ic_search else R.drawable.ic_read
                    ),
                    contentDescription = if (analysisMode is AnalysisMode.Readability)
                        "Word lookup" else "Readability analysis"
                )
            }
        }
    }
}

@Composable
private fun InstructionsPanel(
    message: String,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(
                    if (isError) R.drawable.ic_search else R.drawable.ic_draw
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 16.dp),
                tint = if (isError) Color(0xFFE57373) else Color(0xFF90A4AE)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isError) Color(0xFFE57373) else Color(0xFF78909C),
                textAlign = TextAlign.Center
            )
        }
    }
}
