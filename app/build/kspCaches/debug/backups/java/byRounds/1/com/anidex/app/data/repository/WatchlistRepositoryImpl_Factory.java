package com.anidex.app.data.repository;

import com.anidex.app.core.common.DispatcherProvider;
import com.anidex.app.data.local.dao.AnimeDao;
import com.anidex.app.data.local.dao.WatchlistDao;
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
public final class WatchlistRepositoryImpl_Factory implements Factory<WatchlistRepositoryImpl> {
  private final Provider<WatchlistDao> watchlistDaoProvider;

  private final Provider<AnimeDao> animeDaoProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public WatchlistRepositoryImpl_Factory(Provider<WatchlistDao> watchlistDaoProvider,
      Provider<AnimeDao> animeDaoProvider, Provider<DispatcherProvider> dispatchersProvider) {
    this.watchlistDaoProvider = watchlistDaoProvider;
    this.animeDaoProvider = animeDaoProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public WatchlistRepositoryImpl get() {
    return newInstance(watchlistDaoProvider.get(), animeDaoProvider.get(), dispatchersProvider.get());
  }

  public static WatchlistRepositoryImpl_Factory create(Provider<WatchlistDao> watchlistDaoProvider,
      Provider<AnimeDao> animeDaoProvider, Provider<DispatcherProvider> dispatchersProvider) {
    return new WatchlistRepositoryImpl_Factory(watchlistDaoProvider, animeDaoProvider, dispatchersProvider);
  }

  public static WatchlistRepositoryImpl newInstance(WatchlistDao watchlistDao, AnimeDao animeDao,
      DispatcherProvider dispatchers) {
    return new WatchlistRepositoryImpl(watchlistDao, animeDao, dispatchers);
  }
}
