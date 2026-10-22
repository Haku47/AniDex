package com.anidex.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.anidex.app.core.common.DispatcherProvider;
import com.anidex.app.data.local.AniDexDatabase;
import com.anidex.app.data.local.dao.AnimeDao;
import com.anidex.app.data.local.dao.WatchlistDao;
import com.anidex.app.data.remote.api.KitsuApiService;
import com.anidex.app.data.repository.AnimeRepositoryImpl;
import com.anidex.app.data.repository.AuthRepositoryImpl;
import com.anidex.app.data.repository.WatchlistRepositoryImpl;
import com.anidex.app.di.DatabaseModule_ProvideAnimeDaoFactory;
import com.anidex.app.di.DatabaseModule_ProvideDatabaseFactory;
import com.anidex.app.di.DatabaseModule_ProvideWatchlistDaoFactory;
import com.anidex.app.di.DispatchersModule_ProvideDispatcherProviderFactory;
import com.anidex.app.di.NetworkModule_ProvideHeaderInterceptorFactory;
import com.anidex.app.di.NetworkModule_ProvideJsonFactory;
import com.anidex.app.di.NetworkModule_ProvideKitsuApiServiceFactory;
import com.anidex.app.di.NetworkModule_ProvideLoggingInterceptorFactory;
import com.anidex.app.di.NetworkModule_ProvideOkHttpClientFactory;
import com.anidex.app.di.NetworkModule_ProvideRetrofitFactory;
import com.anidex.app.domain.repository.AnimeRepository;
import com.anidex.app.domain.repository.WatchlistRepository;
import com.anidex.app.domain.usecase.CalculateWatchTimeUseCase;
import com.anidex.app.domain.usecase.GetAiringScheduleUseCase;
import com.anidex.app.domain.usecase.GetWeightedRecommendationsUseCase;
import com.anidex.app.ui.MainActivity;
import com.anidex.app.ui.MainActivity_MembersInjector;
import com.anidex.app.ui.screens.auth.AuthViewModel;
import com.anidex.app.ui.screens.auth.AuthViewModel_HiltModules;
import com.anidex.app.ui.screens.detail.AnimeDetailViewModel;
import com.anidex.app.ui.screens.detail.AnimeDetailViewModel_HiltModules;
import com.anidex.app.ui.screens.discover.DiscoverViewModel;
import com.anidex.app.ui.screens.discover.DiscoverViewModel_HiltModules;
import com.anidex.app.ui.screens.profile.ProfileViewModel;
import com.anidex.app.ui.screens.profile.ProfileViewModel_HiltModules;
import com.anidex.app.ui.screens.schedule.ScheduleViewModel;
import com.anidex.app.ui.screens.schedule.ScheduleViewModel_HiltModules;
import com.anidex.app.ui.screens.stats.StatsViewModel;
import com.anidex.app.ui.screens.stats.StatsViewModel_HiltModules;
import com.anidex.app.ui.screens.watchlist.WatchlistViewModel;
import com.anidex.app.ui.screens.watchlist.WatchlistViewModel_HiltModules;
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
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import kotlinx.serialization.json.Json;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    "KotlinInternalInJava",
    "cast"
})
public final class DaggerAniDexApplication_HiltComponents_SingletonC {
  private DaggerAniDexApplication_HiltComponents_SingletonC() {
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

    public AniDexApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements AniDexApplication_HiltComponents.ActivityRetainedC.Builder {
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
    public AniDexApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements AniDexApplication_HiltComponents.ActivityC.Builder {
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
    public AniDexApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements AniDexApplication_HiltComponents.FragmentC.Builder {
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
    public AniDexApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements AniDexApplication_HiltComponents.ViewWithFragmentC.Builder {
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
    public AniDexApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements AniDexApplication_HiltComponents.ViewC.Builder {
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
    public AniDexApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements AniDexApplication_HiltComponents.ViewModelC.Builder {
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
    public AniDexApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements AniDexApplication_HiltComponents.ServiceC.Builder {
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
    public AniDexApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends AniDexApplication_HiltComponents.ViewWithFragmentC {
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

  private static final class FragmentCImpl extends AniDexApplication_HiltComponents.FragmentC {
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

  private static final class ViewCImpl extends AniDexApplication_HiltComponents.ViewC {
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

  private static final class ActivityCImpl extends AniDexApplication_HiltComponents.ActivityC {
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
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(7).put(LazyClassKeyProvider.com_anidex_app_ui_screens_detail_AnimeDetailViewModel, AnimeDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_anidex_app_ui_screens_auth_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_anidex_app_ui_screens_discover_DiscoverViewModel, DiscoverViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_anidex_app_ui_screens_profile_ProfileViewModel, ProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_anidex_app_ui_screens_schedule_ScheduleViewModel, ScheduleViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_anidex_app_ui_screens_stats_StatsViewModel, StatsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_anidex_app_ui_screens_watchlist_WatchlistViewModel, WatchlistViewModel_HiltModules.KeyModule.provide()).build());
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

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectAuthRepository(instance, singletonCImpl.authRepositoryImplProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_anidex_app_ui_screens_stats_StatsViewModel = "com.anidex.app.ui.screens.stats.StatsViewModel";

      static String com_anidex_app_ui_screens_schedule_ScheduleViewModel = "com.anidex.app.ui.screens.schedule.ScheduleViewModel";

      static String com_anidex_app_ui_screens_auth_AuthViewModel = "com.anidex.app.ui.screens.auth.AuthViewModel";

      static String com_anidex_app_ui_screens_discover_DiscoverViewModel = "com.anidex.app.ui.screens.discover.DiscoverViewModel";

      static String com_anidex_app_ui_screens_profile_ProfileViewModel = "com.anidex.app.ui.screens.profile.ProfileViewModel";

      static String com_anidex_app_ui_screens_detail_AnimeDetailViewModel = "com.anidex.app.ui.screens.detail.AnimeDetailViewModel";

      static String com_anidex_app_ui_screens_watchlist_WatchlistViewModel = "com.anidex.app.ui.screens.watchlist.WatchlistViewModel";

      @KeepFieldType
      StatsViewModel com_anidex_app_ui_screens_stats_StatsViewModel2;

      @KeepFieldType
      ScheduleViewModel com_anidex_app_ui_screens_schedule_ScheduleViewModel2;

      @KeepFieldType
      AuthViewModel com_anidex_app_ui_screens_auth_AuthViewModel2;

      @KeepFieldType
      DiscoverViewModel com_anidex_app_ui_screens_discover_DiscoverViewModel2;

      @KeepFieldType
      ProfileViewModel com_anidex_app_ui_screens_profile_ProfileViewModel2;

      @KeepFieldType
      AnimeDetailViewModel com_anidex_app_ui_screens_detail_AnimeDetailViewModel2;

      @KeepFieldType
      WatchlistViewModel com_anidex_app_ui_screens_watchlist_WatchlistViewModel2;
    }
  }

  private static final class ViewModelCImpl extends AniDexApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AnimeDetailViewModel> animeDetailViewModelProvider;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<DiscoverViewModel> discoverViewModelProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private Provider<ScheduleViewModel> scheduleViewModelProvider;

    private Provider<StatsViewModel> statsViewModelProvider;

    private Provider<WatchlistViewModel> watchlistViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GetWeightedRecommendationsUseCase getWeightedRecommendationsUseCase() {
      return new GetWeightedRecommendationsUseCase(singletonCImpl.bindWatchlistRepositoryProvider.get(), singletonCImpl.bindAnimeRepositoryProvider.get(), singletonCImpl.provideDispatcherProvider.get());
    }

    private CalculateWatchTimeUseCase calculateWatchTimeUseCase() {
      return new CalculateWatchTimeUseCase(singletonCImpl.bindWatchlistRepositoryProvider.get(), singletonCImpl.provideDispatcherProvider.get());
    }

    private GetAiringScheduleUseCase getAiringScheduleUseCase() {
      return new GetAiringScheduleUseCase(singletonCImpl.bindAnimeRepositoryProvider.get(), singletonCImpl.provideDispatcherProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.animeDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.discoverViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.scheduleViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.statsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.watchlistViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(7).put(LazyClassKeyProvider.com_anidex_app_ui_screens_detail_AnimeDetailViewModel, ((Provider) animeDetailViewModelProvider)).put(LazyClassKeyProvider.com_anidex_app_ui_screens_auth_AuthViewModel, ((Provider) authViewModelProvider)).put(LazyClassKeyProvider.com_anidex_app_ui_screens_discover_DiscoverViewModel, ((Provider) discoverViewModelProvider)).put(LazyClassKeyProvider.com_anidex_app_ui_screens_profile_ProfileViewModel, ((Provider) profileViewModelProvider)).put(LazyClassKeyProvider.com_anidex_app_ui_screens_schedule_ScheduleViewModel, ((Provider) scheduleViewModelProvider)).put(LazyClassKeyProvider.com_anidex_app_ui_screens_stats_StatsViewModel, ((Provider) statsViewModelProvider)).put(LazyClassKeyProvider.com_anidex_app_ui_screens_watchlist_WatchlistViewModel, ((Provider) watchlistViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_anidex_app_ui_screens_auth_AuthViewModel = "com.anidex.app.ui.screens.auth.AuthViewModel";

      static String com_anidex_app_ui_screens_schedule_ScheduleViewModel = "com.anidex.app.ui.screens.schedule.ScheduleViewModel";

      static String com_anidex_app_ui_screens_stats_StatsViewModel = "com.anidex.app.ui.screens.stats.StatsViewModel";

      static String com_anidex_app_ui_screens_profile_ProfileViewModel = "com.anidex.app.ui.screens.profile.ProfileViewModel";

      static String com_anidex_app_ui_screens_watchlist_WatchlistViewModel = "com.anidex.app.ui.screens.watchlist.WatchlistViewModel";

      static String com_anidex_app_ui_screens_discover_DiscoverViewModel = "com.anidex.app.ui.screens.discover.DiscoverViewModel";

      static String com_anidex_app_ui_screens_detail_AnimeDetailViewModel = "com.anidex.app.ui.screens.detail.AnimeDetailViewModel";

      @KeepFieldType
      AuthViewModel com_anidex_app_ui_screens_auth_AuthViewModel2;

      @KeepFieldType
      ScheduleViewModel com_anidex_app_ui_screens_schedule_ScheduleViewModel2;

      @KeepFieldType
      StatsViewModel com_anidex_app_ui_screens_stats_StatsViewModel2;

      @KeepFieldType
      ProfileViewModel com_anidex_app_ui_screens_profile_ProfileViewModel2;

      @KeepFieldType
      WatchlistViewModel com_anidex_app_ui_screens_watchlist_WatchlistViewModel2;

      @KeepFieldType
      DiscoverViewModel com_anidex_app_ui_screens_discover_DiscoverViewModel2;

      @KeepFieldType
      AnimeDetailViewModel com_anidex_app_ui_screens_detail_AnimeDetailViewModel2;
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
          case 0: // com.anidex.app.ui.screens.detail.AnimeDetailViewModel 
          return (T) new AnimeDetailViewModel(singletonCImpl.bindAnimeRepositoryProvider.get(), singletonCImpl.bindWatchlistRepositoryProvider.get(), viewModelCImpl.savedStateHandle);

          case 1: // com.anidex.app.ui.screens.auth.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryImplProvider.get());

          case 2: // com.anidex.app.ui.screens.discover.DiscoverViewModel 
          return (T) new DiscoverViewModel(singletonCImpl.bindAnimeRepositoryProvider.get(), singletonCImpl.bindWatchlistRepositoryProvider.get(), viewModelCImpl.getWeightedRecommendationsUseCase());

          case 3: // com.anidex.app.ui.screens.profile.ProfileViewModel 
          return (T) new ProfileViewModel(singletonCImpl.authRepositoryImplProvider.get(), viewModelCImpl.calculateWatchTimeUseCase());

          case 4: // com.anidex.app.ui.screens.schedule.ScheduleViewModel 
          return (T) new ScheduleViewModel(viewModelCImpl.getAiringScheduleUseCase());

          case 5: // com.anidex.app.ui.screens.stats.StatsViewModel 
          return (T) new StatsViewModel(viewModelCImpl.calculateWatchTimeUseCase());

          case 6: // com.anidex.app.ui.screens.watchlist.WatchlistViewModel 
          return (T) new WatchlistViewModel(singletonCImpl.bindWatchlistRepositoryProvider.get(), viewModelCImpl.calculateWatchTimeUseCase());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends AniDexApplication_HiltComponents.ActivityRetainedC {
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

  private static final class ServiceCImpl extends AniDexApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends AniDexApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AuthRepositoryImpl> authRepositoryImplProvider;

    private Provider<HttpLoggingInterceptor> provideLoggingInterceptorProvider;

    private Provider<Interceptor> provideHeaderInterceptorProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<Json> provideJsonProvider;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<KitsuApiService> provideKitsuApiServiceProvider;

    private Provider<AniDexDatabase> provideDatabaseProvider;

    private Provider<DispatcherProvider> provideDispatcherProvider;

    private Provider<AnimeRepositoryImpl> animeRepositoryImplProvider;

    private Provider<AnimeRepository> bindAnimeRepositoryProvider;

    private Provider<WatchlistRepositoryImpl> watchlistRepositoryImplProvider;

    private Provider<WatchlistRepository> bindWatchlistRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private AnimeDao animeDao() {
      return DatabaseModule_ProvideAnimeDaoFactory.provideAnimeDao(provideDatabaseProvider.get());
    }

    private WatchlistDao watchlistDao() {
      return DatabaseModule_ProvideWatchlistDaoFactory.provideWatchlistDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.authRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepositoryImpl>(singletonCImpl, 0));
      this.provideLoggingInterceptorProvider = DoubleCheck.provider(new SwitchingProvider<HttpLoggingInterceptor>(singletonCImpl, 5));
      this.provideHeaderInterceptorProvider = DoubleCheck.provider(new SwitchingProvider<Interceptor>(singletonCImpl, 6));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 4));
      this.provideJsonProvider = DoubleCheck.provider(new SwitchingProvider<Json>(singletonCImpl, 7));
      this.provideRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 3));
      this.provideKitsuApiServiceProvider = DoubleCheck.provider(new SwitchingProvider<KitsuApiService>(singletonCImpl, 2));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AniDexDatabase>(singletonCImpl, 8));
      this.provideDispatcherProvider = DoubleCheck.provider(new SwitchingProvider<DispatcherProvider>(singletonCImpl, 9));
      this.animeRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 1);
      this.bindAnimeRepositoryProvider = DoubleCheck.provider((Provider) animeRepositoryImplProvider);
      this.watchlistRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 10);
      this.bindWatchlistRepositoryProvider = DoubleCheck.provider((Provider) watchlistRepositoryImplProvider);
    }

    @Override
    public void injectAniDexApplication(AniDexApplication aniDexApplication) {
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
          case 0: // com.anidex.app.data.repository.AuthRepositoryImpl 
          return (T) new AuthRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.anidex.app.data.repository.AnimeRepositoryImpl 
          return (T) new AnimeRepositoryImpl(singletonCImpl.provideKitsuApiServiceProvider.get(), singletonCImpl.animeDao(), singletonCImpl.provideDispatcherProvider.get());

          case 2: // com.anidex.app.data.remote.api.KitsuApiService 
          return (T) NetworkModule_ProvideKitsuApiServiceFactory.provideKitsuApiService(singletonCImpl.provideRetrofitProvider.get());

          case 3: // retrofit2.Retrofit 
          return (T) NetworkModule_ProvideRetrofitFactory.provideRetrofit(singletonCImpl.provideOkHttpClientProvider.get(), singletonCImpl.provideJsonProvider.get());

          case 4: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient(singletonCImpl.provideLoggingInterceptorProvider.get(), singletonCImpl.provideHeaderInterceptorProvider.get());

          case 5: // okhttp3.logging.HttpLoggingInterceptor 
          return (T) NetworkModule_ProvideLoggingInterceptorFactory.provideLoggingInterceptor();

          case 6: // okhttp3.Interceptor 
          return (T) NetworkModule_ProvideHeaderInterceptorFactory.provideHeaderInterceptor();

          case 7: // kotlinx.serialization.json.Json 
          return (T) NetworkModule_ProvideJsonFactory.provideJson();

          case 8: // com.anidex.app.data.local.AniDexDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.anidex.app.core.common.DispatcherProvider 
          return (T) DispatchersModule_ProvideDispatcherProviderFactory.provideDispatcherProvider();

          case 10: // com.anidex.app.data.repository.WatchlistRepositoryImpl 
          return (T) new WatchlistRepositoryImpl(singletonCImpl.watchlistDao(), singletonCImpl.animeDao(), singletonCImpl.provideDispatcherProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
