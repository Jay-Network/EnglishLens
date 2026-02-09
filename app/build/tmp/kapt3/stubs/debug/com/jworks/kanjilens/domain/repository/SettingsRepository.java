package com.jworks.kanjilens.domain.repository;

import com.jworks.kanjilens.domain.models.AppSettings;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0002\u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010\tR\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\n"}, d2 = {"Lcom/jworks/kanjilens/domain/repository/SettingsRepository;", "", "settings", "Lkotlinx/coroutines/flow/Flow;", "Lcom/jworks/kanjilens/domain/models/AppSettings;", "getSettings", "()Lkotlinx/coroutines/flow/Flow;", "updateSettings", "", "(Lcom/jworks/kanjilens/domain/models/AppSettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface SettingsRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.jworks.kanjilens.domain.models.AppSettings> getSettings();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSettings(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.domain.models.AppSettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}