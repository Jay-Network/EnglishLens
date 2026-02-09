package com.jworks.englishlens.ui.camera

import android.Manifest
import android.view.MotionEvent
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.jworks.englishlens.R
import com.jworks.englishlens.domain.models.Definition

import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

// Data class for detected word entry in the word list panel
private data class WordEntry(val word: String, val count: Int = 1)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onSettingsClick: () -> Unit,
    onGalleryClick: () -> Unit = {},
    onWordSelected: (String) -> Unit = {},
    viewModel: CameraViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        CameraContent(viewModel, onSettingsClick, onGalleryClick, onWordSelected)
    } else {
        CameraPermissionRequest(
            showRationale = cameraPermissionState.status.shouldShowRationale,
            onRequestPermission = { cameraPermissionState.launchPermissionRequest() }
        )
    }
}

@Composable
private fun CameraContent(
    viewModel: CameraViewModel,
    onSettingsClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onWordSelected: (String) -> Unit
) {
    val detectedTexts by viewModel.detectedTexts.collectAsState()
    val selectedWord by viewModel.selectedWord.collectAsState()
    val lookupState by viewModel.lookupState.collectAsState()
    val analysisMode by viewModel.analysisMode.collectAsState()
    val readabilityMetrics by viewModel.readabilityMetrics.collectAsState()
    val sourceImageSize by viewModel.sourceImageSize.collectAsState()
    val rotationDegrees by viewModel.rotationDegrees.collectAsState()
    val isFlashOn by viewModel.isFlashOn.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val ocrStats by viewModel.ocrStats.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    var camera by remember { mutableStateOf<Camera?>(null) }

    // Boundary animation: smooth transition between partial and full screen
    val boundaryAnim = remember { Animatable(0.25f) }
    val displayBoundary = boundaryAnim.value

    LaunchedEffect(Unit) {
        boundaryAnim.snapTo(settings.partialModeBoundaryRatio)
    }

    // Draggable button positions (in px)
    var settingsBtnOffset by remember { mutableStateOf(Offset.Zero) }
    var flashBtnOffset by remember { mutableStateOf(Offset.Zero) }
    var modeBtnOffset by remember { mutableStateOf(Offset.Zero) }
    var readBtnOffset by remember { mutableStateOf(Offset.Zero) }
    var galleryBtnOffset by remember { mutableStateOf(Offset.Zero) }
    var buttonsInitialized by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
            cameraExecutor.awaitTermination(500, TimeUnit.MILLISECONDS)
        }
    }

    LaunchedEffect(isFlashOn) {
        camera?.cameraControl?.enableTorch(isFlashOn)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val maxHeightPx = with(density) { maxHeight.toPx() }
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val statusBarPx = with(density) { 48.dp.toPx() }
        val btnSizePx = with(density) { 44.dp.toPx() }

        // Report canvas size to ViewModel
        LaunchedEffect(maxWidthPx, maxHeightPx) {
            viewModel.updateCanvasSize(android.util.Size(maxWidthPx.toInt(), maxHeightPx.toInt()))
        }

        val isPartial = displayBoundary < 0.99f
        val cameraHeightRatio = PartialModeConstants.CAMERA_HEIGHT_RATIO
        val btnGap = 12f
        val rightMargin = 16f
        val bottomFooterPadding = 160f

        if (!buttonsInitialized) {
            // 1x5 row: bottom-right corner
            val col5 = maxWidthPx - btnSizePx - rightMargin
            val col4 = col5 - btnSizePx - btnGap
            val col3 = col4 - btnSizePx - btnGap
            val col2 = col3 - btnSizePx - btnGap
            val col1 = col2 - btnSizePx - btnGap
            val row1 = maxHeightPx - btnSizePx - bottomFooterPadding
            settingsBtnOffset = Offset(col1, row1)
            flashBtnOffset = Offset(col2, row1)
            modeBtnOffset = Offset(col3, row1)
            readBtnOffset = Offset(col4, row1)
            galleryBtnOffset = Offset(col5, row1)
            buttonsInitialized = true
        }

        // Layer 1: Full-screen camera preview
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER

                    setOnTouchListener { view, event ->
                        if (event.action == MotionEvent.ACTION_UP) {
                            val cam = camera ?: return@setOnTouchListener true
                            val factory = meteringPointFactory
                            val point = factory.createPoint(event.x, event.y)
                            val action = FocusMeteringAction.Builder(point)
                                .setAutoCancelDuration(3, TimeUnit.SECONDS)
                                .build()
                            cam.cameraControl.startFocusAndMetering(action)
                            view.performClick()
                        }
                        true
                    }

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(surfaceProvider)
                        }

                        val imageAnalyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also { analysis ->
                                analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                    viewModel.processFrame(imageProxy)
                                }
                            }

                        cameraProvider.unbindAll()
                        camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageAnalyzer
                        )
                    }, ContextCompat.getMainExecutor(ctx))
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Layer 2: Text overlay (full screen to match camera coordinates)
        if (detectedTexts.isNotEmpty()) {
            TextOverlay(
                detectedTexts = detectedTexts,
                imageWidth = sourceImageSize.width,
                imageHeight = sourceImageSize.height,
                rotationDegrees = rotationDegrees,
                settings = settings,
                selectedWord = selectedWord,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Layer 3: Bottom panel (definition or word list in partial mode)
        if (displayBoundary < 0.99f) {
            var wordList by remember { mutableStateOf<List<WordEntry>>(emptyList()) }
            var wordAccumulator by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

            // Accumulate words from current frame
            LaunchedEffect(detectedTexts) {
                val newWords = detectedTexts.flatMap { detected ->
                    detected.elements
                        .filter { it.isWord && it.text.length >= 2 }
                        .map { it.text.lowercase() }
                }
                val counts = mutableMapOf<String, Int>()
                counts.putAll(wordAccumulator)
                for (w in newWords) {
                    counts[w] = (counts[w] ?: 0) + 1
                }
                wordAccumulator = counts
            }

            // Refresh list every 3 seconds
            LaunchedEffect(Unit) {
                while (true) {
                    kotlinx.coroutines.delay(3000)
                    wordList = wordAccumulator.map { (word, count) ->
                        WordEntry(word, count)
                    }.sortedByDescending { it.count }
                    wordAccumulator = emptyMap()
                }
            }

            val panelHeightDp = with(density) { (maxHeightPx * (1f - cameraHeightRatio)).toDp() }
            val panelModifier = Modifier
                .fillMaxWidth()
                .height(panelHeightDp)
                .align(Alignment.BottomCenter)
                .background(Color(0xFFF8F9FA))
                .border(width = 1.dp, color = Color(0xFFE0E0E0))

            Box(modifier = panelModifier) {
                when (analysisMode) {
                    is AnalysisMode.Readability -> {
                        readabilityMetrics?.let { metrics ->
                            ReadabilityPanel(
                                metrics = metrics,
                                onBack = { viewModel.switchToWordLookup() },
                                modifier = Modifier.fillMaxSize()
                            )
                        } ?: Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Not enough text to analyze. Point camera at text first.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    is AnalysisMode.WordLookup -> {
                        when (val state = lookupState) {
                            is LookupState.Success -> {
                                DefinitionPanel(
                                    definition = state.definition,
                                    onDismiss = { viewModel.dismissDefinition() },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            is LookupState.Loading -> {
                                DefinitionSkeleton(modifier = Modifier.fillMaxSize())
                            }
                            is LookupState.NotFound -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "\"${state.word}\"",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF333333)
                                        )
                                        Text(
                                            text = "Back",
                                            color = Color(0xFF2196F3),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .clickable { viewModel.dismissDefinition() }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = "Word not found in dictionary.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                            is LookupState.Error -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Lookup error",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFFF44336)
                                    )
                                    Text(
                                        text = state.message ?: "Unknown error",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                    Text(
                                        text = "Back",
                                        color = Color(0xFF2196F3),
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .clickable { viewModel.dismissDefinition() }
                                    )
                                }
                            }
                            is LookupState.Idle -> {
                                DetectedWordList(
                                    words = wordList,
                                    selectedWord = selectedWord,
                                    onWordClick = { entry ->
                                        viewModel.selectWord(entry.word)
                                        onWordSelected(entry.word)
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }

        // Layer 4: Processing indicator
        if (isProcessing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(12.dp)
                    .size(24.dp),
                color = Color(0xFF4CAF50),
                strokeWidth = 2.dp
            )
        }

        // Layer 5: Debug HUD (in camera area)
        if (settings.showDebugHud && ocrStats.framesProcessed > 0) {
            val hudY = with(density) {
                if (isPartial) {
                    (maxHeightPx * cameraHeightRatio - 80.dp.toPx()).coerceAtLeast(0f).toDp()
                } else {
                    12.dp
                }
            }
            DebugStatsHud(
                stats = ocrStats,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(y = hudY)
                    .padding(start = 12.dp)
            )
        }

        // Layer 6: Draggable floating settings button
        DraggableFloatingButton(
            offset = settingsBtnOffset,
            onOffsetChange = { settingsBtnOffset = it },
            onClick = onSettingsClick,
            maxWidth = maxWidthPx,
            maxHeight = maxHeightPx,
            btnSize = btnSizePx
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        // Layer 7: Draggable floating flash button
        camera?.let { cam ->
            if (cam.cameraInfo.hasFlashUnit()) {
                DraggableFloatingButton(
                    offset = flashBtnOffset,
                    onOffsetChange = { flashBtnOffset = it },
                    onClick = { viewModel.toggleFlash() },
                    maxWidth = maxWidthPx,
                    maxHeight = maxHeightPx,
                    btnSize = btnSizePx
                ) {
                    Text(
                        if (isFlashOn) "ON" else "OFF",
                        color = if (isFlashOn) Color.Yellow else Color.White,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Layer 8: Mode toggle button (Full ↔ Partial)
        DraggableFloatingButton(
            offset = modeBtnOffset,
            onOffsetChange = { modeBtnOffset = it },
            onClick = {
                val currentMode = settings.partialModeBoundaryRatio
                val newMode = if (currentMode > 0.6f) 0.25f else 1f

                scope.launch {
                    boundaryAnim.animateTo(
                        newMode,
                        spring(dampingRatio = 0.7f, stiffness = 300f)
                    )
                }
                viewModel.updatePartialModeBoundaryRatio(newMode)
            },
            maxWidth = maxWidthPx,
            maxHeight = maxHeightPx,
            btnSize = btnSizePx
        ) {
            Text(
                if (settings.partialModeBoundaryRatio > 0.6f) "LIST" else "FULL",
                color = Color.White,
                fontSize = 10.sp
            )
        }

        // Layer 9: Readability analysis button
        DraggableFloatingButton(
            offset = readBtnOffset,
            onOffsetChange = { readBtnOffset = it },
            onClick = {
                if (analysisMode is AnalysisMode.Readability) {
                    viewModel.switchToWordLookup()
                } else {
                    viewModel.analyzeReadability()
                }
            },
            maxWidth = maxWidthPx,
            maxHeight = maxHeightPx,
            btnSize = btnSizePx
        ) {
            Text(
                if (analysisMode is AnalysisMode.Readability) "WORD" else "READ",
                color = if (analysisMode is AnalysisMode.Readability) Color(0xFF4CAF50) else Color.White,
                fontSize = 9.sp
            )
        }

        // Layer 10: Gallery import button
        DraggableFloatingButton(
            offset = galleryBtnOffset,
            onOffsetChange = { galleryBtnOffset = it },
            onClick = onGalleryClick,
            maxWidth = maxWidthPx,
            maxHeight = maxHeightPx,
            btnSize = btnSizePx
        ) {
            Text(
                "IMG",
                color = Color(0xFF64B5F6),
                fontSize = 9.sp
            )
        }

        // Version label (bottom-left)
        Text(
            text = "v${com.jworks.englishlens.BuildConfig.VERSION_NAME}",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 9.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 8.dp)
        )

        // Layer 11: App brand indicator (top-right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 12.dp, top = 12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF2196F3).copy(alpha = 0.85f))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "EL",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DraggableFloatingButton(
    offset: Offset,
    onOffsetChange: (Offset) -> Unit,
    onClick: () -> Unit,
    maxWidth: Float,
    maxHeight: Float,
    btnSize: Float,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.4f))
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val down = awaitPointerEvent()
                        val firstDown = down.changes.firstOrNull() ?: continue
                        if (!firstDown.pressed) continue
                        firstDown.consume()

                        var totalDrag = Offset.Zero
                        var wasDragged = false

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull() ?: break
                            if (!change.pressed) {
                                change.consume()
                                if (!wasDragged) onClick()
                                break
                            }
                            val delta = change.positionChange()
                            totalDrag += delta
                            if (totalDrag.getDistance() > viewConfiguration.touchSlop) {
                                wasDragged = true
                            }
                            if (wasDragged) {
                                onOffsetChange(
                                    Offset(
                                        (offset.x + delta.x).coerceIn(0f, maxWidth - btnSize),
                                        (offset.y + delta.y).coerceIn(0f, maxHeight - btnSize)
                                    )
                                )
                            }
                            change.consume()
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetectedWordList(
    words: List<WordEntry>,
    selectedWord: String?,
    onWordClick: (WordEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Detected Words (${words.size})",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF333333),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (words.isEmpty()) {
            Text(
                text = "Point camera at English text...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                words.forEach { entry ->
                    val isSelected = entry.word.equals(selectedWord, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) Color(0xFF2196F3)
                                else Color(0xFFE3F2FD)
                            )
                            .clickable { onWordClick(entry) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = entry.word,
                            fontSize = 16.sp,
                            color = if (isSelected) Color.White else Color(0xFF1565C0),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

// DefinitionPanel and DefinitionSkeleton are in DefinitionPanel.kt

@Composable
private fun DebugStatsHud(stats: OCRStats, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(8.dp)
    ) {
        Text(
            text = "OCR: ${stats.avgFrameMs}ms avg",
            color = when {
                stats.avgFrameMs < 200 -> Color(0xFF4CAF50)
                stats.avgFrameMs < 400 -> Color(0xFFFF9800)
                else -> Color(0xFFF44336)
            },
            fontSize = 11.sp
        )
        Text(
            text = "Lines: ${stats.linesDetected} | #${stats.framesProcessed}",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun CameraPermissionRequest(
    showRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (showRationale) {
                "EnglishLens needs camera access to detect and read English text. Please grant the permission."
            } else {
                "Camera permission is required to use EnglishLens."
            },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = onRequestPermission) {
            Text("Grant Camera Permission")
        }
    }
}
