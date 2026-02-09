package com.jworks.kanjilens.di;

import com.jworks.kanjilens.data.auth.AuthRepository;
import com.jworks.kanjilens.data.auth.SupabaseConfig;
import com.jworks.kanjilens.data.jcoin.JCoinClient;
import com.jworks.kanjilens.data.subscription.SubscriptionManager;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.github.jan.supabase.SupabaseClient;
import io.github.jan.supabase.functions.Functions;
import io.github.jan.supabase.gotrue.Auth;
import io.github.jan.supabase.postgrest.Postgrest;
import javax.inject.Named;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\b\u0010\u0007\u001a\u00020\u0006H\u0007J\u0012\u0010\b\u001a\u00020\t2\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\b\u0010\n\u001a\u00020\u0006H\u0007J\u001a\u0010\u000b\u001a\u00020\f2\b\b\u0001\u0010\u0005\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0004H\u0007\u00a8\u0006\u000e"}, d2 = {"Lcom/jworks/kanjilens/di/AuthModule;", "", "()V", "provideAuthRepository", "Lcom/jworks/kanjilens/data/auth/AuthRepository;", "supabaseClient", "Lio/github/jan/supabase/SupabaseClient;", "provideAuthSupabaseClient", "provideJCoinClient", "Lcom/jworks/kanjilens/data/jcoin/JCoinClient;", "provideJCoinSupabaseClient", "provideSubscriptionManager", "Lcom/jworks/kanjilens/data/subscription/SubscriptionManager;", "authRepository", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AuthModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.jworks.kanjilens.di.AuthModule INSTANCE = null;
    
    private AuthModule() {
        super();
    }
    
    /**
     * KanjiLens auth + subscriptions Supabase client
     */
    @dagger.Provides()
    @javax.inject.Singleton()
    @javax.inject.Named(value = "auth")
    @org.jetbrains.annotations.NotNull()
    public final io.github.jan.supabase.SupabaseClient provideAuthSupabaseClient() {
        return null;
    }
    
    /**
     * Shared J Coin backend Supabase client (inygcrdhfmoerborxehq)
     */
    @dagger.Provides()
    @javax.inject.Singleton()
    @javax.inject.Named(value = "jcoin")
    @org.jetbrains.annotations.NotNull()
    public final io.github.jan.supabase.SupabaseClient provideJCoinSupabaseClient() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.data.auth.AuthRepository provideAuthRepository(@javax.inject.Named(value = "auth")
    @org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabaseClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.data.subscription.SubscriptionManager provideSubscriptionManager(@javax.inject.Named(value = "auth")
    @org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabaseClient, @org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.auth.AuthRepository authRepository) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.data.jcoin.JCoinClient provideJCoinClient(@javax.inject.Named(value = "jcoin")
    @org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabaseClient) {
        return null;
    }
}