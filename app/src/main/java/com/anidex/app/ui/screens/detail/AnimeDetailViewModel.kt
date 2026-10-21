package com.anidex.app.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anidex.app.core.common.Resource
import com.anidex.app.domain.model.Anime
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchlistEntry
import com.anidex.app.domain.repository.AnimeRepository
import com.anidex.app.domain.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnimeDetailUiState(
    val isLoading: Boolean = true,
    val anime: Anime? = null,
    val watchlistEntry: WatchlistEntry? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val watchlistRepository: WatchlistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int = checkNotNull(savedStateHandle["animeId"]) {
        "animeId argument is required"
    }.toString().toIntOrNull() ?: 1

    private val _uiState = MutableStateFlow(AnimeDetailUiState())
    val uiState: StateFlow<AnimeDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val existingEntry = watchlistRepository.getWatchlistEntry(animeId)

            when (val result = animeRepository.getAnimeDetail(animeId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            anime = result.data,
                            watchlistEntry = existingEntry
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message,
                            watchlistEntry = existingEntry
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun updateWatchlistStatus(newStatus: WatchStatus) {
        val anime = _uiState.value.anime ?: return
        viewModelScope.launch {
            val current = _uiState.value.watchlistEntry
            val totalEps = anime.episodes ?: 1
            val watched = when {
                newStatus == WatchStatus.COMPLETED -> totalEps
                current != null -> current.episodesWatched
                else -> 0
            }

            val updated = WatchlistEntry(
                anime = anime,
                status = newStatus,
                episodesWatched = watched,
                userScore = current?.userScore,
                lastUpdatedEpochMillis = System.currentTimeMillis()
            )
            watchlistRepository.upsertWatchlistEntry(updated)
            _uiState.update { it.copy(watchlistEntry = updated) }
        }
    }

    fun updateEpisodesWatched(count: Int) {
        val anime = _uiState.value.anime ?: return
        val current = _uiState.value.watchlistEntry
        val totalEps = anime.episodes ?: Int.MAX_VALUE
        val sanitized = count.coerceIn(0, totalEps)
        val newStatus = if (anime.episodes != null && sanitized >= anime.episodes) {
            WatchStatus.COMPLETED
        } else {
            current?.status ?: WatchStatus.WATCHING
        }

        viewModelScope.launch {
            val updated = WatchlistEntry(
                anime = anime,
                status = newStatus,
                episodesWatched = sanitized,
                userScore = current?.userScore,
                lastUpdatedEpochMillis = System.currentTimeMillis()
            )
            watchlistRepository.upsertWatchlistEntry(updated)
            _uiState.update { it.copy(watchlistEntry = updated) }
        }
    }

    fun updateUserScore(score: Double) {
        val anime = _uiState.value.anime ?: return
        val current = _uiState.value.watchlistEntry ?: WatchlistEntry(
            anime = anime,
            status = WatchStatus.WATCHING,
            episodesWatched = 0,
            userScore = score,
            lastUpdatedEpochMillis = System.currentTimeMillis()
        )

        viewModelScope.launch {
            val updated = current.copy(userScore = score, lastUpdatedEpochMillis = System.currentTimeMillis())
            watchlistRepository.upsertWatchlistEntry(updated)
            _uiState.update { it.copy(watchlistEntry = updated) }
        }
    }

    fun removeFromWatchlist() {
        viewModelScope.launch {
            watchlistRepository.deleteWatchlistEntry(animeId)
            _uiState.update { it.copy(watchlistEntry = null) }
        }
    }
}

