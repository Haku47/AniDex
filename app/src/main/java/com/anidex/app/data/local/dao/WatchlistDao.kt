package com.anidex.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anidex.app.data.local.entity.AnimeEntity
import com.anidex.app.data.local.entity.WatchlistEntity
import kotlinx.coroutines.flow.Flow

data class WatchlistWithAnimeEntity(
    @androidx.room.Embedded val watchlist: WatchlistEntity,
    @androidx.room.Relation(
        parentColumn = "animeId",
        entityColumn = "id"
    )
    val anime: AnimeEntity
)

@Dao
interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimes(animes: List<AnimeEntity>)

    @Query("SELECT * FROM animes WHERE id = :id")
    suspend fun getAnimeById(id: Int): AnimeEntity?
}

@Dao
interface WatchlistDao {
    @androidx.room.Transaction
    @Query("SELECT * FROM watchlist ORDER BY lastUpdatedEpochMillis DESC")
    fun observeAllWithAnime(): Flow<List<WatchlistWithAnimeEntity>>

    @androidx.room.Transaction
    @Query("SELECT * FROM watchlist WHERE status = :status ORDER BY lastUpdatedEpochMillis DESC")
    fun observeByStatusWithAnime(status: String): Flow<List<WatchlistWithAnimeEntity>>

    @androidx.room.Transaction
    @Query("SELECT * FROM watchlist")
    suspend fun getAllWithAnime(): List<WatchlistWithAnimeEntity>

    @androidx.room.Transaction
    @Query("SELECT * FROM watchlist WHERE animeId = :animeId")
    suspend fun getEntryWithAnime(animeId: Int): WatchlistWithAnimeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWatchlist(entry: WatchlistEntity)

    @Query("UPDATE watchlist SET episodesWatched = :episodes, lastUpdatedEpochMillis = :updatedAt WHERE animeId = :animeId")
    suspend fun updateEpisodeCount(animeId: Int, episodes: Int, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM watchlist WHERE animeId = :animeId")
    suspend fun deleteEntry(animeId: Int)
}

