package com.anidex.app.di;

import com.anidex.app.data.remote.api.AniListApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideAniListApiServiceFactory implements Factory<AniListApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideAniListApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public AniListApiService get() {
    return provideAniListApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideAniListApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideAniListApiServiceFactory(retrofitProvider);
  }

  public static AniListApiService provideAniListApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideAniListApiService(retrofit));
  }
}
