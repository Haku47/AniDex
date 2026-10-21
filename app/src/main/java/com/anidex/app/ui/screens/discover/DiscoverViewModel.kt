package com.anidex.app.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anidex.app.core.common.Resource
import com.anidex.app.domain.model.Anime
import com.anidex.app.domain.model.RecommendedAnime
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchlistEntry
import com.anidex.app.domain.repository.AnimeRepository
import com.anidex.app.domain.repository.WatchlistRepository
import com.anidex.app.domain.usecase.GetWeightedRecommendationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiscoverUiState(
    val isLoading: Boolean = true,
    val topAnime: List<Anime> = emptyList(),
    val recommendations: List<RecommendedAnime> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<Anime> = emptyList(),
    val isSearching: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val watchlistRepository: WatchlistRepository,
    private val getWeightedRecommendationsUseCase: GetWeightedRecommendationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        loadDiscoverData()
    }

    fun loadDiscoverData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Fetch Top Anime
            val topResult = animeRepository.getTopAnime(page = 1, limit = 20)
            val recResult = getWeightedRecommendationsUseCase(limit = 10)

            when {
                topResult is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = topResult.message)
                    }
                }
                topResult is Resource.Success -> {
                    val recs = if (recResult is Resource.Success) recResult.data else emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            topAnime = topResult.data,
                            recommendations = recs,
                            errorMessage = null
                        )
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.length >= 3) {
            searchAnime(query)
        } else if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
        }
    }

    private fun searchAnime(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true) }
            val result = animeRepository.searchAnime(query)
            if (result is Resource.Success) {
                _uiState.update { it.copy(isSearching = false, searchResults = result.data) }
            } else {
                _uiState.update { it.copy(isSearching = false) }
            }
        }
    }

    fun addToWatchlist(anime: Anime, status: WatchStatus = WatchStatus.WATCHING) {
        viewModelScope.launch {
            val entry = WatchlistEntry(
                anime = anime,
                status = status,
                episodesWatched = if (status == WatchStatus.COMPLETED) anime.episodes ?: 1 else 0,
                userScore = null,
                lastUpdatedEpochMillis = System.currentTimeMillis()
            )
            watchlistRepository.upsertWatchlistEntry(entry)
            // Reload recommendations since watchlist profile updated!
            val updatedRecs = getWeightedRecommendationsUseCase(limit = 10)
            if (updatedRecs is Resource.Success) {
                _uiState.update { it.copy(recommendations = updatedRecs.data) }
            }
        }
    }
}

