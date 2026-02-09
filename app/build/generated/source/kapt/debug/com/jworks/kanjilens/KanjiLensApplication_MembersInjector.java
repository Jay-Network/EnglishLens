package com.jworks.kanjilens;

import com.jworks.kanjilens.data.nlp.KuromojiTokenizer;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class KanjiLensApplication_MembersInjector implements MembersInjector<KanjiLensApplication> {
  private final Provider<KuromojiTokenizer> kuromojiTokenizerProvider;

  public KanjiLensApplication_MembersInjector(
      Provider<KuromojiTokenizer> kuromojiTokenizerProvider) {
    this.kuromojiTokenizerProvider = kuromojiTokenizerProvider;
  }

  public static MembersInjector<KanjiLensApplication> create(
      Provider<KuromojiTokenizer> kuromojiTokenizerProvider) {
    return new KanjiLensApplication_MembersInjector(kuromojiTokenizerProvider);
  }

  @Override
  public void injectMembers(KanjiLensApplication instance) {
    injectKuromojiTokenizer(instance, kuromojiTokenizerProvider.get());
  }

  @InjectedFieldSignature("com.jworks.kanjilens.KanjiLensApplication.kuromojiTokenizer")
  public static void injectKuromojiTokenizer(KanjiLensApplication instance,
      KuromojiTokenizer kuromojiTokenizer) {
    instance.kuromojiTokenizer = kuromojiTokenizer;
  }
}
