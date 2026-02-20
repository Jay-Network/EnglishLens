package com.jworks.englishlens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jworks.englishlens.ui.auth.LoginScreen
import com.jworks.englishlens.ui.capture.CaptureFlowScreen
import com.jworks.englishlens.ui.feedback.FeedbackDialog
import com.jworks.englishlens.ui.feedback.FeedbackFAB
import com.jworks.englishlens.ui.feedback.FeedbackViewModel
import com.jworks.englishlens.ui.gallery.GalleryImportScreen
import com.jworks.englishlens.ui.settings.SettingsScreen
import com.jworks.englishlens.ui.theme.EnglishLensTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EnglishLensTheme {
                val navController = rememberNavController()
                val feedbackViewModel: FeedbackViewModel = hiltViewModel()
                val feedbackUiState by feedbackViewModel.uiState.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    Scaffold(
                        containerColor = Color.Transparent,
                        floatingActionButton = {
                            FeedbackFAB(onClick = { feedbackViewModel.openDialog() })
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "login",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("login") {
                                LoginScreen(
                                    onLoginSuccess = {
                                        navController.navigate("capture") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    },
                                    onContinueAsGuest = {
                                        navController.navigate("capture") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("capture") {
                                CaptureFlowScreen(
                                    onSettingsClick = { navController.navigate("settings") },
                                    onGalleryClick = { navController.navigate("gallery") }
                                )
                            }

                            composable("settings") {
                                SettingsScreen(
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            composable("gallery") {
                                GalleryImportScreen(
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }

                        if (feedbackUiState.isDialogOpen) {
                            FeedbackDialog(
                                onDismiss = { feedbackViewModel.closeDialog() },
                                viewModel = feedbackViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
