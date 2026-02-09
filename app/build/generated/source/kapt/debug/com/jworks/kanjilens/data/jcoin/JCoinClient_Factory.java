package com.jworks.kanjilens.data.jcoin;

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
public final class JCoinClient_Factory implements Factory<JCoinClient> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public JCoinClient_Factory(Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public JCoinClient get() {
    return newInstance(supabaseClientProvider.get());
  }

  public static JCoinClient_Factory create(Provider<SupabaseClient> supabaseClientProvider) {
    return new JCoinClient_Factory(supabaseClientProvider);
  }

  public static JCoinClient newInstance(SupabaseClient supabaseClient) {
    return new JCoinClient(supabaseClient);
  }
}
