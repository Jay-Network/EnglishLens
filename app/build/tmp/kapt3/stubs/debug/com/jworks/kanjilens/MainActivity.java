package com.jworks.kanjilens;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.compose.ui.Modifier;
import com.jworks.kanjilens.data.auth.AuthRepository;
import com.jworks.kanjilens.data.auth.AuthState;
import com.jworks.kanjilens.data.jcoin.JCoinClient;
import com.jworks.kanjilens.data.jcoin.JCoinEarnRules;
import com.jworks.kanjilens.data.subscription.SubscriptionManager;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0014J\b\u0010\u001f\u001a\u00020\u001cH\u0014R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001e\u0010\u0015\u001a\u00020\u00168\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001a\u00a8\u0006 "}, d2 = {"Lcom/jworks/kanjilens/MainActivity;", "Landroidx/activity/ComponentActivity;", "()V", "authRepository", "Lcom/jworks/kanjilens/data/auth/AuthRepository;", "getAuthRepository", "()Lcom/jworks/kanjilens/data/auth/AuthRepository;", "setAuthRepository", "(Lcom/jworks/kanjilens/data/auth/AuthRepository;)V", "jCoinClient", "Lcom/jworks/kanjilens/data/jcoin/JCoinClient;", "getJCoinClient", "()Lcom/jworks/kanjilens/data/jcoin/JCoinClient;", "setJCoinClient", "(Lcom/jworks/kanjilens/data/jcoin/JCoinClient;)V", "jCoinEarnRules", "Lcom/jworks/kanjilens/data/jcoin/JCoinEarnRules;", "getJCoinEarnRules", "()Lcom/jworks/kanjilens/data/jcoin/JCoinEarnRules;", "setJCoinEarnRules", "(Lcom/jworks/kanjilens/data/jcoin/JCoinEarnRules;)V", "subscriptionManager", "Lcom/jworks/kanjilens/data/subscription/SubscriptionManager;", "getSubscriptionManager", "()Lcom/jworks/kanjilens/data/subscription/SubscriptionManager;", "setSubscriptionManager", "(Lcom/jworks/kanjilens/data/subscription/SubscriptionManager;)V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "app_debug"})
public final class MainActivity extends androidx.activity.ComponentActivity {
    @javax.inject.Inject()
    public com.jworks.kanjilens.data.auth.AuthRepository authRepository;
    @javax.inject.Inject()
    public com.jworks.kanjilens.data.subscription.SubscriptionManager subscriptionManager;
    @javax.inject.Inject()
    public com.jworks.kanjilens.data.jcoin.JCoinClient jCoinClient;
    @javax.inject.Inject()
    public com.jworks.kanjilens.data.jcoin.JCoinEarnRules jCoinEarnRules;
    
    public MainActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.data.auth.AuthRepository getAuthRepository() {
        return null;
    }
    
    public final void setAuthRepository(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.auth.AuthRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.data.subscription.SubscriptionManager getSubscriptionManager() {
        return null;
    }
    
    public final void setSubscriptionManager(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.subscription.SubscriptionManager p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.data.jcoin.JCoinClient getJCoinClient() {
        return null;
    }
    
    public final void setJCoinClient(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.jcoin.JCoinClient p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.data.jcoin.JCoinEarnRules getJCoinEarnRules() {
        return null;
    }
    
    public final void setJCoinEarnRules(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.jcoin.JCoinEarnRules p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
}