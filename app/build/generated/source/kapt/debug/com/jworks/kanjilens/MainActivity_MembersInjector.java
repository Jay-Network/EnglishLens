package com.jworks.kanjilens;

import com.jworks.kanjilens.data.auth.AuthRepository;
import com.jworks.kanjilens.data.jcoin.JCoinClient;
import com.jworks.kanjilens.data.jcoin.JCoinEarnRules;
import com.jworks.kanjilens.data.subscription.SubscriptionManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<SubscriptionManager> subscriptionManagerProvider;

  private final Provider<JCoinClient> jCoinClientProvider;

  private final Provider<JCoinEarnRules> jCoinEarnRulesProvider;

  public MainActivity_MembersInjector(Provider<AuthRepository> authRepositoryProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider,
      Provider<JCoinClient> jCoinClientProvider, Provider<JCoinEarnRules> jCoinEarnRulesProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.subscriptionManagerProvider = subscriptionManagerProvider;
    this.jCoinClientProvider = jCoinClientProvider;
    this.jCoinEarnRulesProvider = jCoinEarnRulesProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider,
      Provider<JCoinClient> jCoinClientProvider, Provider<JCoinEarnRules> jCoinEarnRulesProvider) {
    return new MainActivity_MembersInjector(authRepositoryProvider, subscriptionManagerProvider, jCoinClientProvider, jCoinEarnRulesProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectAuthRepository(instance, authRepositoryProvider.get());
    injectSubscriptionManager(instance, subscriptionManagerProvider.get());
    injectJCoinClient(instance, jCoinClientProvider.get());
    injectJCoinEarnRules(instance, jCoinEarnRulesProvider.get());
  }

  @InjectedFieldSignature("com.jworks.kanjilens.MainActivity.authRepository")
  public static void injectAuthRepository(MainActivity instance, AuthRepository authRepository) {
    instance.authRepository = authRepository;
  }

  @InjectedFieldSignature("com.jworks.kanjilens.MainActivity.subscriptionManager")
  public static void injectSubscriptionManager(MainActivity instance,
      SubscriptionManager subscriptionManager) {
    instance.subscriptionManager = subscriptionManager;
  }

  @InjectedFieldSignature("com.jworks.kanjilens.MainActivity.jCoinClient")
  public static void injectJCoinClient(MainActivity instance, JCoinClient jCoinClient) {
    instance.jCoinClient = jCoinClient;
  }

  @InjectedFieldSignature("com.jworks.kanjilens.MainActivity.jCoinEarnRules")
  public static void injectJCoinEarnRules(MainActivity instance, JCoinEarnRules jCoinEarnRules) {
    instance.jCoinEarnRules = jCoinEarnRules;
  }
}
