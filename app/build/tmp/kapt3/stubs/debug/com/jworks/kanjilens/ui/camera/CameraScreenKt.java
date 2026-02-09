package com.jworks.kanjilens.ui.camera;

import android.Manifest;
import android.view.MotionEvent;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.ExperimentalLayoutApi;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.core.content.ContextCompat;
import com.google.accompanist.permissions.ExperimentalPermissionsApi;
import com.jworks.kanjilens.R;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000^\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a,\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u001e\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a@\u0010\u000b\u001a\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u001a\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001a4\u0010\u0012\u001a\u00020\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u00142\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00010\u00172\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001a \u0010\u0018\u001a\u00020\u00012\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00142\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u001ag\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u001d2\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u00010\u00172\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!2\u0006\u0010#\u001a\u00020!2\u0011\u0010$\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b%H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b&\u0010\'\u001a(\u0010(\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00152\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0003\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006*"}, d2 = {"CameraContent", "", "viewModel", "Lcom/jworks/kanjilens/ui/camera/CameraViewModel;", "onSettingsClick", "Lkotlin/Function0;", "onRewardsClick", "CameraPermissionRequest", "showRationale", "", "onRequestPermission", "CameraScreen", "onPaywallNeeded", "DebugStatsHud", "stats", "Lcom/jworks/kanjilens/ui/camera/OCRStats;", "modifier", "Landroidx/compose/ui/Modifier;", "DetectedJukugoList", "jukugo", "", "Lcom/jworks/kanjilens/ui/camera/JukugoEntry;", "onJukugoClick", "Lkotlin/Function1;", "DetectedKanjiList", "kanji", "", "DraggableFloatingButton", "offset", "Landroidx/compose/ui/geometry/Offset;", "onOffsetChange", "onClick", "maxWidth", "", "maxHeight", "btnSize", "content", "Landroidx/compose/runtime/Composable;", "DraggableFloatingButton-7362WCg", "(JLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;FFFLkotlin/jvm/functions/Function0;)V", "JukugoDictionaryView", "onBackClick", "app_debug"})
public final class CameraScreenKt {
    
    @kotlin.OptIn(markerClass = {com.google.accompanist.permissions.ExperimentalPermissionsApi.class})
    @androidx.compose.runtime.Composable()
    public static final void CameraScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSettingsClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRewardsClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPaywallNeeded, @org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.ui.camera.CameraViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CameraContent(com.jworks.kanjilens.ui.camera.CameraViewModel viewModel, kotlin.jvm.functions.Function0<kotlin.Unit> onSettingsClick, kotlin.jvm.functions.Function0<kotlin.Unit> onRewardsClick) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    private static final void DetectedKanjiList(java.util.List<java.lang.Character> kanji, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DetectedJukugoList(java.util.List<com.jworks.kanjilens.ui.camera.JukugoEntry> jukugo, kotlin.jvm.functions.Function1<? super com.jworks.kanjilens.ui.camera.JukugoEntry, kotlin.Unit> onJukugoClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void JukugoDictionaryView(com.jworks.kanjilens.ui.camera.JukugoEntry jukugo, kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DebugStatsHud(com.jworks.kanjilens.ui.camera.OCRStats stats, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CameraPermissionRequest(boolean showRationale, kotlin.jvm.functions.Function0<kotlin.Unit> onRequestPermission) {
    }
}