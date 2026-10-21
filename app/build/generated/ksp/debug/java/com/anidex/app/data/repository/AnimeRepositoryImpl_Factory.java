package com.anidex.app.data.repository;

import com.anidex.app.core.common.DispatcherProvider;
import com.anidex.app.data.local.dao.AnimeDao;
import com.anidex.app.data.remote.api.KitsuApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
    "KotlinInternalInJava",
    "cast"
})
public final class AnimeRepositoryImpl_Factory implements Factory<AnimeRepositoryImpl> {
  private final Provider<KitsuApiService> apiProvider;

  private final Provider<AnimeDao> animeDaoProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public AnimeRepositoryImpl_Factory(Provider<KitsuApiService> apiProvider,
      Provider<AnimeDao> animeDaoProvider, Provider<DispatcherProvider> dispatchersProvider) {
    this.apiProvider = apiProvider;
    this.animeDaoProvider = animeDaoProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public AnimeRepositoryImpl get() {
    return newInstance(apiProvider.get(), animeDaoProvider.get(), dispatchersProvider.get());
  }

  public static AnimeRepositoryImpl_Factory create(Provider<KitsuApiService> apiProvider,
      Provider<AnimeDao> animeDaoProvider, Provider<DispatcherProvider> dispatchersProvider) {
    return new AnimeRepositoryImpl_Factory(apiProvider, animeDaoProvider, dispatchersProvider);
  }

  public static AnimeRepositoryImpl newInstance(KitsuApiService api, AnimeDao animeDao,
      DispatcherProvider dispatchers) {
    return new AnimeRepositoryImpl(api, animeDao, dispatchers);
  }
}
