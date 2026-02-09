package com.jworks.kanjilens.ui.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;
import android.util.Size;
import androidx.camera.core.ImageProxy;
import androidx.lifecycle.ViewModel;
import com.google.mlkit.vision.common.InputImage;
import com.jworks.kanjilens.domain.models.AppSettings;
import com.jworks.kanjilens.domain.models.DetectedText;
import com.jworks.kanjilens.domain.models.TextElement;
import com.jworks.kanjilens.domain.repository.SettingsRepository;
import com.jworks.kanjilens.domain.usecases.EnrichWithFuriganaUseCase;
import com.jworks.kanjilens.domain.usecases.ProcessCameraFrameUseCase;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import java.io.ByteArrayOutputStream;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00a0\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u000f\b\u0007\u0018\u0000 X2\u00020\u0001:\u0002XYB\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u001c\u00106\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\f\u00107\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u0002J0\u00108\u001a\u00020\u00182\u0006\u00109\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010:\u001a\u00020;2\u0006\u0010)\u001a\u00020\u00152\u0006\u0010<\u001a\u00020\u0010H\u0002J\u0018\u0010=\u001a\u00020;2\u0006\u0010>\u001a\u00020\u00182\u0006\u0010?\u001a\u00020\u0018H\u0002J\u001a\u0010@\u001a\u0004\u0018\u0001032\u0006\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020\u0018H\u0002J\u0010\u0010D\u001a\u00020E2\u0006\u0010F\u001a\u00020GH\u0002J \u0010H\u001a\u00020\u00182\u0006\u0010>\u001a\u00020\u00182\u0006\u0010?\u001a\u00020\u00182\u0006\u0010I\u001a\u00020;H\u0002J\u000e\u0010J\u001a\u00020K2\u0006\u0010F\u001a\u00020GJ\u0006\u0010L\u001a\u00020KJ\u000e\u0010M\u001a\u00020K2\u0006\u0010N\u001a\u00020\u000bJ\u000e\u0010O\u001a\u00020K2\u0006\u0010P\u001a\u00020;J\u0018\u0010Q\u001a\u00020K2\u0006\u0010R\u001a\u00020(2\u0006\u0010S\u001a\u00020\u0015H\u0002J\u0016\u0010T\u001a\u00020K2\u0006\u0010U\u001a\u00020\u00102\u0006\u0010P\u001a\u00020;J\u000e\u0010V\u001a\u00020K2\u0006\u0010W\u001a\u00020\u0010R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u001d\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001cR\u000e\u0010\u001f\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00100\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001cR\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00100\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001cR\u000e\u0010#\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00130\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001cR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010&\u001a\b\u0012\u0004\u0012\u00020(0\'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00150\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001cR\u0017\u0010+\u001a\b\u0012\u0004\u0012\u00020,0\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001cR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010.\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001cR\u001a\u00100\u001a\u000e\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020301X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0019\u00104\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010\u001c\u00a8\u0006Z"}, d2 = {"Lcom/jworks/kanjilens/ui/camera/CameraViewModel;", "Landroidx/lifecycle/ViewModel;", "processCameraFrame", "Lcom/jworks/kanjilens/domain/usecases/ProcessCameraFrameUseCase;", "enrichWithFurigana", "Lcom/jworks/kanjilens/domain/usecases/EnrichWithFuriganaUseCase;", "settingsRepository", "Lcom/jworks/kanjilens/domain/repository/SettingsRepository;", "(Lcom/jworks/kanjilens/domain/usecases/ProcessCameraFrameUseCase;Lcom/jworks/kanjilens/domain/usecases/EnrichWithFuriganaUseCase;Lcom/jworks/kanjilens/domain/repository/SettingsRepository;)V", "_canvasSize", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Landroid/util/Size;", "_detectedTexts", "", "Lcom/jworks/kanjilens/domain/models/DetectedText;", "_isFlashOn", "", "_isProcessing", "_ocrStats", "Lcom/jworks/kanjilens/ui/camera/OCRStats;", "_rotationDegrees", "", "_sourceImageSize", "_visibleRegion", "Landroid/graphics/Rect;", "canvasSize", "Lkotlinx/coroutines/flow/StateFlow;", "getCanvasSize", "()Lkotlinx/coroutines/flow/StateFlow;", "detectedTexts", "getDetectedTexts", "emptyFrameCount", "frameCount", "isFlashOn", "isProcessing", "modeSwitchPauseFrames", "ocrStats", "getOcrStats", "recentTimings", "Lkotlin/collections/ArrayDeque;", "", "rotationDegrees", "getRotationDegrees", "settings", "Lcom/jworks/kanjilens/domain/models/AppSettings;", "getSettings", "sourceImageSize", "getSourceImageSize", "trackedElements", "", "", "Lcom/jworks/kanjilens/ui/camera/CameraViewModel$TrackedElement;", "visibleRegion", "getVisibleRegion", "applyPositionStabilization", "texts", "calculateVisibleRegion", "imageSize", "displayBoundary", "", "isVerticalMode", "distanceBetweenRects", "r1", "r2", "findMatchingElement", "element", "Lcom/jworks/kanjilens/domain/models/TextElement;", "bounds", "imageProxyToBitmap", "Landroid/graphics/Bitmap;", "imageProxy", "Landroidx/camera/core/ImageProxy;", "lerpRect", "alpha", "processFrame", "", "toggleFlash", "updateCanvasSize", "size", "updatePartialModeBoundaryRatio", "ratio", "updateStats", "processingTimeMs", "lineCount", "updateVerticalModeAndBoundary", "verticalMode", "updateVerticalTextMode", "enabled", "Companion", "TrackedElement", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CameraViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.jworks.kanjilens.domain.usecases.ProcessCameraFrameUseCase processCameraFrame = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jworks.kanjilens.domain.usecases.EnrichWithFuriganaUseCase enrichWithFurigana = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jworks.kanjilens.domain.repository.SettingsRepository settingsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "CameraVM";
    private static final int STATS_WINDOW = 30;
    private static final float SMOOTHING_ALPHA = 0.3F;
    private static final float MAX_TRACKING_DISTANCE = 100.0F;
    private static final int PERSIST_FRAMES = 3;
    @org.jetbrains.annotations.NotNull()
    private java.util.Map<java.lang.String, com.jworks.kanjilens.ui.camera.CameraViewModel.TrackedElement> trackedElements;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.jworks.kanjilens.domain.models.AppSettings> settings = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.jworks.kanjilens.domain.models.DetectedText>> _detectedTexts = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.jworks.kanjilens.domain.models.DetectedText>> detectedTexts = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isProcessing = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isProcessing = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<android.util.Size> _sourceImageSize = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<android.util.Size> sourceImageSize = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _rotationDegrees = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> rotationDegrees = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<android.util.Size> _canvasSize = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<android.util.Size> canvasSize = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isFlashOn = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isFlashOn = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<android.graphics.Rect> _visibleRegion = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<android.graphics.Rect> visibleRegion = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.jworks.kanjilens.ui.camera.OCRStats> _ocrStats = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.jworks.kanjilens.ui.camera.OCRStats> ocrStats = null;
    private int frameCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.collections.ArrayDeque<java.lang.Long> recentTimings = null;
    private int modeSwitchPauseFrames = 0;
    private int emptyFrameCount = 0;
    @org.jetbrains.annotations.NotNull()
    public static final com.jworks.kanjilens.ui.camera.CameraViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public CameraViewModel(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.domain.usecases.ProcessCameraFrameUseCase processCameraFrame, @org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.domain.usecases.EnrichWithFuriganaUseCase enrichWithFurigana, @org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.domain.repository.SettingsRepository settingsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.jworks.kanjilens.domain.models.AppSettings> getSettings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.jworks.kanjilens.domain.models.DetectedText>> getDetectedTexts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isProcessing() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<android.util.Size> getSourceImageSize() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getRotationDegrees() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<android.util.Size> getCanvasSize() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isFlashOn() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<android.graphics.Rect> getVisibleRegion() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.jworks.kanjilens.ui.camera.OCRStats> getOcrStats() {
        return null;
    }
    
    public final void toggleFlash() {
    }
    
    public final void updateCanvasSize(@org.jetbrains.annotations.NotNull()
    android.util.Size size) {
    }
    
    public final void updateVerticalTextMode(boolean enabled) {
    }
    
    public final void updatePartialModeBoundaryRatio(float ratio) {
    }
    
    /**
     * Update both vertical mode and boundary ratio atomically to avoid race condition
     * where two separate coroutines read stale settings.value and overwrite each other.
     */
    public final void updateVerticalModeAndBoundary(boolean verticalMode, float ratio) {
    }
    
    public final void processFrame(@org.jetbrains.annotations.NotNull()
    androidx.camera.core.ImageProxy imageProxy) {
    }
    
    /**
     * Calculate which portion of camera frame is visible on screen.
     * Uses FILL_CENTER scaling to map screen region back to image coordinates.
     *
     * Horizontal partial: top HORIZ_CAMERA_HEIGHT_RATIO of screen height, full width
     * Vertical partial: right VERT_CAMERA_WIDTH_RATIO of screen width, top VERT_PAD_TOP_RATIO of height
     */
    private final android.graphics.Rect calculateVisibleRegion(android.util.Size imageSize, android.util.Size canvasSize, float displayBoundary, int rotationDegrees, boolean isVerticalMode) {
        return null;
    }
    
    private final void updateStats(long processingTimeMs, int lineCount) {
    }
    
    /**
     * Apply position stabilization to minimize jitter from hand shake.
     * Smooths bounding box positions for text elements seen in consecutive frames.
     */
    private final java.util.List<com.jworks.kanjilens.domain.models.DetectedText> applyPositionStabilization(java.util.List<com.jworks.kanjilens.domain.models.DetectedText> texts) {
        return null;
    }
    
    /**
     * Find a matching element from previous frame based on text content and proximity
     */
    private final com.jworks.kanjilens.ui.camera.CameraViewModel.TrackedElement findMatchingElement(com.jworks.kanjilens.domain.models.TextElement element, android.graphics.Rect bounds) {
        return null;
    }
    
    /**
     * Calculate distance between centers of two rectangles
     */
    private final float distanceBetweenRects(android.graphics.Rect r1, android.graphics.Rect r2) {
        return 0.0F;
    }
    
    /**
     * Linear interpolation between two rectangles
     * @param alpha: 0.0 = fully r1, 1.0 = fully r2
     */
    private final android.graphics.Rect lerpRect(android.graphics.Rect r1, android.graphics.Rect r2, float alpha) {
        return null;
    }
    
    /**
     * Convert ImageProxy to Bitmap for explicit cropping
     */
    private final android.graphics.Bitmap imageProxyToBitmap(androidx.camera.core.ImageProxy imageProxy) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/jworks/kanjilens/ui/camera/CameraViewModel$Companion;", "", "()V", "MAX_TRACKING_DISTANCE", "", "PERSIST_FRAMES", "", "SMOOTHING_ALPHA", "STATS_WINDOW", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0007H\u00c6\u0003J\'\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0019"}, d2 = {"Lcom/jworks/kanjilens/ui/camera/CameraViewModel$TrackedElement;", "", "text", "", "smoothedBounds", "Landroid/graphics/Rect;", "lastSeenTime", "", "(Ljava/lang/String;Landroid/graphics/Rect;J)V", "getLastSeenTime", "()J", "getSmoothedBounds", "()Landroid/graphics/Rect;", "getText", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class TrackedElement {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String text = null;
        @org.jetbrains.annotations.NotNull()
        private final android.graphics.Rect smoothedBounds = null;
        private final long lastSeenTime = 0L;
        
        public TrackedElement(@org.jetbrains.annotations.NotNull()
        java.lang.String text, @org.jetbrains.annotations.NotNull()
        android.graphics.Rect smoothedBounds, long lastSeenTime) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getText() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.graphics.Rect getSmoothedBounds() {
            return null;
        }
        
        public final long getLastSeenTime() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.graphics.Rect component2() {
            return null;
        }
        
        public final long component3() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.jworks.kanjilens.ui.camera.CameraViewModel.TrackedElement copy(@org.jetbrains.annotations.NotNull()
        java.lang.String text, @org.jetbrains.annotations.NotNull()
        android.graphics.Rect smoothedBounds, long lastSeenTime) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}