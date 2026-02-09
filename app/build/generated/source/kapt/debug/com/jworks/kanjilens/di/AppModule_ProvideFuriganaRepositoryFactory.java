package com.jworks.kanjilens.di;

import com.jworks.kanjilens.data.repository.FuriganaRepositoryImpl;
import com.jworks.kanjilens.domain.repository.FuriganaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AppModule_ProvideFuriganaRepositoryFactory implements Factory<FuriganaRepository> {
  private final Provider<FuriganaRepositoryImpl> implProvider;

  public AppModule_ProvideFuriganaRepositoryFactory(Provider<FuriganaRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public FuriganaRepository get() {
    return provideFuriganaRepository(implProvider.get());
  }

  public static AppModule_ProvideFuriganaRepositoryFactory create(
      Provider<FuriganaRepositoryImpl> implProvider) {
    return new AppModule_ProvideFuriganaRepositoryFactory(implProvider);
  }

  public static FuriganaRepository provideFuriganaRepository(FuriganaRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFuriganaRepository(impl));
  }
}
