package com.jworks.kanjilens.di;

import com.google.mlkit.vision.text.TextRecognizer;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideTextRecognizerFactory implements Factory<TextRecognizer> {
  @Override
  public TextRecognizer get() {
    return provideTextRecognizer();
  }

  public static AppModule_ProvideTextRecognizerFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TextRecognizer provideTextRecognizer() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideTextRecognizer());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideTextRecognizerFactory INSTANCE = new AppModule_ProvideTextRecognizerFactory();
  }
}
