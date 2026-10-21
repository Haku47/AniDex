package com.anidex.app.di;

import com.anidex.app.data.remote.api.KitsuApiService;
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
public final class NetworkModule_ProvideKitsuApiServiceFactory implements Factory<KitsuApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideKitsuApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public KitsuApiService get() {
    return provideKitsuApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideKitsuApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideKitsuApiServiceFactory(retrofitProvider);
  }

  public static KitsuApiService provideKitsuApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideKitsuApiService(retrofit));
  }
}
