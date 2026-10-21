package com.anidex.app.domain.model

data class Anime(
    val id: Int,
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
    val format: AnimeFormat,
    val status: AnimeStatus,
    val genres: List<Genre>,
    val studios: List<Studio>,
    val broadcastDay: String?,
    val broadcastTime: String?,
    val broadcastTimezone: String?,
    val year: Int?,
    val season: String?
) {
    /**
     * Display title prioritizing English if available, fallback to default romaji title.
     */
    val displayTitle: String
        get() = englishTitle?.takeIf { it.isNotBlank() } ?: title

    /**
     * Fallback standard duration in minutes if not specified by API.
     */
    val effectiveDurationMinutes: Int
        get() = episodeDurationMinutes?.takeIf { it > 0 } ?: when (format) {
            AnimeFormat.MOVIE -> 100
            AnimeFormat.OVA, AnimeFormat.ONA, AnimeFormat.SPECIAL -> 20
            AnimeFormat.MUSIC -> 4
            AnimeFormat.TV, AnimeFormat.UNKNOWN -> 24
        }
}

