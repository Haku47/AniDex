package com.anidex.app.ui.screens.profile;

import com.anidex.app.domain.repository.AuthRepository;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<CalculateWatchTimeUseCase> calculateWatchTimeUseCaseProvider;

  public ProfileViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<CalculateWatchTimeUseCase> calculateWatchTimeUseCaseProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.calculateWatchTimeUseCaseProvider = calculateWatchTimeUseCaseProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(authRepositoryProvider.get(), calculateWatchTimeUseCaseProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<CalculateWatchTimeUseCase> calculateWatchTimeUseCaseProvider) {
    return new ProfileViewModel_Factory(authRepositoryProvider, calculateWatchTimeUseCaseProvider);
  }

  public static ProfileViewModel newInstance(AuthRepository authRepository,
      CalculateWatchTimeUseCase calculateWatchTimeUseCase) {
    return new ProfileViewModel(authRepository, calculateWatchTimeUseCase);
  }
}
