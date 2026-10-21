package com.anidex.app.domain.model

data class WatchTimeSummary(
    val totalMinutes: Long,
    val totalEpisodesWatched: Int,
    val animeCountByStatus: Map<WatchStatus, Int>,
    val minutesByFormat: Map<AnimeFormat, Long>,
    val meanUserRating: Double?,
    val completionRate: Float // Completed titles / (Completed + Watching + Dropped)
) {
    val days: Long get() = totalMinutes / (24 * 60)
    val hours: Long get() = (totalMinutes % (24 * 60)) / 60
    val minutes: Long get() = totalMinutes % 60

    /**
     * Human readable localized watch time breakdown.
     * E.g. "5 Hari, 14 Jam, 30 Menit"
     */
    val formattedDisplayString: String
        get() {
            val parts = mutableListOf<String>()
            if (days > 0) parts.add("$days Hari")
            if (hours > 0) parts.add("$hours Jam")
            if (minutes > 0 || parts.isEmpty()) parts.add("$minutes Menit")
            return parts.joinToString(", ")
        }

    val totalHoursPrecise: Double
        get() = totalMinutes / 60.0
}

