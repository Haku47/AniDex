package com.anidex.app.ui.screens.discover;

import com.anidex.app.domain.repository.AnimeRepository;
import com.anidex.app.domain.repository.WatchlistRepository;
import com.anidex.app.domain.usecase.GetWeightedRecommendationsUseCase;
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
public final class DiscoverViewModel_Factory implements Factory<DiscoverViewModel> {
  private final Provider<AnimeRepository> animeRepositoryProvider;

  private final Provider<WatchlistRepository> watchlistRepositoryProvider;

  private final Provider<GetWeightedRecommendationsUseCase> getWeightedRecommendationsUseCaseProvider;

  public DiscoverViewModel_Factory(Provider<AnimeRepository> animeRepositoryProvider,
      Provider<WatchlistRepository> watchlistRepositoryProvider,
      Provider<GetWeightedRecommendationsUseCase> getWeightedRecommendationsUseCaseProvider) {
    this.animeRepositoryProvider = animeRepositoryProvider;
    this.watchlistRepositoryProvider = watchlistRepositoryProvider;
    this.getWeightedRecommendationsUseCaseProvider = getWeightedRecommendationsUseCaseProvider;
  }

  @Override
  public DiscoverViewModel get() {
    return newInstance(animeRepositoryProvider.get(), watchlistRepositoryProvider.get(), getWeightedRecommendationsUseCaseProvider.get());
  }

  public static DiscoverViewModel_Factory create(Provider<AnimeRepository> animeRepositoryProvider,
      Provider<WatchlistRepository> watchlistRepositoryProvider,
      Provider<GetWeightedRecommendationsUseCase> getWeightedRecommendationsUseCaseProvider) {
    return new DiscoverViewModel_Factory(animeRepositoryProvider, watchlistRepositoryProvider, getWeightedRecommendationsUseCaseProvider);
  }

  public static DiscoverViewModel newInstance(AnimeRepository animeRepository,
      WatchlistRepository watchlistRepository,
      GetWeightedRecommendationsUseCase getWeightedRecommendationsUseCase) {
    return new DiscoverViewModel(animeRepository, watchlistRepository, getWeightedRecommendationsUseCase);
  }
}
