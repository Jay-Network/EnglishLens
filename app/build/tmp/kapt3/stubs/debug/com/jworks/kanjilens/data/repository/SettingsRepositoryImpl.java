package com.jworks.kanjilens.data.repository;

import com.jworks.kanjilens.data.preferences.SettingsDataStore;
import com.jworks.kanjilens.domain.models.AppSettings;
import com.jworks.kanjilens.domain.repository.SettingsRepository;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u0007H\u0096@\u00a2\u0006\u0002\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\r"}, d2 = {"Lcom/jworks/kanjilens/data/repository/SettingsRepositoryImpl;", "Lcom/jworks/kanjilens/domain/repository/SettingsRepository;", "dataStore", "Lcom/jworks/kanjilens/data/preferences/SettingsDataStore;", "(Lcom/jworks/kanjilens/data/preferences/SettingsDataStore;)V", "settings", "Lkotlinx/coroutines/flow/Flow;", "Lcom/jworks/kanjilens/domain/models/AppSettings;", "getSettings", "()Lkotlinx/coroutines/flow/Flow;", "updateSettings", "", "(Lcom/jworks/kanjilens/domain/models/AppSettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class SettingsRepositoryImpl implements com.jworks.kanjilens.domain.repository.SettingsRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.jworks.kanjilens.data.preferences.SettingsDataStore dataStore = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.jworks.kanjilens.domain.models.AppSettings> settings = null;
    
    @javax.inject.Inject()
    public SettingsRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.preferences.SettingsDataStore dataStore) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.jworks.kanjilens.domain.models.AppSettings> getSettings() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateSettings(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.domain.models.AppSettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}