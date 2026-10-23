package com.anidex.app.domain.model

import java.time.DayOfWeek
import java.time.ZonedDateTime

data class AiringSchedule(
    val anime: Anime,
    val originalJstDay: String,
    val originalJstTime: String,
    val localAirDateTime: ZonedDateTime,
    val localDayOfWeek: DayOfWeek,
    val localFormattedTime: String, // e.g. "23:00 WIB"
    val countdownMillis: Long,
    val isAiringToday: Boolean,
    val nextEpisodeNumber: Int?
) {
    val countdownFormatted: String
        get() {
            if (countdownMillis <= 0) return "Aired / Soon"
            val totalSeconds = countdownMillis / 1000
            val days = totalSeconds / (24 * 3600)
            val hours = (totalSeconds % (24 * 3600)) / 3600
            val minutes = (totalSeconds % 3600) / 60

            return buildString {
                if (days > 0) append("${days}d ")
                if (hours > 0 || days > 0) append("${hours}h ")
                append("${minutes}m left")
            }
        }
}

