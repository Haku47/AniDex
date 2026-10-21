package com.anidex.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animes")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val englishTitle: String?,
    val synopsis: String,
    val imageUrl: String,
    val bannerUrl: String?,
    val score: Double?,
    val scoredBy: Int?,
    val rank: Int?,
    val popularity: Int?,
    val episodes: Int?,
    val episodeDurationMinutes: Int?,
    val format: String,
    val status: String,
    val genresJson: String, // Stored as comma-separated or JSON string
    val studiosJson: String,
    val broadcastDay: String?,
    val broadcastTime: String?,
    val broadcastTimezone: String?,
    val year: Int?,
    val season: String?
)

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val animeId: Int,
    val status: String, // WATCHING, COMPLETED, ON_HOLD, DROPPED, PLAN_TO_WATCH
    val episodesWatched: Int,
    val userScore: Double?,
    val lastUpdatedEpochMillis: Long,
    val notes: String = "",
    val startDateEpochMillis: Long? = null,
    val finishDateEpochMillis: Long? = null
)

