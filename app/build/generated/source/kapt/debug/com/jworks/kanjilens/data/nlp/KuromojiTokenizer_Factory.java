package com.jworks.kanjilens.data.nlp;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class KuromojiTokenizer_Factory implements Factory<KuromojiTokenizer> {
  @Override
  public KuromojiTokenizer get() {
    return newInstance();
  }

  public static KuromojiTokenizer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static KuromojiTokenizer newInstance() {
    return new KuromojiTokenizer();
  }

  private static final class InstanceHolder {
    private static final KuromojiTokenizer_Factory INSTANCE = new KuromojiTokenizer_Factory();
  }
}
