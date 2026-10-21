package com.anidex.app.domain.model

data class RecommendedAnime(
    val anime: Anime,
    val matchScore: Double,
    val matchPercentage: Int, // e.g. 94%
    val matchedGenres: List<String>,
    val matchedStudios: List<String>,
    val reasonDescription: String
)

