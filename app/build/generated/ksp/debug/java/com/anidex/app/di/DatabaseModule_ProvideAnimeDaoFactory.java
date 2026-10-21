package com.anidex.app.di;

import com.anidex.app.data.local.AniDexDatabase;
import com.anidex.app.data.local.dao.AnimeDao;
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
public final class DatabaseModule_ProvideAnimeDaoFactory implements Factory<AnimeDao> {
  private final Provider<AniDexDatabase> dbProvider;

  public DatabaseModule_ProvideAnimeDaoFactory(Provider<AniDexDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AnimeDao get() {
    return provideAnimeDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAnimeDaoFactory create(Provider<AniDexDatabase> dbProvider) {
    return new DatabaseModule_ProvideAnimeDaoFactory(dbProvider);
  }

  public static AnimeDao provideAnimeDao(AniDexDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAnimeDao(db));
  }
}
