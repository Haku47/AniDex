package com.anidex.app.di

import com.anidex.app.data.repository.AnimeRepositoryImpl
import com.anidex.app.data.repository.AuthRepositoryImpl
import com.anidex.app.data.repository.WatchlistRepositoryImpl
import com.anidex.app.domain.repository.AnimeRepository
import com.anidex.app.domain.repository.AuthRepository
import com.anidex.app.domain.repository.WatchlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAnimeRepository(
        impl: AnimeRepositoryImpl
    ): AnimeRepository

    @Binds
    @Singleton
    abstract fun bindWatchlistRepository(
        impl: WatchlistRepositoryImpl
    ): WatchlistRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}
