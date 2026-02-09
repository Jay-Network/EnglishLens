package com.jworks.kanjilens.data.auth;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public AuthRepository_Factory(Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(supabaseClientProvider.get());
  }

  public static AuthRepository_Factory create(Provider<SupabaseClient> supabaseClientProvider) {
    return new AuthRepository_Factory(supabaseClientProvider);
  }

  public static AuthRepository newInstance(SupabaseClient supabaseClient) {
    return new AuthRepository(supabaseClient);
  }
}
