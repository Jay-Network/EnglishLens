package com.jworks.kanjilens.ui.settings;

import androidx.lifecycle.ViewModel;
import com.jworks.kanjilens.domain.models.AppSettings;
import com.jworks.kanjilens.domain.repository.SettingsRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rJ\u000e\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0013J\u000e\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0016J\u001c\u0010\u0018\u001a\u00020\u000b2\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u001aH\u0002J\u000e\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0013J\u000e\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0013J\u000e\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0016J\u000e\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0013R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/jworks/kanjilens/ui/settings/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "settingsRepository", "Lcom/jworks/kanjilens/domain/repository/SettingsRepository;", "(Lcom/jworks/kanjilens/domain/repository/SettingsRepository;)V", "settings", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/jworks/kanjilens/domain/models/AppSettings;", "getSettings", "()Lkotlinx/coroutines/flow/StateFlow;", "applyColorPreset", "", "kanjiColor", "", "kanaColor", "updateFrameSkip", "value", "", "updateFuriganaIsBold", "", "updateFuriganaUseWhiteText", "updateLabelBackgroundAlpha", "", "updateLabelFontSize", "updateSettings", "transform", "Lkotlin/Function1;", "updateShowBoxes", "updateShowDebugHud", "updateStrokeWidth", "updateVerticalTextMode", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.jworks.kanjilens.domain.repository.SettingsRepository settingsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.jworks.kanjilens.domain.models.AppSettings> settings = null;
    
    @javax.inject.Inject()
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.domain.repository.SettingsRepository settingsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.jworks.kanjilens.domain.models.AppSettings> getSettings() {
        return null;
    }
    
    public final void updateStrokeWidth(float value) {
    }
    
    public final void updateLabelFontSize(float value) {
    }
    
    public final void updateLabelBackgroundAlpha(float value) {
    }
    
    public final void updateFrameSkip(int value) {
    }
    
    public final void updateShowDebugHud(boolean value) {
    }
    
    public final void updateShowBoxes(boolean value) {
    }
    
    public final void updateFuriganaIsBold(boolean value) {
    }
    
    public final void updateFuriganaUseWhiteText(boolean value) {
    }
    
    public final void updateVerticalTextMode(boolean value) {
    }
    
    public final void applyColorPreset(long kanjiColor, long kanaColor) {
    }
    
    private final void updateSettings(kotlin.jvm.functions.Function1<? super com.jworks.kanjilens.domain.models.AppSettings, com.jworks.kanjilens.domain.models.AppSettings> transform) {
    }
}