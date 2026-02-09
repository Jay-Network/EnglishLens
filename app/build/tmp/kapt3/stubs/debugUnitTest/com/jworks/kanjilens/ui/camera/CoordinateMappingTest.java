package com.jworks.kanjilens.ui.camera;

import org.junit.Test;

/**
 * Tests the coordinate mapping math used in OverlayCanvas.
 * Extracted as pure functions to avoid Compose/Canvas dependencies.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b \u0018\u00002\u00020\u0001:\u0002;<B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\b\u0010\u0005\u001a\u00020\u0004H\u0007J\b\u0010\u0006\u001a\u00020\u0004H\u0007J\b\u0010\u0007\u001a\u00020\u0004H\u0007J\b\u0010\b\u001a\u00020\u0004H\u0007J\b\u0010\t\u001a\u00020\u0004H\u0007J\b\u0010\n\u001a\u00020\u0004H\u0007J\b\u0010\u000b\u001a\u00020\u0004H\u0007J\b\u0010\f\u001a\u00020\u0004H\u0007J\b\u0010\r\u001a\u00020\u0004H\u0007JB\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u0018H\u0002J<\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140\u001b2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0011H\u0002JP\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u00142\u0006\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u00112\u0006\u0010#\u001a\u00020\u00112\u0006\u0010$\u001a\u00020\u00112\u0006\u0010%\u001a\u00020\u00142\u0006\u0010&\u001a\u00020\u0014H\u0002J \u0010\'\u001a\u00020\u00112\u0006\u0010(\u001a\u00020\u00112\u0006\u0010)\u001a\u00020\u00142\u0006\u0010*\u001a\u00020\u0014H\u0002J8\u0010+\u001a\u00020\u000f2\u0006\u0010,\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0011H\u0002J\b\u0010-\u001a\u00020\u0004H\u0007J\u0018\u0010.\u001a\u00020\u00182\u0006\u0010/\u001a\u00020\u000f2\u0006\u00100\u001a\u00020\u000fH\u0002J\b\u00101\u001a\u00020\u0004H\u0007J\b\u00102\u001a\u00020\u0004H\u0007J\b\u00103\u001a\u00020\u0004H\u0007J\b\u00104\u001a\u00020\u0004H\u0007J\b\u00105\u001a\u00020\u0004H\u0007J\b\u00106\u001a\u00020\u0004H\u0007J\b\u00107\u001a\u00020\u0004H\u0007J\b\u00108\u001a\u00020\u0004H\u0007J\b\u00109\u001a\u00020\u0004H\u0007J\b\u0010:\u001a\u00020\u0004H\u0007\u00a8\u0006="}, d2 = {"Lcom/jworks/kanjilens/ui/camera/CoordinateMappingTest;", "", "()V", "1-to-1 scale when canvas matches image", "", "180 degree rotation does not swap", "270 degree rotation also swaps", "90 degree rotation swaps image dimensions", "FILL_CENTER crops correctly on narrow canvas", "Z Flip 7 - bottom-right VISIBLE region", "Z Flip 7 - square sensor 90 degree rotation", "Z Flip 7 - square sensor FILL_CENTER on portrait screen", "Z Flip 7 - top-left VISIBLE region", "bounding box scales correctly", "calculateVisibleRegionForTest", "Lcom/jworks/kanjilens/ui/camera/CoordinateMappingTest$TestRect;", "imageWidth", "", "imageHeight", "canvasWidth", "", "canvasHeight", "rotationDegrees", "isVerticalMode", "", "isFullMode", "computeScale", "Lkotlin/Pair;", "computeVerticalSegment", "Lcom/jworks/kanjilens/ui/camera/CoordinateMappingTest$VerticalSegmentResult;", "elemLeft", "elemTop", "elemWidth", "elemHeight", "textLength", "startIndex", "endIndex", "furiganaWidth", "furiganaHeight", "imageHeightQuarter", "imageSize", "canvasH", "canvasW", "mapImageToScreen", "imageBounds", "no rotation maps directly", "testRectsIntersect", "a", "b", "vertical mode - charHeight divides element evenly", "vertical mode - furigana clamped to top of screen", "vertical mode - furigana placed to the right of element", "vertical mode - furigana vertically centered on segment", "vertical mode - segment top positioned correctly", "visible region - full mode returns entire image", "visible region - horizontal partial is top 25 percent", "visible region - vertical partial filters bottom pad", "visible region - vertical partial filters panel area", "visible region - vertical partial is top-right quadrant", "TestRect", "VerticalSegmentResult", "app_debugUnitTest"})
public final class CoordinateMappingTest {
    
    public CoordinateMappingTest() {
        super();
    }
    
    private final kotlin.Pair<java.lang.Float, java.lang.Float> computeScale(float canvasWidth, float canvasHeight, int imageWidth, int imageHeight, int rotationDegrees) {
        return null;
    }
    
    private final com.jworks.kanjilens.ui.camera.CoordinateMappingTest.TestRect mapImageToScreen(com.jworks.kanjilens.ui.camera.CoordinateMappingTest.TestRect imageBounds, int imageWidth, int imageHeight, float canvasWidth, float canvasHeight, int rotationDegrees) {
        return null;
    }
    
    private final com.jworks.kanjilens.ui.camera.CoordinateMappingTest.VerticalSegmentResult computeVerticalSegment(float elemLeft, float elemTop, float elemWidth, float elemHeight, int textLength, int startIndex, int endIndex, float furiganaWidth, float furiganaHeight) {
        return null;
    }
    
    /**
     * Mirrors calculateVisibleRegion() from CameraViewModel.
     * Maps screen-space partial mode region back to image coordinates.
     */
    private final com.jworks.kanjilens.ui.camera.CoordinateMappingTest.TestRect calculateVisibleRegionForTest(int imageWidth, int imageHeight, float canvasWidth, float canvasHeight, int rotationDegrees, boolean isVerticalMode, boolean isFullMode) {
        return null;
    }
    
    private final boolean testRectsIntersect(com.jworks.kanjilens.ui.camera.CoordinateMappingTest.TestRect a, com.jworks.kanjilens.ui.camera.CoordinateMappingTest.TestRect b) {
        return false;
    }
    
    /**
     * Helper: approximate image-space height for 25% of canvas
     */
    private final int imageHeightQuarter(int imageSize, float canvasH, float canvasW) {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J1\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00d6\u0001J\u0006\u0010\u0016\u001a\u00020\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\u0006\u0010\u0019\u001a\u00020\u0003R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\u001a"}, d2 = {"Lcom/jworks/kanjilens/ui/camera/CoordinateMappingTest$TestRect;", "", "left", "", "top", "right", "bottom", "(IIII)V", "getBottom", "()I", "getLeft", "getRight", "getTop", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "height", "toString", "", "width", "app_debugUnitTest"})
    public static final class TestRect {
        private final int left = 0;
        private final int top = 0;
        private final int right = 0;
        private final int bottom = 0;
        
        public TestRect(int left, int top, int right, int bottom) {
            super();
        }
        
        public final int getLeft() {
            return 0;
        }
        
        public final int getTop() {
            return 0;
        }
        
        public final int getRight() {
            return 0;
        }
        
        public final int getBottom() {
            return 0;
        }
        
        public final int width() {
            return 0;
        }
        
        public final int height() {
            return 0;
        }
        
        public final int component1() {
            return 0;
        }
        
        public final int component2() {
            return 0;
        }
        
        public final int component3() {
            return 0;
        }
        
        public final int component4() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.jworks.kanjilens.ui.camera.CoordinateMappingTest.TestRect copy(int left, int top, int right, int bottom) {
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
    
    /**
     * Mirrors the vertical segment positioning logic in OverlayCanvas.drawKanjiSegments
     * when isVerticalMode=true: charHeight = elemHeight / textLength,
     * segTop = elemTop + startIndex * charHeight
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J1\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\u0019"}, d2 = {"Lcom/jworks/kanjilens/ui/camera/CoordinateMappingTest$VerticalSegmentResult;", "", "segTop", "", "segHeight", "furiganaBgLeft", "furiganaBgTop", "(FFFF)V", "getFuriganaBgLeft", "()F", "getFuriganaBgTop", "getSegHeight", "getSegTop", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debugUnitTest"})
    static final class VerticalSegmentResult {
        private final float segTop = 0.0F;
        private final float segHeight = 0.0F;
        private final float furiganaBgLeft = 0.0F;
        private final float furiganaBgTop = 0.0F;
        
        public VerticalSegmentResult(float segTop, float segHeight, float furiganaBgLeft, float furiganaBgTop) {
            super();
        }
        
        public final float getSegTop() {
            return 0.0F;
        }
        
        public final float getSegHeight() {
            return 0.0F;
        }
        
        public final float getFuriganaBgLeft() {
            return 0.0F;
        }
        
        public final float getFuriganaBgTop() {
            return 0.0F;
        }
        
        public final float component1() {
            return 0.0F;
        }
        
        public final float component2() {
            return 0.0F;
        }
        
        public final float component3() {
            return 0.0F;
        }
        
        public final float component4() {
            return 0.0F;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.jworks.kanjilens.ui.camera.CoordinateMappingTest.VerticalSegmentResult copy(float segTop, float segHeight, float furiganaBgLeft, float furiganaBgTop) {
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