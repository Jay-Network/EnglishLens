package com.jworks.kanjilens.data.repository;

import com.jworks.kanjilens.data.local.JMDictDao;
import com.jworks.kanjilens.data.remote.KuroshiroApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class FuriganaRepositoryImpl_Factory implements Factory<FuriganaRepositoryImpl> {
  private final Provider<JMDictDao> jmDictDaoProvider;

  private final Provider<KuroshiroApi> kuroshiroApiProvider;

  public FuriganaRepositoryImpl_Factory(Provider<JMDictDao> jmDictDaoProvider,
      Provider<KuroshiroApi> kuroshiroApiProvider) {
    this.jmDictDaoProvider = jmDictDaoProvider;
    this.kuroshiroApiProvider = kuroshiroApiProvider;
  }

  @Override
  public FuriganaRepositoryImpl get() {
    return newInstance(jmDictDaoProvider.get(), kuroshiroApiProvider.get());
  }

  public static FuriganaRepositoryImpl_Factory create(Provider<JMDictDao> jmDictDaoProvider,
      Provider<KuroshiroApi> kuroshiroApiProvider) {
    return new FuriganaRepositoryImpl_Factory(jmDictDaoProvider, kuroshiroApiProvider);
  }

  public static FuriganaRepositoryImpl newInstance(JMDictDao jmDictDao, KuroshiroApi kuroshiroApi) {
    return new FuriganaRepositoryImpl(jmDictDao, kuroshiroApi);
  }
}
