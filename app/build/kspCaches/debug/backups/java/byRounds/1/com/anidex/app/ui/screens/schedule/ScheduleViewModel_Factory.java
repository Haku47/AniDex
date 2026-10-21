package com.anidex.app.ui.screens.schedule;

import com.anidex.app.domain.usecase.GetAiringScheduleUseCase;
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
public final class ScheduleViewModel_Factory implements Factory<ScheduleViewModel> {
  private final Provider<GetAiringScheduleUseCase> getAiringScheduleUseCaseProvider;

  public ScheduleViewModel_Factory(
      Provider<GetAiringScheduleUseCase> getAiringScheduleUseCaseProvider) {
    this.getAiringScheduleUseCaseProvider = getAiringScheduleUseCaseProvider;
  }

  @Override
  public ScheduleViewModel get() {
    return newInstance(getAiringScheduleUseCaseProvider.get());
  }

  public static ScheduleViewModel_Factory create(
      Provider<GetAiringScheduleUseCase> getAiringScheduleUseCaseProvider) {
    return new ScheduleViewModel_Factory(getAiringScheduleUseCaseProvider);
  }

  public static ScheduleViewModel newInstance(GetAiringScheduleUseCase getAiringScheduleUseCase) {
    return new ScheduleViewModel(getAiringScheduleUseCase);
  }
}
