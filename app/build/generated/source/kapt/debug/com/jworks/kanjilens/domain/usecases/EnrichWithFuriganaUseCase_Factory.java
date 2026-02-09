package com.jworks.kanjilens.domain.usecases;

import com.jworks.kanjilens.data.nlp.KuromojiTokenizer;
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
public final class EnrichWithFuriganaUseCase_Factory implements Factory<EnrichWithFuriganaUseCase> {
  private final Provider<FuriganaRepository> furiganaRepositoryProvider;

  private final Provider<KuromojiTokenizer> kuromojiTokenizerProvider;

  public EnrichWithFuriganaUseCase_Factory(Provider<FuriganaRepository> furiganaRepositoryProvider,
      Provider<KuromojiTokenizer> kuromojiTokenizerProvider) {
    this.furiganaRepositoryProvider = furiganaRepositoryProvider;
    this.kuromojiTokenizerProvider = kuromojiTokenizerProvider;
  }

  @Override
  public EnrichWithFuriganaUseCase get() {
    return newInstance(furiganaRepositoryProvider.get(), kuromojiTokenizerProvider.get());
  }

  public static EnrichWithFuriganaUseCase_Factory create(
      Provider<FuriganaRepository> furiganaRepositoryProvider,
      Provider<KuromojiTokenizer> kuromojiTokenizerProvider) {
    return new EnrichWithFuriganaUseCase_Factory(furiganaRepositoryProvider, kuromojiTokenizerProvider);
  }

  public static EnrichWithFuriganaUseCase newInstance(FuriganaRepository furiganaRepository,
      KuromojiTokenizer kuromojiTokenizer) {
    return new EnrichWithFuriganaUseCase(furiganaRepository, kuromojiTokenizer);
  }
}
