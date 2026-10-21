package com.anidex.app.domain.usecase

import com.anidex.app.core.common.DispatcherProvider
import com.anidex.app.core.common.Resource
import com.anidex.app.domain.model.Anime
import com.anidex.app.domain.model.AnimeFormat
import com.anidex.app.domain.model.AnimeStatus
import com.anidex.app.domain.model.Genre
import com.anidex.app.domain.model.Studio
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchlistEntry
import com.anidex.app.domain.repository.AnimeRepository
import com.anidex.app.domain.repository.WatchlistRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetWeightedRecommendationsUseCaseTest {

    private lateinit var useCase: GetWeightedRecommendationsUseCase

    private val fakeWatchlistRepo = object : WatchlistRepository {
        override fun getWatchlistStream(): Flow<List<WatchlistEntry>> = flowOf(emptyList())
        override fun getWatchlistByStatusStream(status: WatchStatus): Flow<List<WatchlistEntry>> = flowOf(emptyList())
        override suspend fun getWatchlist(): List<WatchlistEntry> = emptyList()
        override suspend fun getWatchlistEntry(animeId: Int): WatchlistEntry? = null
        override suspend fun upsertWatchlistEntry(entry: WatchlistEntry) {}
        override suspend fun updateEpisodeProgress(animeId: Int, episodeWatched: Int) {}
        override suspend fun deleteWatchlistEntry(animeId: Int) {}
    }

    private val fakeAnimeRepo = object : AnimeRepository {
        override suspend fun getAnimeDetail(animeId: Int): Resource<Anime> = Resource.Error("Stub")
        override suspend fun getTopAnime(page: Int, limit: Int): Resource<List<Anime>> = Resource.Success(emptyList())
        override suspend fun getSeasonalAnime(year: Int, season: String, page: Int): Resource<List<Anime>> = Resource.Success(emptyList())
        override suspend fun getAiringSchedules(dayOfWeek: String?): Resource<List<Anime>> = Resource.Success(emptyList())
        override suspend fun searchAnime(query: String, page: Int): Resource<List<Anime>> = Resource.Success(emptyList())
    }

    private val testDispatchers = object : DispatcherProvider {
        override val main: CoroutineDispatcher = Dispatchers.Unconfined
        override val io: CoroutineDispatcher = Dispatchers.Unconfined
        override val default: CoroutineDispatcher = Dispatchers.Unconfined
        override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
    }

    @Before
    fun setUp() {
        useCase = GetWeightedRecommendationsUseCase(
            watchlistRepository = fakeWatchlistRepo,
            animeRepository = fakeAnimeRepo,
            dispatchers = testDispatchers
        )
    }

    @Test
    fun `computeRecommendations prioritizes candidates matching user favorite genres and studio`() {
        val actionGenre = Genre(1, "Action")
        val romanceGenre = Genre(2, "Romance")
        val mappaStudio = Studio(1, "MAPPA")
        val kyoAniStudio = Studio(2, "Kyoto Animation")

        // User watched and rated 10/10 an Action anime from MAPPA
        val watchedAnime = createAnime(
            id = 100,
            title = "Jujutsu Kaisen",
            genres = listOf(actionGenre),
            studios = listOf(mappaStudio)
        )
        val watchlist = listOf(
            WatchlistEntry(
                anime = watchedAnime,
                status = WatchStatus.COMPLETED,
                episodesWatched = 24,
                userScore = 10.0,
                lastUpdatedEpochMillis = 0L
            )
        )

        // Candidates
        val candidate1 = createAnime(
            id = 101,
            title = "Chainsaw Man",
            genres = listOf(actionGenre),
            studios = listOf(mappaStudio)
        )
        val candidate2 = createAnime(
            id = 102,
            title = "Clannad",
            genres = listOf(romanceGenre),
            studios = listOf(kyoAniStudio)
        )

        val recommendations = useCase.computeRecommendations(
            watchlist = watchlist,
            candidates = listOf(candidate1, candidate2),
            limit = 5
        )

        // Chainsaw Man should rank 1st because of matching Action & MAPPA affinity
        assertEquals(1, recommendations.size) // Clannad has 0 overlap with user profile
        assertEquals(101, recommendations.first().anime.id)
        assertTrue(recommendations.first().matchPercentage >= 70)
        assertTrue(recommendations.first().matchedGenres.contains("Action"))
        assertTrue(recommendations.first().matchedStudios.contains("MAPPA"))
    }

    private fun createAnime(
        id: Int,
        title: String,
        genres: List<Genre>,
        studios: List<Studio>
    ): Anime {
        return Anime(
            id = id,
            title = title,
            englishTitle = null,
            synopsis = "Synopsis",
            imageUrl = "",
            bannerUrl = null,
            score = 8.5,
            scoredBy = 1000,
            rank = 1,
            popularity = 1,
            episodes = 12,
            episodeDurationMinutes = 24,
            format = AnimeFormat.TV,
            status = AnimeStatus.FINISHED,
            genres = genres,
            studios = studios,
            broadcastDay = null,
            broadcastTime = null,
            broadcastTimezone = null,
            year = 2024,
            season = "fall"
        )
    }
}

