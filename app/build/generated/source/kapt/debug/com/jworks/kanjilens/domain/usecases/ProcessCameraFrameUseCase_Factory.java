package com.jworks.kanjilens.domain.usecases;

import com.google.mlkit.vision.text.TextRecognizer;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ProcessCameraFrameUseCase_Factory implements Factory<ProcessCameraFrameUseCase> {
  private final Provider<TextRecognizer> textRecognizerProvider;

  public ProcessCameraFrameUseCase_Factory(Provider<TextRecognizer> textRecognizerProvider) {
    this.textRecognizerProvider = textRecognizerProvider;
  }

  @Override
  public ProcessCameraFrameUseCase get() {
    return newInstance(textRecognizerProvider.get());
  }

  public static ProcessCameraFrameUseCase_Factory create(
      Provider<TextRecognizer> textRecognizerProvider) {
    return new ProcessCameraFrameUseCase_Factory(textRecognizerProvider);
  }

  public static ProcessCameraFrameUseCase newInstance(TextRecognizer textRecognizer) {
    return new ProcessCameraFrameUseCase(textRecognizer);
  }
}
