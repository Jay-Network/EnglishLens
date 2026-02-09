package com.jworks.englishlens.ui.capture

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CaptureFlowScreen(
    onSettingsClick: () -> Unit,
    onGalleryClick: () -> Unit,
    viewModel: CaptureFlowViewModel = hiltViewModel()
) {
    val captureState by viewModel.captureState.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val context = LocalContext.current

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap?.let { bmp -> viewModel.onGalleryImport(bmp) }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = captureState) {
            is CaptureState.Camera -> {
                CameraPreviewMode(
                    isFlashOn = viewModel.isFlashOn.collectAsState().value,
                    onCapture = { bitmap -> viewModel.onPhotoCapture(bitmap) },
                    onFlashToggle = { viewModel.toggleFlash() },
                    onGalleryClick = {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onSettingsClick = onSettingsClick
                )
            }

            is CaptureState.Annotate -> {
                AnnotationMode(
                    capturedImage = state.capturedImage,
                    viewModel = viewModel,
                    onBack = { viewModel.resetToCamera() }
                )
            }
        }

        if (isProcessing) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}
