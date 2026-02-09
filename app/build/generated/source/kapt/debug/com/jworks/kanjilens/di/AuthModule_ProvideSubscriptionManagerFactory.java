package com.jworks.kanjilens.di;

import com.jworks.kanjilens.data.auth.AuthRepository;
import com.jworks.kanjilens.data.subscription.SubscriptionManager;
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
public final class AuthModule_ProvideSubscriptionManagerFactory implements Factory<SubscriptionManager> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public AuthModule_ProvideSubscriptionManagerFactory(
      Provider<SupabaseClient> supabaseClientProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public SubscriptionManager get() {
    return provideSubscriptionManager(supabaseClientProvider.get(), authRepositoryProvider.get());
  }

  public static AuthModule_ProvideSubscriptionManagerFactory create(
      Provider<SupabaseClient> supabaseClientProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new AuthModule_ProvideSubscriptionManagerFactory(supabaseClientProvider, authRepositoryProvider);
  }

  public static SubscriptionManager provideSubscriptionManager(SupabaseClient supabaseClient,
      AuthRepository authRepository) {
    return Preconditions.checkNotNullFromProvides(AuthModule.INSTANCE.provideSubscriptionManager(supabaseClient, authRepository));
  }
}
