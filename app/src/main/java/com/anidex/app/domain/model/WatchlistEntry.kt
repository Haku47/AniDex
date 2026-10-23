package com.anidex.app.domain.model

import androidx.annotation.StringRes
import com.anidex.app.R

enum class WatchStatus(
    val displayName: String,
    @StringRes val titleRes: Int
) {
    WATCHING("Watching", R.string.status_watching),
    COMPLETED("Completed", R.string.status_completed),
    ON_HOLD("On Hold", R.string.status_hold),
    DROPPED("Dropped", R.string.status_dropped),
    PLAN_TO_WATCH("Plan to Watch", R.string.status_plan)
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

