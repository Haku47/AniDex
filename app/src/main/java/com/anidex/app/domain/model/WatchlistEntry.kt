package com.anidex.app.domain.model

enum class WatchStatus(val displayName: String) {
    WATCHING("Sedang Ditonton"),
    COMPLETED("Selesai"),
    ON_HOLD("Ditunda"),
    DROPPED("Ditinggalkan"),
    PLAN_TO_WATCH("Rencana Nonton")
}

data class WatchlistEntry(
    val anime: Anime,
    val status: WatchStatus,
    val episodesWatched: Int,
    val userScore: Double?, // 0.0 to 10.0
    val lastUpdatedEpochMillis: Long,
    val notes: String = "",
    val startDateEpochMillis: Long? = null,
    val finishDateEpochMillis: Long? = null
) {
    /**
     * Percentage progress of episodes watched (0% - 100%).
     */
    val progressPercentage: Float
        get() {
            val total = anime.episodes ?: return 0f
            if (total <= 0) return 0f
            return (episodesWatched.toFloat() / total).coerceIn(0f, 1f)
        }

    /**
     * Total minutes spent on this anime based on episodes watched.
     */
    val minutesSpent: Long
        get() = episodesWatched.toLong() * anime.effectiveDurationMinutes
}

