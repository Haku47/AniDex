package com.anidex.app.domain.usecase

import com.anidex.app.core.common.DispatcherProvider
import com.anidex.app.domain.model.Anime
import com.anidex.app.domain.model.AnimeFormat
import com.anidex.app.domain.model.AnimeStatus
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchlistEntry
import com.anidex.app.domain.repository.WatchlistRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class CalculateWatchTimeUseCaseTest {

    private lateinit var useCase: CalculateWatchTimeUseCase

    private val fakeRepository = object : WatchlistRepository {
        override fun getWatchlistStream(): Flow<List<WatchlistEntry>> = flowOf(emptyList())
        override fun getWatchlistByStatusStream(status: WatchStatus): Flow<List<WatchlistEntry>> = flowOf(emptyList())
        override suspend fun getWatchlist(): List<WatchlistEntry> = emptyList()
        override suspend fun getWatchlistEntry(animeId: Int): WatchlistEntry? = null
        override suspend fun upsertWatchlistEntry(entry: WatchlistEntry) {}
        override suspend fun updateEpisodeProgress(animeId: Int, episodeWatched: Int) {}
        override suspend fun deleteWatchlistEntry(animeId: Int) {}
    }

    private val testDispatchers = object : DispatcherProvider {
        override val main: CoroutineDispatcher = Dispatchers.Unconfined
        override val io: CoroutineDispatcher = Dispatchers.Unconfined
        override val default: CoroutineDispatcher = Dispatchers.Unconfined
        override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
    }

    @Before
    fun setUp() {
        useCase = CalculateWatchTimeUseCase(fakeRepository, testDispatchers)
    }

    @Test
    fun `calculateSummary computes exact days hours and minutes correctly`() {
        val anime1 = createAnime(id = 1, episodes = 24, duration = 24, format = AnimeFormat.TV)
        val anime2 = createAnime(id = 2, episodes = 1, duration = 120, format = AnimeFormat.MOVIE)

        val entries = listOf(
            WatchlistEntry(
                anime = anime1,
                status = WatchStatus.COMPLETED,
                episodesWatched = 24, // 24 * 24 = 576 minutes
                userScore = 9.0,
                lastUpdatedEpochMillis = 0L
            ),
            WatchlistEntry(
                anime = anime2,
                status = WatchStatus.COMPLETED,
                episodesWatched = 1, // 1 * 120 = 120 minutes
                userScore = 8.0,
                lastUpdatedEpochMillis = 0L
            )
        )

        // Total = 576 + 120 = 696 minutes = 11 hours, 36 minutes
        val summary = useCase.calculateSummary(entries)

        assertEquals(696L, summary.totalMinutes)
        assertEquals(0L, summary.days)
        assertEquals(11L, summary.hours)
        assertEquals(36L, summary.minutes)
        assertEquals(25, summary.totalEpisodesWatched)
        assertNotNull(summary.meanUserRating)
        assertEquals(8.5, summary.meanUserRating!!, 0.01)
        assertEquals(1.0f, summary.completionRate, 0.01f)
    }

    private fun createAnime(
        id: Int,
        episodes: Int,
        duration: Int,
        format: AnimeFormat
    ): Anime {
        return Anime(
            id = id,
            title = "Test Anime $id",
            englishTitle = null,
            synopsis = "Synopsis",
            imageUrl = "",
            bannerUrl = null,
            score = 8.0,
            scoredBy = 100,
            rank = 1,
            popularity = 1,
            episodes = episodes,
            episodeDurationMinutes = duration,
            format = format,
            status = AnimeStatus.FINISHED,
            genres = emptyList(),
            studios = emptyList(),
            broadcastDay = null,
            broadcastTime = null,
            broadcastTimezone = null,
            year = 2024,
            season = "spring"
        )
    }
}

