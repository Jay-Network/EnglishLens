package com.jworks.kanjilens;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.mlkit.vision.text.TextRecognizer;
import com.jworks.kanjilens.data.auth.AuthRepository;
import com.jworks.kanjilens.data.jcoin.JCoinClient;
import com.jworks.kanjilens.data.jcoin.JCoinEarnRules;
import com.jworks.kanjilens.data.local.JMDictDao;
import com.jworks.kanjilens.data.local.JMDictDatabase;
import com.jworks.kanjilens.data.nlp.KuromojiTokenizer;
import com.jworks.kanjilens.data.preferences.SettingsDataStore;
import com.jworks.kanjilens.data.remote.KuroshiroApi;
import com.jworks.kanjilens.data.repository.FuriganaRepositoryImpl;
import com.jworks.kanjilens.data.repository.SettingsRepositoryImpl;
import com.jworks.kanjilens.data.subscription.SubscriptionManager;
import com.jworks.kanjilens.di.AppModule_ProvideFuriganaRepositoryFactory;
import com.jworks.kanjilens.di.AppModule_ProvideKuroshiroApiFactory;
import com.jworks.kanjilens.di.AppModule_ProvideRetrofitFactory;
import com.jworks.kanjilens.di.AppModule_ProvideSettingsDataStoreFactory;
import com.jworks.kanjilens.di.AppModule_ProvideSettingsRepositoryFactory;
import com.jworks.kanjilens.di.AppModule_ProvideTextRecognizerFactory;
import com.jworks.kanjilens.di.AuthModule_ProvideAuthRepositoryFactory;
import com.jworks.kanjilens.di.AuthModule_ProvideAuthSupabaseClientFactory;
import com.jworks.kanjilens.di.AuthModule_ProvideJCoinClientFactory;
import com.jworks.kanjilens.di.AuthModule_ProvideJCoinSupabaseClientFactory;
import com.jworks.kanjilens.di.AuthModule_ProvideSubscriptionManagerFactory;
import com.jworks.kanjilens.di.DatabaseModule_ProvideDatabaseFactory;
import com.jworks.kanjilens.di.DatabaseModule_ProvideJMDictDaoFactory;
import com.jworks.kanjilens.domain.repository.FuriganaRepository;
import com.jworks.kanjilens.domain.repository.SettingsRepository;
import com.jworks.kanjilens.domain.usecases.EnrichWithFuriganaUseCase;
import com.jworks.kanjilens.domain.usecases.ProcessCameraFrameUseCase;
import com.jworks.kanjilens.ui.camera.CameraViewModel;
import com.jworks.kanjilens.ui.camera.CameraViewModel_HiltModules_KeyModule_ProvideFactory;
import com.jworks.kanjilens.ui.settings.SettingsViewModel;
import com.jworks.kanjilens.ui.settings.SettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SetBuilder;
import io.github.jan.supabase.SupabaseClient;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

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
public final class DaggerKanjiLensApplication_HiltComponents_SingletonC {
  private DaggerKanjiLensApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public KanjiLensApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements KanjiLensApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public KanjiLensApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements KanjiLensApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public KanjiLensApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements KanjiLensApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public KanjiLensApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements KanjiLensApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public KanjiLensApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements KanjiLensApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public KanjiLensApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements KanjiLensApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public KanjiLensApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements KanjiLensApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public KanjiLensApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends KanjiLensApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends KanjiLensApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends KanjiLensApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends KanjiLensApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return SetBuilder.<String>newSetBuilder(2).add(CameraViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(SettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).build();
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectAuthRepository(instance, singletonCImpl.provideAuthRepositoryProvider.get());
      MainActivity_MembersInjector.injectSubscriptionManager(instance, singletonCImpl.provideSubscriptionManagerProvider.get());
      MainActivity_MembersInjector.injectJCoinClient(instance, singletonCImpl.provideJCoinClientProvider.get());
      MainActivity_MembersInjector.injectJCoinEarnRules(instance, singletonCImpl.jCoinEarnRulesProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends KanjiLensApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CameraViewModel> cameraViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private ProcessCameraFrameUseCase processCameraFrameUseCase() {
      return new ProcessCameraFrameUseCase(singletonCImpl.provideTextRecognizerProvider.get());
    }

    private EnrichWithFuriganaUseCase enrichWithFuriganaUseCase() {
      return new EnrichWithFuriganaUseCase(singletonCImpl.provideFuriganaRepositoryProvider.get(), singletonCImpl.kuromojiTokenizerProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.cameraViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
    }

    @Override
    public Map<String, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(2).put("com.jworks.kanjilens.ui.camera.CameraViewModel", ((Provider) cameraViewModelProvider)).put("com.jworks.kanjilens.ui.settings.SettingsViewModel", ((Provider) settingsViewModelProvider)).build();
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return Collections.<String, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.jworks.kanjilens.ui.camera.CameraViewModel 
          return (T) new CameraViewModel(viewModelCImpl.processCameraFrameUseCase(), viewModelCImpl.enrichWithFuriganaUseCase(), singletonCImpl.provideSettingsRepositoryProvider.get());

          case 1: // com.jworks.kanjilens.ui.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.provideSettingsRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends KanjiLensApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends KanjiLensApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends KanjiLensApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<KuromojiTokenizer> kuromojiTokenizerProvider;

    private Provider<SupabaseClient> provideAuthSupabaseClientProvider;

    private Provider<AuthRepository> provideAuthRepositoryProvider;

    private Provider<SubscriptionManager> provideSubscriptionManagerProvider;

    private Provider<SupabaseClient> provideJCoinSupabaseClientProvider;

    private Provider<JCoinClient> provideJCoinClientProvider;

    private Provider<JCoinEarnRules> jCoinEarnRulesProvider;

    private Provider<TextRecognizer> provideTextRecognizerProvider;

    private Provider<JMDictDatabase> provideDatabaseProvider;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<KuroshiroApi> provideKuroshiroApiProvider;

    private Provider<FuriganaRepositoryImpl> furiganaRepositoryImplProvider;

    private Provider<FuriganaRepository> provideFuriganaRepositoryProvider;

    private Provider<SettingsDataStore> provideSettingsDataStoreProvider;

    private Provider<SettingsRepository> provideSettingsRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private JMDictDao jMDictDao() {
      return DatabaseModule_ProvideJMDictDaoFactory.provideJMDictDao(provideDatabaseProvider.get());
    }

    private SettingsRepositoryImpl settingsRepositoryImpl() {
      return new SettingsRepositoryImpl(provideSettingsDataStoreProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.kuromojiTokenizerProvider = DoubleCheck.provider(new SwitchingProvider<KuromojiTokenizer>(singletonCImpl, 0));
      this.provideAuthSupabaseClientProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseClient>(singletonCImpl, 2));
      this.provideAuthRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 1));
      this.provideSubscriptionManagerProvider = DoubleCheck.provider(new SwitchingProvider<SubscriptionManager>(singletonCImpl, 3));
      this.provideJCoinSupabaseClientProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseClient>(singletonCImpl, 5));
      this.provideJCoinClientProvider = DoubleCheck.provider(new SwitchingProvider<JCoinClient>(singletonCImpl, 4));
      this.jCoinEarnRulesProvider = DoubleCheck.provider(new SwitchingProvider<JCoinEarnRules>(singletonCImpl, 6));
      this.provideTextRecognizerProvider = DoubleCheck.provider(new SwitchingProvider<TextRecognizer>(singletonCImpl, 7));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<JMDictDatabase>(singletonCImpl, 10));
      this.provideRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 12));
      this.provideKuroshiroApiProvider = DoubleCheck.provider(new SwitchingProvider<KuroshiroApi>(singletonCImpl, 11));
      this.furiganaRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<FuriganaRepositoryImpl>(singletonCImpl, 9));
      this.provideFuriganaRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FuriganaRepository>(singletonCImpl, 8));
      this.provideSettingsDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<SettingsDataStore>(singletonCImpl, 14));
      this.provideSettingsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepository>(singletonCImpl, 13));
    }

    @Override
    public void injectKanjiLensApplication(KanjiLensApplication kanjiLensApplication) {
      injectKanjiLensApplication2(kanjiLensApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private KanjiLensApplication injectKanjiLensApplication2(KanjiLensApplication instance) {
      KanjiLensApplication_MembersInjector.injectKuromojiTokenizer(instance, kuromojiTokenizerProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.jworks.kanjilens.data.nlp.KuromojiTokenizer 
          return (T) new KuromojiTokenizer();

          case 1: // com.jworks.kanjilens.data.auth.AuthRepository 
          return (T) AuthModule_ProvideAuthRepositoryFactory.provideAuthRepository(singletonCImpl.provideAuthSupabaseClientProvider.get());

          case 2: // @javax.inject.Named("auth") io.github.jan.supabase.SupabaseClient 
          return (T) AuthModule_ProvideAuthSupabaseClientFactory.provideAuthSupabaseClient();

          case 3: // com.jworks.kanjilens.data.subscription.SubscriptionManager 
          return (T) AuthModule_ProvideSubscriptionManagerFactory.provideSubscriptionManager(singletonCImpl.provideAuthSupabaseClientProvider.get(), singletonCImpl.provideAuthRepositoryProvider.get());

          case 4: // com.jworks.kanjilens.data.jcoin.JCoinClient 
          return (T) AuthModule_ProvideJCoinClientFactory.provideJCoinClient(singletonCImpl.provideJCoinSupabaseClientProvider.get());

          case 5: // @javax.inject.Named("jcoin") io.github.jan.supabase.SupabaseClient 
          return (T) AuthModule_ProvideJCoinSupabaseClientFactory.provideJCoinSupabaseClient();

          case 6: // com.jworks.kanjilens.data.jcoin.JCoinEarnRules 
          return (T) new JCoinEarnRules();

          case 7: // com.google.mlkit.vision.text.TextRecognizer 
          return (T) AppModule_ProvideTextRecognizerFactory.provideTextRecognizer();

          case 8: // com.jworks.kanjilens.domain.repository.FuriganaRepository 
          return (T) AppModule_ProvideFuriganaRepositoryFactory.provideFuriganaRepository(singletonCImpl.furiganaRepositoryImplProvider.get());

          case 9: // com.jworks.kanjilens.data.repository.FuriganaRepositoryImpl 
          return (T) new FuriganaRepositoryImpl(singletonCImpl.jMDictDao(), singletonCImpl.provideKuroshiroApiProvider.get());

          case 10: // com.jworks.kanjilens.data.local.JMDictDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 11: // com.jworks.kanjilens.data.remote.KuroshiroApi 
          return (T) AppModule_ProvideKuroshiroApiFactory.provideKuroshiroApi(singletonCImpl.provideRetrofitProvider.get());

          case 12: // retrofit2.Retrofit 
          return (T) AppModule_ProvideRetrofitFactory.provideRetrofit();

          case 13: // com.jworks.kanjilens.domain.repository.SettingsRepository 
          return (T) AppModule_ProvideSettingsRepositoryFactory.provideSettingsRepository(singletonCImpl.settingsRepositoryImpl());

          case 14: // com.jworks.kanjilens.data.preferences.SettingsDataStore 
          return (T) AppModule_ProvideSettingsDataStoreFactory.provideSettingsDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
