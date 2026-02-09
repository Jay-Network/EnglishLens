package com.jworks.kanjilens.data.subscription

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.jworks.kanjilens.data.auth.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions
import io.ktor.client.call.body
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class SubscriptionStatus(
    val isActive: Boolean = false,
    val plan: String? = null, // "premium_monthly" or "premium_annual"
    val currentPeriodEnd: String? = null,
    val cancelAtPeriodEnd: Boolean = false
)

@Singleton
class SubscriptionManager @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val authRepository: AuthRepository
) {
    private val _subscriptionStatus = MutableStateFlow(SubscriptionStatus())
    val subscriptionStatus: StateFlow<SubscriptionStatus> = _subscriptionStatus.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true }

    companion object {
        const val FREE_SCAN_LIMIT = 5
        const val SCAN_COUNT_KEY = "daily_scan_count"
        const val SCAN_DATE_KEY = "daily_scan_date"
    }

    suspend fun checkSubscription(): SubscriptionStatus {
        val token = authRepository.getAccessToken() ?: return SubscriptionStatus()

        return try {
            val response = supabaseClient.functions.invoke(
                function = "verify-subscription",
                headers = Headers.build {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            )
            val body = response.body<String>()
            val status = json.decodeFromString<SubscriptionStatus>(body)
            _subscriptionStatus.value = status
            status
        } catch (e: Exception) {
            SubscriptionStatus()
        }
    }

    suspend fun createCheckoutSession(plan: String): String? {
        val token = authRepository.getAccessToken() ?: return null

        return try {
            val requestBody = buildJsonObject {
                put("plan", plan)
                put("app", "kanjilens")
            }
            val response = supabaseClient.functions.invoke(
                function = "create-checkout-session",
                body = requestBody,
                headers = Headers.build {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            )
            val body = response.body<String>()

            @Serializable
            data class CheckoutResponse(val url: String)
            val checkout = json.decodeFromString<CheckoutResponse>(body)
            checkout.url
        } catch (e: Exception) {
            null
        }
    }

    fun openCheckout(context: Context, url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    suspend fun cancelSubscription(): Boolean {
        val token = authRepository.getAccessToken() ?: return false

        return try {
            supabaseClient.functions.invoke(
                function = "cancel-subscription",
                headers = Headers.build {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            )
            checkSubscription()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun isPremium(): Boolean = _subscriptionStatus.value.isActive

    // Feature gates: FREE vs PREMIUM
    /** Unlimited scans (premium) vs 5/day (free) */
    fun hasUnlimitedScans(): Boolean = isPremium()
    /** Scan history (premium only) */
    fun hasHistory(): Boolean = isPremium()
    /** Offline dictionary access (premium only) */
    fun hasOfflineDictionary(): Boolean = isPremium()
    /** J Coin earning (premium only) */
    fun hasJCoinEarning(): Boolean = isPremium()
    /** Favorites saving (premium only) */
    fun hasFavorites(): Boolean = isPremium()

    /**
     * Track daily scan count using SharedPreferences.
     * Returns true if the scan is allowed (premium or under limit).
     */
    fun canScan(context: Context): Boolean {
        if (isPremium()) return true

        val prefs = context.getSharedPreferences("kanjilens_usage", Context.MODE_PRIVATE)
        val today = java.time.LocalDate.now().toString()
        val savedDate = prefs.getString(SCAN_DATE_KEY, null)

        if (savedDate != today) {
            // Reset for new day
            prefs.edit()
                .putString(SCAN_DATE_KEY, today)
                .putInt(SCAN_COUNT_KEY, 0)
                .apply()
            return true
        }

        return prefs.getInt(SCAN_COUNT_KEY, 0) < FREE_SCAN_LIMIT
    }

    fun incrementScanCount(context: Context) {
        if (isPremium()) return

        val prefs = context.getSharedPreferences("kanjilens_usage", Context.MODE_PRIVATE)
        val today = java.time.LocalDate.now().toString()
        val savedDate = prefs.getString(SCAN_DATE_KEY, null)

        if (savedDate != today) {
            prefs.edit()
                .putString(SCAN_DATE_KEY, today)
                .putInt(SCAN_COUNT_KEY, 1)
                .apply()
        } else {
            val current = prefs.getInt(SCAN_COUNT_KEY, 0)
            prefs.edit().putInt(SCAN_COUNT_KEY, current + 1).apply()
        }
    }

    fun getRemainingScans(context: Context): Int {
        if (isPremium()) return Int.MAX_VALUE

        val prefs = context.getSharedPreferences("kanjilens_usage", Context.MODE_PRIVATE)
        val today = java.time.LocalDate.now().toString()
        val savedDate = prefs.getString(SCAN_DATE_KEY, null)

        if (savedDate != today) return FREE_SCAN_LIMIT

        return (FREE_SCAN_LIMIT - prefs.getInt(SCAN_COUNT_KEY, 0)).coerceAtLeast(0)
    }
}
