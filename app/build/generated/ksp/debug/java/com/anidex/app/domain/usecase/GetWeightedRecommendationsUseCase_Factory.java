package com.anidex.app.domain.usecase;

import com.anidex.app.core.common.DispatcherProvider;
import com.anidex.app.domain.repository.AnimeRepository;
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
public final class GetWeightedRecommendationsUseCase_Factory implements Factory<GetWeightedRecommendationsUseCase> {
  private final Provider<WatchlistRepository> watchlistRepositoryProvider;

  private final Provider<AnimeRepository> animeRepositoryProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public GetWeightedRecommendationsUseCase_Factory(
      Provider<WatchlistRepository> watchlistRepositoryProvider,
      Provider<AnimeRepository> animeRepositoryProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.watchlistRepositoryProvider = watchlistRepositoryProvider;
    this.animeRepositoryProvider = animeRepositoryProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public GetWeightedRecommendationsUseCase get() {
    return newInstance(watchlistRepositoryProvider.get(), animeRepositoryProvider.get(), dispatchersProvider.get());
  }

  public static GetWeightedRecommendationsUseCase_Factory create(
      Provider<WatchlistRepository> watchlistRepositoryProvider,
      Provider<AnimeRepository> animeRepositoryProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new GetWeightedRecommendationsUseCase_Factory(watchlistRepositoryProvider, animeRepositoryProvider, dispatchersProvider);
  }

  public static GetWeightedRecommendationsUseCase newInstance(
      WatchlistRepository watchlistRepository, AnimeRepository animeRepository,
      DispatcherProvider dispatchers) {
    return new GetWeightedRecommendationsUseCase(watchlistRepository, animeRepository, dispatchers);
  }
}
