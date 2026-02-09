package com.jworks.kanjilens.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import javax.annotation.processing.Generated;

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
public final class AuthModule_ProvideJCoinSupabaseClientFactory implements Factory<SupabaseClient> {
  @Override
  public SupabaseClient get() {
    return provideJCoinSupabaseClient();
  }

  public static AuthModule_ProvideJCoinSupabaseClientFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SupabaseClient provideJCoinSupabaseClient() {
    return Preconditions.checkNotNullFromProvides(AuthModule.INSTANCE.provideJCoinSupabaseClient());
  }

  private static final class InstanceHolder {
    private static final AuthModule_ProvideJCoinSupabaseClientFactory INSTANCE = new AuthModule_ProvideJCoinSupabaseClientFactory();
  }
}
