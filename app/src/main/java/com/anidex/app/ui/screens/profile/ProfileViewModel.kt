package com.anidex.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anidex.app.domain.model.User
import com.anidex.app.domain.model.WatchTimeSummary
import com.anidex.app.domain.repository.AuthRepository
import com.anidex.app.domain.usecase.CalculateWatchTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val summary: WatchTimeSummary = WatchTimeSummary(
        totalMinutes = 0L,
        totalEpisodesWatched = 0,
        animeCountByStatus = emptyMap(),
        minutesByFormat = emptyMap(),
        meanUserRating = null,
        completionRate = 0f
    )
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    calculateWatchTimeUseCase: CalculateWatchTimeUseCase
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        authRepository.currentUser,
        calculateWatchTimeUseCase()
    ) { user, summary ->
        ProfileUiState(user = user, summary = summary)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLoggedOut()
        }
    }
}

