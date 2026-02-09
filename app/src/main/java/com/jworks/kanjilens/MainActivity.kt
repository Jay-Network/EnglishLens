package com.jworks.kanjilens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jworks.kanjilens.data.auth.AuthRepository
import com.jworks.kanjilens.data.auth.AuthState
import com.jworks.kanjilens.data.jcoin.JCoinClient
import com.jworks.kanjilens.data.jcoin.JCoinEarnRules
import com.jworks.kanjilens.data.subscription.SubscriptionManager
import com.jworks.kanjilens.ui.auth.AuthScreen
import com.jworks.kanjilens.ui.camera.CameraScreen
import com.jworks.kanjilens.ui.paywall.PaywallScreen
import com.jworks.kanjilens.ui.rewards.RewardsScreen
import com.jworks.kanjilens.ui.settings.SettingsScreen
import com.jworks.kanjilens.ui.theme.KanjiLensTheme
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var authRepository: AuthRepository
    @Inject lateinit var subscriptionManager: SubscriptionManager
    @Inject lateinit var jCoinClient: JCoinClient
    @Inject lateinit var jCoinEarnRules: JCoinEarnRules

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KanjiLensTheme {
                val navController = rememberNavController()
                val authState by authRepository.authState.collectAsState()
                var hasSkippedAuth by remember { mutableStateOf(false) }

                // Check auth state on launch
                LaunchedEffect(Unit) {
                    authRepository.refreshAuthState()
                }

                // Auto-navigate when auth state changes
                LaunchedEffect(authState) {
                    if (authState is AuthState.SignedIn) {
                        subscriptionManager.checkSubscription()
                        val current = navController.currentDestination?.route
                        if (current == "auth" || current == null) {
                            navController.navigate("camera") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "auth"
                    ) {
                        composable("auth") {
                            if (authState is AuthState.SignedIn || hasSkippedAuth) {
                                LaunchedEffect(Unit) {
                                    navController.navigate("camera") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                }
                            } else {
                                AuthScreen(
                                    authRepository = authRepository,
                                    onSkip = {
                                        hasSkippedAuth = true
                                        navController.navigate("camera") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    },
                                    onSignedIn = {
                                        navController.navigate("camera") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }

                        composable("camera") {
                            CameraScreen(
                                onSettingsClick = { navController.navigate("settings") },
                                onRewardsClick = { navController.navigate("rewards") },
                                onPaywallNeeded = { navController.navigate("paywall") }
                            )
                        }

                        composable("settings") {
                            SettingsScreen(
                                onBackClick = { navController.popBackStack() },
                                onLogout = {
                                    lifecycleScope.launch {
                                        authRepository.signOut()
                                        hasSkippedAuth = false
                                        navController.navigate("auth") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }

                        composable("paywall") {
                            PaywallScreen(
                                subscriptionManager = subscriptionManager,
                                remainingScans = subscriptionManager.getRemainingScans(this@MainActivity),
                                onDismiss = { navController.popBackStack() }
                            )
                        }

                        composable("rewards") {
                            RewardsScreen(
                                authRepository = authRepository,
                                jCoinClient = jCoinClient,
                                earnRules = jCoinEarnRules,
                                subscriptionManager = subscriptionManager,
                                onBackClick = { navController.popBackStack() },
                                onUpgradeClick = {
                                    navController.navigate("paywall")
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            if (authRepository.authState.value is AuthState.SignedIn) {
                subscriptionManager.checkSubscription()
            }
        }
    }
}
