package com.anidex.app.domain.usecase

import com.anidex.app.core.common.DispatcherProvider
import com.anidex.app.core.common.Resource
import com.anidex.app.domain.model.Anime
import com.anidex.app.domain.model.RecommendedAnime
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchlistEntry
import com.anidex.app.domain.repository.AnimeRepository
import com.anidex.app.domain.repository.WatchlistRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

class GetWeightedRecommendationsUseCase @Inject constructor(
    private val watchlistRepository: WatchlistRepository,
    private val animeRepository: AnimeRepository,
    private val dispatchers: DispatcherProvider
) {

    suspend operator fun invoke(limit: Int = 15): Resource<List<RecommendedAnime>> {
        return withContext(dispatchers.default) {
            val watchlist = watchlistRepository.getWatchlist()
            val candidateResult = animeRepository.getTopAnime(page = 1, limit = 50)

            when (candidateResult) {
                is Resource.Error -> Resource.Error(candidateResult.message, candidateResult.throwable)
                is Resource.Loading -> Resource.Loading
                is Resource.Idle -> Resource.Idle
                is Resource.Success -> {
                    val recommendations = computeRecommendations(
                        watchlist = watchlist,
                        candidates = candidateResult.data,
                        limit = limit
                    )
                    Resource.Success(recommendations)
                }
            }
        }
    }

    /**
     * Executes the weighted recommendation algorithm against the candidate pool.
     */
    fun computeRecommendations(
        watchlist: List<WatchlistEntry>,
        candidates: List<Anime>,
        limit: Int
    ): List<RecommendedAnime> {
        val watchedAnimeIds = watchlist.map { it.anime.id }.toSet()
        val availableCandidates = candidates.filter { it.id !in watchedAnimeIds }

        // If user has insufficient watchlist data, fallback to global top scoring recommendations
        if (watchlist.isEmpty()) {
            return availableCandidates.take(limit).map { anime ->
                RecommendedAnime(
                    anime = anime,
                    matchScore = anime.score ?: 8.0,
                    matchPercentage = ((anime.score ?: 7.5) * 10).roundToInt().coerceIn(70, 99),
                    matchedGenres = anime.genres.map { it.name }.take(2),
                    matchedStudios = anime.studios.map { it.name },
                    reasonDescription = "Sangat populer dan memiliki rating tinggi di komunitas anime."
                )
            }
        }

        // 1. Build User Taste Affinity Profiles for Genres & Studios
        val genreAffinity = mutableMapOf<String, Double>()
        val studioAffinity = mutableMapOf<String, Double>()

        for (entry in watchlist) {
            val statusMultiplier = when (entry.status) {
                WatchStatus.COMPLETED -> 1.2
                WatchStatus.WATCHING -> 1.0
                WatchStatus.ON_HOLD -> 0.4
                WatchStatus.DROPPED -> -0.8
                WatchStatus.PLAN_TO_WATCH -> 0.3
            }

            // Score centered around 6.0: ratings > 6 add positive weight, < 6 penalize
            val scoreFactor = if (entry.userScore != null && entry.userScore > 0) {
                (entry.userScore - 6.0) / 4.0 // e.g. 10.0 -> +1.0, 6.0 -> 0.0, 2.0 -> -1.0
            } else {
                0.25 // Default modest positive for unrated items
            }

            val compoundWeight = statusMultiplier * (1.0 + scoreFactor)

            // Accumulate genre scores
            for (genre in entry.anime.genres) {
                genreAffinity[genre.name] = (genreAffinity[genre.name] ?: 0.0) + compoundWeight
            }

            // Accumulate studio scores (higher impact multiplier)
            for (studio in entry.anime.studios) {
                studioAffinity[studio.name] = (studioAffinity[studio.name] ?: 0.0) + (compoundWeight * 1.5)
            }
        }

        // 2. Score candidate anime based on affinity overlap
        val maxGenreScore = genreAffinity.values.maxOrNull()?.coerceAtLeast(1.0) ?: 1.0
        val maxStudioScore = studioAffinity.values.maxOrNull()?.coerceAtLeast(1.0) ?: 1.0

        val scoredList = availableCandidates.mapNotNull { candidate ->
            var rawScore = 0.0
            val matchedGenres = mutableListOf<String>()
            val matchedStudios = mutableListOf<String>()

            for (genre in candidate.genres) {
                val weight = genreAffinity[genre.name] ?: 0.0
                if (weight > 0) {
                    rawScore += (weight / maxGenreScore) * 35.0 // Up to 35 pts per top genre
                    matchedGenres.add(genre.name)
                } else if (weight < 0) {
                    rawScore += (weight / maxGenreScore) * 15.0 // Penalty for disliked genres
                }
            }

            for (studio in candidate.studios) {
                val weight = studioAffinity[studio.name] ?: 0.0
                if (weight > 0) {
                    rawScore += (weight / maxStudioScore) * 25.0 // Up to 25 pts for favored studio
                    matchedStudios.add(studio.name)
                }
            }

            // Baseline global rating bonus (up to 15 pts)
            val globalRating = candidate.score ?: 7.0
            rawScore += (globalRating / 10.0) * 15.0

            if (matchedGenres.isEmpty() && matchedStudios.isEmpty()) {
                // Low relevance to current profile
                null
            } else {
                val percentage = (rawScore.coerceIn(40.0, 99.0)).roundToInt()
                val reason = buildRecommendationReason(matchedGenres, matchedStudios)

                RecommendedAnime(
                    anime = candidate,
                    matchScore = rawScore,
                    matchPercentage = percentage,
                    matchedGenres = matchedGenres,
                    matchedStudios = matchedStudios,
                    reasonDescription = reason
                )
            }
        }

        return scoredList
            .sortedByDescending { it.matchScore }
            .take(limit)
    }

    private fun buildRecommendationReason(
        matchedGenres: List<String>,
        matchedStudios: List<String>
    ): String {
        return buildString {
            append("Direkomendasikan karena kamu menyukai genre ")
            append(matchedGenres.take(2).joinToString(" & "))
            if (matchedStudios.isNotEmpty()) {
                append(" dari studio ")
                append(matchedStudios.first())
            }
            append(".")
        }
    }
}

