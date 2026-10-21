package com.anidex.app.core.datetime

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object DateTimeHelper {

    val JST_ZONE_ID: ZoneId = ZoneId.of("Asia/Tokyo")

    /**
     * Parse day name (e.g., "Mondays", "sunday", "Friday") to DayOfWeek.
     */
    fun parseDayOfWeek(dayName: String?): DayOfWeek? {
        if (dayName.isNullOrBlank()) return null
        val normalized = dayName.trim().lowercase().removeSuffix("s")
        return when (normalized) {
            "monday" -> DayOfWeek.MONDAY
            "tuesday" -> DayOfWeek.TUESDAY
            "wednesday" -> DayOfWeek.WEDNESDAY
            "thursday" -> DayOfWeek.THURSDAY
            "friday" -> DayOfWeek.FRIDAY
            "saturday" -> DayOfWeek.SATURDAY
            "sunday" -> DayOfWeek.SUNDAY
            else -> null
        }
    }

    /**
     * Parses time string "HH:mm" safely.
     */
    fun parseLocalTime(timeString: String?): LocalTime? {
        if (timeString.isNullOrBlank()) return null
        return runCatching {
            LocalTime.parse(timeString.trim(), DateTimeFormatter.ofPattern("HH:mm"))
        }.getOrNull()
    }

    /**
     * Calculates the target local ZonedDateTime for a broadcast scheduled in JST.
     * Accurately rolls over days across timezone boundaries (e.g., Sunday 00:30 JST -> Saturday 22:30 WIB).
     */
    fun calculateLocalAirTime(
        broadcastDay: String?,
        broadcastTimeJst: String?,
        referenceTime: ZonedDateTime = ZonedDateTime.now(JST_ZONE_ID),
        targetZoneId: ZoneId = ZoneId.systemDefault()
    ): ZonedDateTime? {
        val dayOfWeek = parseDayOfWeek(broadcastDay) ?: return null
        val time = parseLocalTime(broadcastTimeJst) ?: LocalTime.of(0, 0)

        // Find the date for this week's scheduled broadcast in JST
        val jstNow = referenceTime.withZoneSameInstant(JST_ZONE_ID)
        var targetDate = jstNow.toLocalDate().with(TemporalAdjusters.nextOrSame(dayOfWeek))
        var scheduledJst = ZonedDateTime.of(targetDate, time, JST_ZONE_ID)

        // If this week's broadcast already elapsed by more than 2 hours, project next week's
        if (scheduledJst.isBefore(jstNow.minusHours(2))) {
            targetDate = targetDate.plusWeeks(1)
            scheduledJst = ZonedDateTime.of(targetDate, time, JST_ZONE_ID)
        }

        // Convert JST scheduled time to user's local zone
        return scheduledJst.withZoneSameInstant(targetZoneId)
    }

    /**
     * Format a local ZonedDateTime to friendly string like "22:30 WIB" or "10:30 PM".
     */
    fun formatLocalTime(zonedDateTime: ZonedDateTime, locale: Locale = Locale.getDefault()): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm z", locale)
        return zonedDateTime.format(formatter)
    }

    /**
     * Calculates countdown duration from now to the target local air time.
     */
    fun calculateCountdownMillis(
        targetDateTime: ZonedDateTime,
        now: ZonedDateTime = ZonedDateTime.now(targetDateTime.zone)
    ): Long {
        val duration = Duration.between(now, targetDateTime)
        return duration.toMillis().coerceAtLeast(0L)
    }
}

