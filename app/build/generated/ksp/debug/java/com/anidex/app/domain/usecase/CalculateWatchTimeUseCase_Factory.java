package com.anidex.app.domain.usecase;

import com.anidex.app.core.common.DispatcherProvider;
import com.anidex.app.domain.repository.WatchlistRepository;
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
public final class CalculateWatchTimeUseCase_Factory implements Factory<CalculateWatchTimeUseCase> {
  private final Provider<WatchlistRepository> watchlistRepositoryProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public CalculateWatchTimeUseCase_Factory(
      Provider<WatchlistRepository> watchlistRepositoryProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.watchlistRepositoryProvider = watchlistRepositoryProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public CalculateWatchTimeUseCase get() {
    return newInstance(watchlistRepositoryProvider.get(), dispatchersProvider.get());
  }

  public static CalculateWatchTimeUseCase_Factory create(
      Provider<WatchlistRepository> watchlistRepositoryProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new CalculateWatchTimeUseCase_Factory(watchlistRepositoryProvider, dispatchersProvider);
  }

  public static CalculateWatchTimeUseCase newInstance(WatchlistRepository watchlistRepository,
      DispatcherProvider dispatchers) {
    return new CalculateWatchTimeUseCase(watchlistRepository, dispatchers);
  }
}
