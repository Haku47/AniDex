package com.anidex.app.ui.screens.watchlist;

import com.anidex.app.domain.repository.WatchlistRepository;
import com.anidex.app.domain.usecase.CalculateWatchTimeUseCase;
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
public final class WatchlistViewModel_Factory implements Factory<WatchlistViewModel> {
  private final Provider<WatchlistRepository> watchlistRepositoryProvider;

  private final Provider<CalculateWatchTimeUseCase> calculateWatchTimeUseCaseProvider;

  public WatchlistViewModel_Factory(Provider<WatchlistRepository> watchlistRepositoryProvider,
      Provider<CalculateWatchTimeUseCase> calculateWatchTimeUseCaseProvider) {
    this.watchlistRepositoryProvider = watchlistRepositoryProvider;
    this.calculateWatchTimeUseCaseProvider = calculateWatchTimeUseCaseProvider;
  }

  @Override
  public WatchlistViewModel get() {
    return newInstance(watchlistRepositoryProvider.get(), calculateWatchTimeUseCaseProvider.get());
  }

  public static WatchlistViewModel_Factory create(
      Provider<WatchlistRepository> watchlistRepositoryProvider,
      Provider<CalculateWatchTimeUseCase> calculateWatchTimeUseCaseProvider) {
    return new WatchlistViewModel_Factory(watchlistRepositoryProvider, calculateWatchTimeUseCaseProvider);
  }

  public static WatchlistViewModel newInstance(WatchlistRepository watchlistRepository,
      CalculateWatchTimeUseCase calculateWatchTimeUseCase) {
    return new WatchlistViewModel(watchlistRepository, calculateWatchTimeUseCase);
  }
}
