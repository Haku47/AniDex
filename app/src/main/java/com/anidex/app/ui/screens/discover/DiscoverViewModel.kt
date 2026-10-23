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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class DiscoverTab {
    TRENDING,
    TOP_RATED,
    AIRING,
    RECOMMENDED
}

data class DiscoverUiState(
    val isLoading: Boolean = true,
    val selectedTab: DiscoverTab = DiscoverTab.TRENDING,
    val selectedCategory: String? = null,
    val trendingAnime: List<Anime> = emptyList(),
    val topRatedAnime: List<Anime> = emptyList(),
    val topAnime: List<Anime> = emptyList(),
    val airingAnime: List<Anime> = emptyList(),
    val recommendations: List<RecommendedAnime> = emptyList(),
    val categoryAnime: List<Anime> = emptyList(),
    val isCategoryLoading: Boolean = false,
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

            val trendingDeferred = async { animeRepository.getTrendingAnime(limit = 20) }
            val topDeferred = async { animeRepository.getTopAnime(page = 1, limit = 20) }
            val topRatedDeferred = async { animeRepository.getTopRatedAnime(page = 1, limit = 20) }
            val airingDeferred = async { animeRepository.getAiringSchedules() }
            val recDeferred = async { getWeightedRecommendationsUseCase(limit = 10) }

            val trendingRes = trendingDeferred.await()
            val topRes = topDeferred.await()
            val topRatedRes = topRatedDeferred.await()
            val airingRes = airingDeferred.await()
            val recRes = recDeferred.await()

            val trendingList = if (trendingRes is Resource.Success) trendingRes.data else emptyList()
            val topList = if (topRes is Resource.Success) topRes.data else emptyList()
            val topRatedList = if (topRatedRes is Resource.Success) topRatedRes.data else emptyList()
            val airingList = if (airingRes is Resource.Success) airingRes.data else emptyList()
            val recList = if (recRes is Resource.Success) recRes.data else emptyList()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    trendingAnime = if (trendingList.isNotEmpty()) trendingList else topList,
                    topAnime = topList,
                    topRatedAnime = if (topRatedList.isNotEmpty()) topRatedList else topList,
                    airingAnime = airingList,
                    recommendations = recList,
                    errorMessage = null
                )
            }
        }
    }

    fun selectTab(tab: DiscoverTab) {
        _uiState.update { it.copy(selectedTab = tab, selectedCategory = null) }
    }

    fun selectCategory(category: String?) {
        if (category == null || category == "All" || category == "Semua") {
            _uiState.update { it.copy(selectedCategory = null, categoryAnime = emptyList()) }
            return
        }

        _uiState.update { it.copy(selectedCategory = category, isCategoryLoading = true) }
        viewModelScope.launch {
            val result = animeRepository.getAnimeByCategory(category)
            if (result is Resource.Success) {
                _uiState.update {
                    it.copy(
                        isCategoryLoading = false,
                        categoryAnime = result.data
                    )
                }
            } else {
                _uiState.update { it.copy(isCategoryLoading = false) }
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
        }
    }
}
