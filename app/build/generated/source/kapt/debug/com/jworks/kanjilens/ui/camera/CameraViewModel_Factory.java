package com.jworks.kanjilens.ui.camera;

import com.jworks.kanjilens.domain.repository.SettingsRepository;
import com.jworks.kanjilens.domain.usecases.EnrichWithFuriganaUseCase;
import com.jworks.kanjilens.domain.usecases.ProcessCameraFrameUseCase;
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
public final class CameraViewModel_Factory implements Factory<CameraViewModel> {
  private final Provider<ProcessCameraFrameUseCase> processCameraFrameProvider;

  private final Provider<EnrichWithFuriganaUseCase> enrichWithFuriganaProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public CameraViewModel_Factory(Provider<ProcessCameraFrameUseCase> processCameraFrameProvider,
      Provider<EnrichWithFuriganaUseCase> enrichWithFuriganaProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.processCameraFrameProvider = processCameraFrameProvider;
    this.enrichWithFuriganaProvider = enrichWithFuriganaProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public CameraViewModel get() {
    return newInstance(processCameraFrameProvider.get(), enrichWithFuriganaProvider.get(), settingsRepositoryProvider.get());
  }

  public static CameraViewModel_Factory create(
      Provider<ProcessCameraFrameUseCase> processCameraFrameProvider,
      Provider<EnrichWithFuriganaUseCase> enrichWithFuriganaProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new CameraViewModel_Factory(processCameraFrameProvider, enrichWithFuriganaProvider, settingsRepositoryProvider);
  }

  public static CameraViewModel newInstance(ProcessCameraFrameUseCase processCameraFrame,
      EnrichWithFuriganaUseCase enrichWithFurigana, SettingsRepository settingsRepository) {
    return new CameraViewModel(processCameraFrame, enrichWithFurigana, settingsRepository);
  }
}
