package com.anidex.app.ui.screens.stats;

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
public final class StatsViewModel_Factory implements Factory<StatsViewModel> {
  private final Provider<CalculateWatchTimeUseCase> calculateWatchTimeUseCaseProvider;

  public StatsViewModel_Factory(
      Provider<CalculateWatchTimeUseCase> calculateWatchTimeUseCaseProvider) {
    this.calculateWatchTimeUseCaseProvider = calculateWatchTimeUseCaseProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(calculateWatchTimeUseCaseProvider.get());
  }

  public static StatsViewModel_Factory create(
      Provider<CalculateWatchTimeUseCase> calculateWatchTimeUseCaseProvider) {
    return new StatsViewModel_Factory(calculateWatchTimeUseCaseProvider);
  }

  public static StatsViewModel newInstance(CalculateWatchTimeUseCase calculateWatchTimeUseCase) {
    return new StatsViewModel(calculateWatchTimeUseCase);
  }
}
