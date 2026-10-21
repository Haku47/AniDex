package com.anidex.app.domain.repository

import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchlistEntry
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    fun getWatchlistStream(): Flow<List<WatchlistEntry>>
    fun getWatchlistByStatusStream(status: WatchStatus): Flow<List<WatchlistEntry>>
    suspend fun getWatchlist(): List<WatchlistEntry>
    suspend fun getWatchlistEntry(animeId: Int): WatchlistEntry?
    suspend fun upsertWatchlistEntry(entry: WatchlistEntry)
    suspend fun updateEpisodeProgress(animeId: Int, episodeWatched: Int)
    suspend fun deleteWatchlistEntry(animeId: Int)
}

