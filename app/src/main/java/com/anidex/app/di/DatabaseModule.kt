package com.anidex.app.di

import android.content.Context
import androidx.room.Room
import com.anidex.app.data.local.AniDexDatabase
import com.anidex.app.data.local.dao.AnimeDao
import com.anidex.app.data.local.dao.WatchlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AniDexDatabase {
        return Room.databaseBuilder(
            context,
            AniDexDatabase::class.java,
            "anidex_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAnimeDao(db: AniDexDatabase): AnimeDao = db.animeDao

    @Provides
    fun provideWatchlistDao(db: AniDexDatabase): WatchlistDao = db.watchlistDao
}

