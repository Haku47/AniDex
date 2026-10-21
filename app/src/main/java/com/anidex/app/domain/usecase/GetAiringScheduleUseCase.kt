package com.anidex.app.domain.usecase

import com.anidex.app.core.common.DispatcherProvider
import com.anidex.app.core.common.Resource
import com.anidex.app.core.datetime.DateTimeHelper
import com.anidex.app.domain.model.AiringSchedule
import com.anidex.app.domain.repository.AnimeRepository
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class GetAiringScheduleUseCase @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val dispatchers: DispatcherProvider
) {

    /**
     * Retrieves and maps airing schedules with timezone conversion to user's device local timezone.
     *
     * @param targetLocalDay Optional filter for the user's LOCAL day of week.
     * @param targetZoneId Device's local timezone, defaulting to system default.
     */
    suspend operator fun invoke(
        targetLocalDay: DayOfWeek? = null,
        targetZoneId: ZoneId = ZoneId.systemDefault()
    ): Resource<List<AiringSchedule>> {
        return withContext(dispatchers.default) {
            val result = animeRepository.getAiringSchedules()

            when (result) {
                is Resource.Error -> Resource.Error(result.message, result.throwable)
                is Resource.Loading -> Resource.Loading
                is Resource.Idle -> Resource.Idle
                is Resource.Success -> {
                    val localNow = ZonedDateTime.now(targetZoneId)
                    val schedules = result.data.mapNotNull { anime ->
                        mapToLocalAiringSchedule(anime, localNow, targetZoneId)
                    }

                    val filtered = if (targetLocalDay != null) {
                        schedules.filter { it.localDayOfWeek == targetLocalDay }
                    } else {
                        schedules
                    }

                    // Sort by countdown time ascending (nearest airing first)
                    val sorted = filtered.sortedBy { it.countdownMillis }
                    Resource.Success(sorted)
                }
            }
        }
    }

    private fun mapToLocalAiringSchedule(
        anime: com.anidex.app.domain.model.Anime,
        localNow: ZonedDateTime,
        targetZoneId: ZoneId
    ): AiringSchedule? {
        val jstDay = anime.broadcastDay ?: return null
        val jstTime = anime.broadcastTime ?: "00:00"

        val localAirDateTime = DateTimeHelper.calculateLocalAirTime(
            broadcastDay = jstDay,
            broadcastTimeJst = jstTime,
            referenceTime = localNow,
            targetZoneId = targetZoneId
        ) ?: return null

        val localDayOfWeek = localAirDateTime.dayOfWeek
        val countdownMillis = DateTimeHelper.calculateCountdownMillis(localAirDateTime, localNow)
        val isAiringToday = localAirDateTime.toLocalDate() == localNow.toLocalDate()
        val localFormattedTime = DateTimeHelper.formatLocalTime(localAirDateTime)

        return AiringSchedule(
            anime = anime,
            originalJstDay = jstDay,
            originalJstTime = jstTime,
            localAirDateTime = localAirDateTime,
            localDayOfWeek = localDayOfWeek,
            localFormattedTime = localFormattedTime,
            countdownMillis = countdownMillis,
            isAiringToday = isAiringToday,
            nextEpisodeNumber = null // Can be populated if episode metadata is available
        )
    }
}

