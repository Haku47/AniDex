package com.anidex.app.di;

import com.anidex.app.data.remote.api.JikanApiService;
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
public final class NetworkModule_ProvideJikanApiServiceFactory implements Factory<JikanApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideJikanApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public JikanApiService get() {
    return provideJikanApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideJikanApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideJikanApiServiceFactory(retrofitProvider);
  }

  public static JikanApiService provideJikanApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideJikanApiService(retrofit));
  }
}
