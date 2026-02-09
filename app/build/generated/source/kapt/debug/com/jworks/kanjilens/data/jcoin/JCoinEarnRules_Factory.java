package com.jworks.kanjilens.data.jcoin;

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
public final class JCoinEarnRules_Factory implements Factory<JCoinEarnRules> {
  @Override
  public JCoinEarnRules get() {
    return newInstance();
  }

  public static JCoinEarnRules_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static JCoinEarnRules newInstance() {
    return new JCoinEarnRules();
  }

  private static final class InstanceHolder {
    private static final JCoinEarnRules_Factory INSTANCE = new JCoinEarnRules_Factory();
  }
}
