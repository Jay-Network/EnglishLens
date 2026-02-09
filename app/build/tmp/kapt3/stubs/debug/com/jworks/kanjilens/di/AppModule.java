package com.jworks.kanjilens.di;

import android.content.Context;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions;
import com.jworks.kanjilens.data.preferences.SettingsDataStore;
import com.jworks.kanjilens.data.remote.KuroshiroApi;
import com.jworks.kanjilens.data.repository.FuriganaRepositoryImpl;
import com.jworks.kanjilens.data.repository.SettingsRepositoryImpl;
import com.jworks.kanjilens.domain.repository.FuriganaRepository;
import com.jworks.kanjilens.domain.repository.SettingsRepository;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\b\u0010\u000b\u001a\u00020\nH\u0007J\u0012\u0010\f\u001a\u00020\r2\b\b\u0001\u0010\u000e\u001a\u00020\u000fH\u0007J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u0012H\u0007J\b\u0010\u0013\u001a\u00020\u0014H\u0007\u00a8\u0006\u0015"}, d2 = {"Lcom/jworks/kanjilens/di/AppModule;", "", "()V", "provideFuriganaRepository", "Lcom/jworks/kanjilens/domain/repository/FuriganaRepository;", "impl", "Lcom/jworks/kanjilens/data/repository/FuriganaRepositoryImpl;", "provideKuroshiroApi", "Lcom/jworks/kanjilens/data/remote/KuroshiroApi;", "retrofit", "Lretrofit2/Retrofit;", "provideRetrofit", "provideSettingsDataStore", "Lcom/jworks/kanjilens/data/preferences/SettingsDataStore;", "context", "Landroid/content/Context;", "provideSettingsRepository", "Lcom/jworks/kanjilens/domain/repository/SettingsRepository;", "Lcom/jworks/kanjilens/data/repository/SettingsRepositoryImpl;", "provideTextRecognizer", "Lcom/google/mlkit/vision/text/TextRecognizer;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.jworks.kanjilens.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.google.mlkit.vision.text.TextRecognizer provideTextRecognizer() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final retrofit2.Retrofit provideRetrofit() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.data.remote.KuroshiroApi provideKuroshiroApi(@org.jetbrains.annotations.NotNull()
    retrofit2.Retrofit retrofit) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.domain.repository.FuriganaRepository provideFuriganaRepository(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.repository.FuriganaRepositoryImpl impl) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.data.preferences.SettingsDataStore provideSettingsDataStore(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.domain.repository.SettingsRepository provideSettingsRepository(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.repository.SettingsRepositoryImpl impl) {
        return null;
    }
}