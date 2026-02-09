package com.jworks.kanjilens.domain.usecases;

import android.util.Log;
import android.util.Size;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognizer;
import com.jworks.kanjilens.domain.models.DetectedText;
import com.jworks.kanjilens.domain.models.JapaneseTextUtil;
import com.jworks.kanjilens.domain.models.OCRResult;
import com.jworks.kanjilens.domain.models.TextElement;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/jworks/kanjilens/domain/usecases/ProcessCameraFrameUseCase;", "", "textRecognizer", "Lcom/google/mlkit/vision/text/TextRecognizer;", "(Lcom/google/mlkit/vision/text/TextRecognizer;)V", "execute", "Lcom/jworks/kanjilens/domain/models/OCRResult;", "inputImage", "Lcom/google/mlkit/vision/common/InputImage;", "imageSize", "Landroid/util/Size;", "(Lcom/google/mlkit/vision/common/InputImage;Landroid/util/Size;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class ProcessCameraFrameUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.google.mlkit.vision.text.TextRecognizer textRecognizer = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "OCR";
    private static final float MIN_JAPANESE_RATIO = 0.3F;
    @org.jetbrains.annotations.NotNull()
    public static final com.jworks.kanjilens.domain.usecases.ProcessCameraFrameUseCase.Companion Companion = null;
    
    @javax.inject.Inject()
    public ProcessCameraFrameUseCase(@org.jetbrains.annotations.NotNull()
    com.google.mlkit.vision.text.TextRecognizer textRecognizer) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object execute(@org.jetbrains.annotations.NotNull()
    com.google.mlkit.vision.common.InputImage inputImage, @org.jetbrains.annotations.NotNull()
    android.util.Size imageSize, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.jworks.kanjilens.domain.models.OCRResult> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/jworks/kanjilens/domain/usecases/ProcessCameraFrameUseCase$Companion;", "", "()V", "MIN_JAPANESE_RATIO", "", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}