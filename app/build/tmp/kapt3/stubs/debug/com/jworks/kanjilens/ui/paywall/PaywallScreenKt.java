package com.jworks.kanjilens.ui.paywall;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import com.jworks.kanjilens.data.subscription.SubscriptionManager;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0003\u001a&\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0007\u001aB\u0010\f\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u00a8\u0006\u0013"}, d2 = {"FeatureRow", "", "title", "", "description", "PaywallScreen", "subscriptionManager", "Lcom/jworks/kanjilens/data/subscription/SubscriptionManager;", "remainingScans", "", "onDismiss", "Lkotlin/Function0;", "PlanCard", "price", "period", "savings", "isSelected", "", "onClick", "app_debug"})
public final class PaywallScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void PaywallScreen(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.subscription.SubscriptionManager subscriptionManager, int remainingScans, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FeatureRow(java.lang.String title, java.lang.String description) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PlanCard(java.lang.String title, java.lang.String price, java.lang.String period, java.lang.String savings, boolean isSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}