package com.anidex.app.ui.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchTimeSummary
import com.anidex.app.domain.model.WatchlistEntry
import com.anidex.app.domain.repository.WatchlistRepository
import com.anidex.app.domain.usecase.CalculateWatchTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WatchlistUiState(
    val entries: List<WatchlistEntry> = emptyList(),
    val filteredEntries: List<WatchlistEntry> = emptyList(),
    val selectedStatusFilter: WatchStatus? = null, // null means "Semua"
    val watchTimeSummary: WatchTimeSummary = WatchTimeSummary(
        totalMinutes = 0L,
        totalEpisodesWatched = 0,
        animeCountByStatus = emptyMap(),
        minutesByFormat = emptyMap(),
        meanUserRating = null,
        completionRate = 0f
    ),
    val isLoading: Boolean = false
)

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistRepository: WatchlistRepository,
    private val calculateWatchTimeUseCase: CalculateWatchTimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    init {
        observeWatchlist()
    }

    private fun observeWatchlist() {
        combine(
            watchlistRepository.getWatchlistStream(),
            calculateWatchTimeUseCase()
        ) { entries, summary ->
            val filter = _uiState.value.selectedStatusFilter
            val filtered = if (filter != null) entries.filter { it.status == filter } else entries

            _uiState.update {
                it.copy(
                    entries = entries,
                    filteredEntries = filtered,
                    watchTimeSummary = summary,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun setStatusFilter(status: WatchStatus?) {
        _uiState.update { state ->
            val filtered = if (status != null) state.entries.filter { it.status == status } else state.entries
            state.copy(selectedStatusFilter = status, filteredEntries = filtered)
        }
    }

    fun incrementEpisode(entry: WatchlistEntry) {
        viewModelScope.launch {
            val totalEps = entry.anime.episodes ?: Int.MAX_VALUE
            val newCount = (entry.episodesWatched + 1).coerceAtMost(totalEps)
            val newStatus = if (entry.anime.episodes != null && newCount >= entry.anime.episodes) {
                WatchStatus.COMPLETED
            } else {
                entry.status
            }

            val updated = entry.copy(
                episodesWatched = newCount,
                status = newStatus,
                lastUpdatedEpochMillis = System.currentTimeMillis()
            )
            watchlistRepository.upsertWatchlistEntry(updated)
        }
    }

    fun decrementEpisode(entry: WatchlistEntry) {
        viewModelScope.launch {
            val newCount = (entry.episodesWatched - 1).coerceAtLeast(0)
            val updated = entry.copy(
                episodesWatched = newCount,
                lastUpdatedEpochMillis = System.currentTimeMillis()
            )
            watchlistRepository.upsertWatchlistEntry(updated)
        }
    }

    fun updateStatus(entry: WatchlistEntry, newStatus: WatchStatus) {
        viewModelScope.launch {
            val newWatched = if (newStatus == WatchStatus.COMPLETED) {
                entry.anime.episodes ?: entry.episodesWatched
            } else {
                entry.episodesWatched
            }

            val updated = entry.copy(
                status = newStatus,
                episodesWatched = newWatched,
                lastUpdatedEpochMillis = System.currentTimeMillis()
            )
            watchlistRepository.upsertWatchlistEntry(updated)
        }
    }

    fun deleteEntry(animeId: Int) {
        viewModelScope.launch {
            watchlistRepository.deleteWatchlistEntry(animeId)
        }
    }
}

