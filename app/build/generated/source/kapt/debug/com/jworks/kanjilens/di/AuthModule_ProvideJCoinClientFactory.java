package com.jworks.kanjilens.di;

import com.jworks.kanjilens.data.jcoin.JCoinClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
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
public final class AuthModule_ProvideJCoinClientFactory implements Factory<JCoinClient> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public AuthModule_ProvideJCoinClientFactory(Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public JCoinClient get() {
    return provideJCoinClient(supabaseClientProvider.get());
  }

  public static AuthModule_ProvideJCoinClientFactory create(
      Provider<SupabaseClient> supabaseClientProvider) {
    return new AuthModule_ProvideJCoinClientFactory(supabaseClientProvider);
  }

  public static JCoinClient provideJCoinClient(SupabaseClient supabaseClient) {
    return Preconditions.checkNotNullFromProvides(AuthModule.INSTANCE.provideJCoinClient(supabaseClient));
  }
}
