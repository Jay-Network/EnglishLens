package com.jworks.kanjilens.di;

import com.jworks.kanjilens.data.remote.KuroshiroApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class AppModule_ProvideKuroshiroApiFactory implements Factory<KuroshiroApi> {
  private final Provider<Retrofit> retrofitProvider;

  public AppModule_ProvideKuroshiroApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public KuroshiroApi get() {
    return provideKuroshiroApi(retrofitProvider.get());
  }

  public static AppModule_ProvideKuroshiroApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new AppModule_ProvideKuroshiroApiFactory(retrofitProvider);
  }

  public static KuroshiroApi provideKuroshiroApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideKuroshiroApi(retrofit));
  }
}
