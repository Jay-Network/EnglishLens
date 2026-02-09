package com.jworks.englishlens.ui.capture

import android.graphics.Bitmap
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.jworks.englishlens.R

@Composable
fun CameraPreviewMode(
    isFlashOn: Boolean,
    onCapture: (Bitmap) -> Unit,
    onFlashToggle: () -> Unit,
    onGalleryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var camera by remember { mutableStateOf<Camera?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    // Apply flash state
    camera?.cameraControl?.enableTorch(isFlashOn)

    Box(modifier = modifier.fillMaxSize()) {
        // Full-screen camera preview (NO ImageAnalysis)
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    previewView = this

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(surfaceProvider)
                        }
                        cameraProvider.unbindAll()
                        camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview  // Only preview, no ImageAnalysis
                        )
                    }, ContextCompat.getMainExecutor(context))
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Settings button (top-left)
        SmallFloatingActionButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            containerColor = Color.Black.copy(alpha = 0.5f),
            contentColor = Color.White
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "Settings"
            )
        }

        // Flash toggle (top-right)
        SmallFloatingActionButton(
            onClick = onFlashToggle,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            containerColor = if (isFlashOn) Color(0xFFFFEB3B) else Color.Black.copy(alpha = 0.5f),
            contentColor = if (isFlashOn) Color.Black else Color.White
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_flash),
                contentDescription = if (isFlashOn) "Flash On" else "Flash Off"
            )
        }

        // Gallery import (bottom-left)
        SmallFloatingActionButton(
            onClick = onGalleryClick,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 40.dp),
            containerColor = Color.Black.copy(alpha = 0.5f),
            contentColor = Color.White
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_gallery),
                contentDescription = "Import from gallery"
            )
        }

        // Capture button (bottom-center)
        FloatingActionButton(
            onClick = {
                previewView?.bitmap?.let { onCapture(it) }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .size(72.dp),
            shape = CircleShape,
            containerColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
                    .background(Color.White, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .background(Color(0xFF2196F3), CircleShape)
                )
            }
        }
    }
}
