package com.jworks.kanjilens.data.subscription;

import com.jworks.kanjilens.data.auth.AuthRepository;
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
public final class SubscriptionManager_Factory implements Factory<SubscriptionManager> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public SubscriptionManager_Factory(Provider<SupabaseClient> supabaseClientProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public SubscriptionManager get() {
    return newInstance(supabaseClientProvider.get(), authRepositoryProvider.get());
  }

  public static SubscriptionManager_Factory create(Provider<SupabaseClient> supabaseClientProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new SubscriptionManager_Factory(supabaseClientProvider, authRepositoryProvider);
  }

  public static SubscriptionManager newInstance(SupabaseClient supabaseClient,
      AuthRepository authRepository) {
    return new SubscriptionManager(supabaseClient, authRepository);
  }
}
