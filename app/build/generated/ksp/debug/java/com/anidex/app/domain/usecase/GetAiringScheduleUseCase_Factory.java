package com.anidex.app.domain.usecase;

import com.anidex.app.core.common.DispatcherProvider;
import com.anidex.app.domain.repository.AnimeRepository;
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
public final class GetAiringScheduleUseCase_Factory implements Factory<GetAiringScheduleUseCase> {
  private final Provider<AnimeRepository> animeRepositoryProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public GetAiringScheduleUseCase_Factory(Provider<AnimeRepository> animeRepositoryProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.animeRepositoryProvider = animeRepositoryProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public GetAiringScheduleUseCase get() {
    return newInstance(animeRepositoryProvider.get(), dispatchersProvider.get());
  }

  public static GetAiringScheduleUseCase_Factory create(
      Provider<AnimeRepository> animeRepositoryProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new GetAiringScheduleUseCase_Factory(animeRepositoryProvider, dispatchersProvider);
  }

  public static GetAiringScheduleUseCase newInstance(AnimeRepository animeRepository,
      DispatcherProvider dispatchers) {
    return new GetAiringScheduleUseCase(animeRepository, dispatchers);
  }
}
