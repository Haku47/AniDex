package com.anidex.app.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anidex.app.domain.model.WatchTimeSummary
import com.anidex.app.domain.usecase.CalculateWatchTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    calculateWatchTimeUseCase: CalculateWatchTimeUseCase
) : ViewModel() {

    val summary: StateFlow<WatchTimeSummary> = calculateWatchTimeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WatchTimeSummary(
                totalMinutes = 0L,
                totalEpisodesWatched = 0,
                animeCountByStatus = emptyMap(),
                minutesByFormat = emptyMap(),
                meanUserRating = null,
                completionRate = 0f
            )
        )
}

