package com.jworks.kanjilens.ui.rewards;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import com.jworks.kanjilens.R;
import com.jworks.kanjilens.data.auth.AuthRepository;
import com.jworks.kanjilens.data.auth.AuthState;
import com.jworks.kanjilens.data.jcoin.JCoinBalance;
import com.jworks.kanjilens.data.jcoin.JCoinClient;
import com.jworks.kanjilens.data.jcoin.JCoinEarnRules;
import com.jworks.kanjilens.data.subscription.SubscriptionManager;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a2\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a \u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003H\u0003\u001a(\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0014H\u0003\u001aF\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00010\u001f2\u000e\b\u0002\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\u001fH\u0007\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006!"}, d2 = {"DailyProgressCard", "", "label", "", "current", "", "max", "color", "Landroidx/compose/ui/graphics/Color;", "DailyProgressCard-g2O1Hgs", "(Ljava/lang/String;IIJ)V", "EarnRuleRow", "action", "reward", "frequency", "RedemptionCard", "title", "cost", "description", "enabled", "", "RewardsScreen", "authRepository", "Lcom/jworks/kanjilens/data/auth/AuthRepository;", "jCoinClient", "Lcom/jworks/kanjilens/data/jcoin/JCoinClient;", "earnRules", "Lcom/jworks/kanjilens/data/jcoin/JCoinEarnRules;", "subscriptionManager", "Lcom/jworks/kanjilens/data/subscription/SubscriptionManager;", "onBackClick", "Lkotlin/Function0;", "onUpgradeClick", "app_debug"})
public final class RewardsScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void RewardsScreen(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.auth.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.jcoin.JCoinClient jCoinClient, @org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.jcoin.JCoinEarnRules earnRules, @org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.subscription.SubscriptionManager subscriptionManager, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onUpgradeClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EarnRuleRow(java.lang.String action, java.lang.String reward, java.lang.String frequency) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RedemptionCard(java.lang.String title, java.lang.String cost, java.lang.String description, boolean enabled) {
    }
}