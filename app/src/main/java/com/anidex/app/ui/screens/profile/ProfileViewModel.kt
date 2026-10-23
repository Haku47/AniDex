package com.anidex.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anidex.app.core.common.Resource
import com.anidex.app.domain.model.User
import com.anidex.app.domain.model.WatchTimeSummary
import com.anidex.app.domain.repository.AuthRepository
import com.anidex.app.domain.usecase.CalculateWatchTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    ),
    val isUpdating: Boolean = false,
    val updateMessage: String? = null,
    val updateError: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    calculateWatchTimeUseCase: CalculateWatchTimeUseCase
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)
    private val _updateMessage = MutableStateFlow<String?>(null)
    private val _updateError = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ProfileUiState> = combine(
        authRepository.currentUser,
        calculateWatchTimeUseCase(),
        _isUpdating,
        _updateMessage,
        _updateError
    ) { user, summary, isUpdating, message, error ->
        ProfileUiState(
            user = user,
            summary = summary,
            isUpdating = isUpdating,
            updateMessage = message,
            updateError = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )

    fun updateProfile(
        name: String,
        bio: String,
        avatarUrl: String?,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isUpdating.value = true
            _updateError.value = null
            _updateMessage.value = null

            when (val result = authRepository.updateProfile(name = name, bio = bio, avatarUrl = avatarUrl)) {
                is Resource.Success -> {
                    _isUpdating.value = false
                    _updateMessage.value = "Profile updated successfully!"
                    onSuccess()
                }
                is Resource.Error -> {
                    _isUpdating.value = false
                    _updateError.value = result.message ?: "Failed to update profile."
                }
                else -> {
                    _isUpdating.value = false
                }
            }
        }
    }

    fun clearMessages() {
        _updateMessage.value = null
        _updateError.value = null
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLoggedOut()
        }
    }
}
