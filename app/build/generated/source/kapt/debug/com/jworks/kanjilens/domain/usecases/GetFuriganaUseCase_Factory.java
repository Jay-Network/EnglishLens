package com.jworks.kanjilens.domain.usecases;

import com.jworks.kanjilens.domain.repository.FuriganaRepository;
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
public final class GetFuriganaUseCase_Factory implements Factory<GetFuriganaUseCase> {
  private final Provider<FuriganaRepository> repositoryProvider;

  public GetFuriganaUseCase_Factory(Provider<FuriganaRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetFuriganaUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetFuriganaUseCase_Factory create(Provider<FuriganaRepository> repositoryProvider) {
    return new GetFuriganaUseCase_Factory(repositoryProvider);
  }

  public static GetFuriganaUseCase newInstance(FuriganaRepository repository) {
    return new GetFuriganaUseCase(repository);
  }
}
