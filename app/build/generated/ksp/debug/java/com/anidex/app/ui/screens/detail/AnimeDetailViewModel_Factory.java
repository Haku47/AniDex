package com.anidex.app.ui.screens.detail;

import androidx.lifecycle.SavedStateHandle;
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
public final class AnimeDetailViewModel_Factory implements Factory<AnimeDetailViewModel> {
  private final Provider<AnimeRepository> animeRepositoryProvider;

  private final Provider<WatchlistRepository> watchlistRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AnimeDetailViewModel_Factory(Provider<AnimeRepository> animeRepositoryProvider,
      Provider<WatchlistRepository> watchlistRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.animeRepositoryProvider = animeRepositoryProvider;
    this.watchlistRepositoryProvider = watchlistRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AnimeDetailViewModel get() {
    return newInstance(animeRepositoryProvider.get(), watchlistRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static AnimeDetailViewModel_Factory create(
      Provider<AnimeRepository> animeRepositoryProvider,
      Provider<WatchlistRepository> watchlistRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AnimeDetailViewModel_Factory(animeRepositoryProvider, watchlistRepositoryProvider, savedStateHandleProvider);
  }

  public static AnimeDetailViewModel newInstance(AnimeRepository animeRepository,
      WatchlistRepository watchlistRepository, SavedStateHandle savedStateHandle) {
    return new AnimeDetailViewModel(animeRepository, watchlistRepository, savedStateHandle);
  }
}
