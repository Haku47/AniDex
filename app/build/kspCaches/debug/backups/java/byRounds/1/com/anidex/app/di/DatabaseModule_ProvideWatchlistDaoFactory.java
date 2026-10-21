package com.anidex.app.di;

import com.anidex.app.data.local.AniDexDatabase;
import com.anidex.app.data.local.dao.WatchlistDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideWatchlistDaoFactory implements Factory<WatchlistDao> {
  private final Provider<AniDexDatabase> dbProvider;

  public DatabaseModule_ProvideWatchlistDaoFactory(Provider<AniDexDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public WatchlistDao get() {
    return provideWatchlistDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideWatchlistDaoFactory create(
      Provider<AniDexDatabase> dbProvider) {
    return new DatabaseModule_ProvideWatchlistDaoFactory(dbProvider);
  }

  public static WatchlistDao provideWatchlistDao(AniDexDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideWatchlistDao(db));
  }
}
