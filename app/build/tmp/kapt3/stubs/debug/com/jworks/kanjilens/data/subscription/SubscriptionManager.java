package com.jworks.kanjilens.data.subscription;

import android.content.Context;
import android.net.Uri;
import androidx.browser.customtabs.CustomTabsIntent;
import com.jworks.kanjilens.data.auth.AuthRepository;
import io.github.jan.supabase.SupabaseClient;
import io.github.jan.supabase.functions.Functions;
import io.ktor.http.Headers;
import io.ktor.http.HttpHeaders;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.serialization.Serializable;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 \'2\u00020\u0001:\u0001\'B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u000e\u0010\u0016\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010\u0015J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u0018H\u0086@\u00a2\u0006\u0002\u0010\u001aJ\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u001d\u001a\u00020\u0011J\u0006\u0010\u001e\u001a\u00020\u0011J\u0006\u0010\u001f\u001a\u00020\u0011J\u0006\u0010 \u001a\u00020\u0011J\u0006\u0010!\u001a\u00020\u0011J\u000e\u0010\"\u001a\u00020#2\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010$\u001a\u00020\u0011J\u0016\u0010%\u001a\u00020#2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010&\u001a\u00020\u0018R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/jworks/kanjilens/data/subscription/SubscriptionManager;", "", "supabaseClient", "Lio/github/jan/supabase/SupabaseClient;", "authRepository", "Lcom/jworks/kanjilens/data/auth/AuthRepository;", "(Lio/github/jan/supabase/SupabaseClient;Lcom/jworks/kanjilens/data/auth/AuthRepository;)V", "_subscriptionStatus", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/jworks/kanjilens/data/subscription/SubscriptionStatus;", "json", "Lkotlinx/serialization/json/Json;", "subscriptionStatus", "Lkotlinx/coroutines/flow/StateFlow;", "getSubscriptionStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "canScan", "", "context", "Landroid/content/Context;", "cancelSubscription", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkSubscription", "createCheckoutSession", "", "plan", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRemainingScans", "", "hasFavorites", "hasHistory", "hasJCoinEarning", "hasOfflineDictionary", "hasUnlimitedScans", "incrementScanCount", "", "isPremium", "openCheckout", "url", "Companion", "app_debug"})
public final class SubscriptionManager {
    @org.jetbrains.annotations.NotNull()
    private final io.github.jan.supabase.SupabaseClient supabaseClient = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jworks.kanjilens.data.auth.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.jworks.kanjilens.data.subscription.SubscriptionStatus> _subscriptionStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.jworks.kanjilens.data.subscription.SubscriptionStatus> subscriptionStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.serialization.json.Json json = null;
    public static final int FREE_SCAN_LIMIT = 5;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SCAN_COUNT_KEY = "daily_scan_count";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SCAN_DATE_KEY = "daily_scan_date";
    @org.jetbrains.annotations.NotNull()
    public static final com.jworks.kanjilens.data.subscription.SubscriptionManager.Companion Companion = null;
    
    @javax.inject.Inject()
    public SubscriptionManager(@org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabaseClient, @org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.auth.AuthRepository authRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.jworks.kanjilens.data.subscription.SubscriptionStatus> getSubscriptionStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object checkSubscription(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.jworks.kanjilens.data.subscription.SubscriptionStatus> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createCheckoutSession(@org.jetbrains.annotations.NotNull()
    java.lang.String plan, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    public final void openCheckout(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String url) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object cancelSubscription(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    public final boolean isPremium() {
        return false;
    }
    
    /**
     * Unlimited scans (premium) vs 5/day (free)
     */
    public final boolean hasUnlimitedScans() {
        return false;
    }
    
    /**
     * Scan history (premium only)
     */
    public final boolean hasHistory() {
        return false;
    }
    
    /**
     * Offline dictionary access (premium only)
     */
    public final boolean hasOfflineDictionary() {
        return false;
    }
    
    /**
     * J Coin earning (premium only)
     */
    public final boolean hasJCoinEarning() {
        return false;
    }
    
    /**
     * Favorites saving (premium only)
     */
    public final boolean hasFavorites() {
        return false;
    }
    
    /**
     * Track daily scan count using SharedPreferences.
     * Returns true if the scan is allowed (premium or under limit).
     */
    public final boolean canScan(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    public final void incrementScanCount(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final int getRemainingScans(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/jworks/kanjilens/data/subscription/SubscriptionManager$Companion;", "", "()V", "FREE_SCAN_LIMIT", "", "SCAN_COUNT_KEY", "", "SCAN_DATE_KEY", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}