package com.anidex.app.data.repository

import com.anidex.app.core.common.DispatcherProvider
import com.anidex.app.data.local.dao.AnimeDao
import com.anidex.app.data.local.dao.WatchlistDao
import com.anidex.app.data.mapper.AnimeMapper.toDomain
import com.anidex.app.data.mapper.AnimeMapper.toEntity
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchlistEntry
import com.anidex.app.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WatchlistRepositoryImpl @Inject constructor(
    private val watchlistDao: WatchlistDao,
    private val animeDao: AnimeDao,
    private val dispatchers: DispatcherProvider
) : WatchlistRepository {

    override fun getWatchlistStream(): Flow<List<WatchlistEntry>> {
        return watchlistDao.observeAllWithAnime()
            .map { list ->
                list.map { item ->
                    val domainAnime = item.anime.toDomain()
                    item.watchlist.toDomain(domainAnime)
                }
            }
            .flowOn(dispatchers.io)
    }

    override fun getWatchlistByStatusStream(status: WatchStatus): Flow<List<WatchlistEntry>> {
        return watchlistDao.observeByStatusWithAnime(status.name)
            .map { list ->
                list.map { item ->
                    val domainAnime = item.anime.toDomain()
                    item.watchlist.toDomain(domainAnime)
                }
            }
            .flowOn(dispatchers.io)
    }

    override suspend fun getWatchlist(): List<WatchlistEntry> {
        return withContext(dispatchers.io) {
            watchlistDao.getAllWithAnime().map { item ->
                item.watchlist.toDomain(item.anime.toDomain())
            }
        }
    }

    override suspend fun getWatchlistEntry(animeId: Int): WatchlistEntry? {
        return withContext(dispatchers.io) {
            watchlistDao.getEntryWithAnime(animeId)?.let {
                it.watchlist.toDomain(it.anime.toDomain())
            }
        }
    }

    override suspend fun upsertWatchlistEntry(entry: WatchlistEntry) {
        withContext(dispatchers.io) {
            animeDao.insertAnime(entry.anime.toEntity())
            watchlistDao.upsertWatchlist(entry.toEntity())
        }
    }

    override suspend fun updateEpisodeProgress(animeId: Int, episodeWatched: Int) {
        withContext(dispatchers.io) {
            watchlistDao.updateEpisodeCount(animeId, episodeWatched)
        }
    }

    override suspend fun deleteWatchlistEntry(animeId: Int) {
        withContext(dispatchers.io) {
            watchlistDao.deleteEntry(animeId)
        }
    }
}

