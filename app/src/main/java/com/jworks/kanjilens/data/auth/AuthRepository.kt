package com.jworks.kanjilens.data.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class AuthUser(
    val id: String,
    val email: String?,
    val displayName: String?,
    val avatarUrl: String?
)

sealed class AuthState {
    data object Loading : AuthState()
    data object SignedOut : AuthState()
    data class SignedIn(val user: AuthUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

@Singleton
class AuthRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var googleSignInClient: GoogleSignInClient? = null

    fun initGoogleSignIn(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(SupabaseConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent? {
        return googleSignInClient?.signInIntent
    }

    suspend fun handleSignInResult(data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken

            if (idToken != null) {
                supabaseClient.auth.signInWith(IDToken) {
                    provider = Google
                    this.idToken = idToken
                }
                refreshAuthState()
            } else {
                _authState.value = AuthState.Error("No ID token received from Google")
            }
        } catch (e: ApiException) {
            _authState.value = AuthState.Error("Google sign-in failed: ${e.statusCode}")
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Authentication failed: ${e.message}")
        }
    }

    suspend fun refreshAuthState() {
        try {
            val session = supabaseClient.auth.currentSessionOrNull()
            if (session != null) {
                val user = session.user
                if (user != null) {
                    _authState.value = AuthState.SignedIn(
                        AuthUser(
                            id = user.id,
                            email = user.email,
                            displayName = user.userMetadata?.get("full_name")?.toString()?.trim('"'),
                            avatarUrl = user.userMetadata?.get("avatar_url")?.toString()?.trim('"')
                        )
                    )
                    return
                }
            }
            _authState.value = AuthState.SignedOut
        } catch (e: Exception) {
            _authState.value = AuthState.SignedOut
        }
    }

    suspend fun signOut() {
        try {
            supabaseClient.auth.signOut()
            googleSignInClient?.signOut()
        } catch (_: Exception) { }
        _authState.value = AuthState.SignedOut
    }

    fun getCurrentUserId(): String? {
        val state = _authState.value
        return if (state is AuthState.SignedIn) state.user.id else null
    }

    fun getAccessToken(): String? {
        return try {
            supabaseClient.auth.currentAccessTokenOrNull()
        } catch (_: Exception) {
            null
        }
    }
}
