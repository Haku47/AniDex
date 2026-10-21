package com.anidex.app.domain.usecase

import com.anidex.app.core.common.DispatcherProvider
import com.anidex.app.domain.model.AnimeFormat
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchTimeSummary
import com.anidex.app.domain.model.WatchlistEntry
import com.anidex.app.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CalculateWatchTimeUseCase @Inject constructor(
    private val watchlistRepository: WatchlistRepository,
    private val dispatchers: DispatcherProvider
) {

    /**
     * Observes the watchlist and continuously emits updated watch time summaries.
     */
    operator fun invoke(): Flow<WatchTimeSummary> {
        return watchlistRepository.getWatchlistStream()
            .map { calculateSummary(it) }
            .flowOn(dispatchers.default)
    }

    /**
     * Pure calculation logic for watchlist aggregation.
     */
    fun calculateSummary(entries: List<WatchlistEntry>): WatchTimeSummary {
        if (entries.isEmpty()) {
            return WatchTimeSummary(
                totalMinutes = 0L,
                totalEpisodesWatched = 0,
                animeCountByStatus = emptyMap(),
                minutesByFormat = emptyMap(),
                meanUserRating = null,
                completionRate = 0f
            )
        }

        var totalMinutes = 0L
        var totalEpisodes = 0
        val statusCountMap = mutableMapOf<WatchStatus, Int>()
        val formatMinutesMap = mutableMapOf<AnimeFormat, Long>()
        val ratings = mutableListOf<Double>()

        for (entry in entries) {
            // Validate episode watched bounds
            val anime = entry.anime
            val totalAnimeEps = anime.episodes ?: Int.MAX_VALUE
            val sanitizedEpisodes = entry.episodesWatched.coerceIn(0, totalAnimeEps)

            // Duration per episode in minutes
            val durationPerEp = anime.effectiveDurationMinutes
            val entryMinutes = sanitizedEpisodes.toLong() * durationPerEp

            totalMinutes += entryMinutes
            totalEpisodes += sanitizedEpisodes

            // Count by status
            statusCountMap[entry.status] = (statusCountMap[entry.status] ?: 0) + 1

            // Minutes by format
            formatMinutesMap[anime.format] = (formatMinutesMap[anime.format] ?: 0L) + entryMinutes

            // Ratings collection for mean score
            entry.userScore?.takeIf { it in 1.0..10.0 }?.let { ratings.add(it) }
        }

        val completedCount = statusCountMap[WatchStatus.COMPLETED] ?: 0
        val activeOrDropped = completedCount +
                (statusCountMap[WatchStatus.WATCHING] ?: 0) +
                (statusCountMap[WatchStatus.DROPPED] ?: 0)

        val completionRate = if (activeOrDropped > 0) {
            completedCount.toFloat() / activeOrDropped.toFloat()
        } else {
            0f
        }

        val meanRating = if (ratings.isNotEmpty()) {
            ratings.average()
        } else {
            null
        }

        return WatchTimeSummary(
            totalMinutes = totalMinutes,
            totalEpisodesWatched = totalEpisodes,
            animeCountByStatus = statusCountMap,
            minutesByFormat = formatMinutesMap,
            meanUserRating = meanRating,
            completionRate = completionRate
        )
    }
}

