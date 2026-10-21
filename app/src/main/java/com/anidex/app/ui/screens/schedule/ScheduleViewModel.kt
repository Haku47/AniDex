package com.anidex.app.ui.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anidex.app.core.common.Resource
import com.anidex.app.domain.model.AiringSchedule
import com.anidex.app.domain.usecase.GetAiringScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class ScheduleUiState(
    val isLoading: Boolean = true,
    val selectedDay: DayOfWeek = LocalDate.now().dayOfWeek,
    val schedulesForSelectedDay: List<AiringSchedule> = emptyList(),
    val allSchedules: List<AiringSchedule> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getAiringScheduleUseCase: GetAiringScheduleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        loadSchedules()
    }

    fun loadSchedules() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = getAiringScheduleUseCase(targetZoneId = ZoneId.systemDefault())

            when (result) {
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is Resource.Success -> {
                    val all = result.data
                    val currentDay = _uiState.value.selectedDay
                    val filtered = all.filter { it.localDayOfWeek == currentDay }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            allSchedules = all,
                            schedulesForSelectedDay = filtered,
                            errorMessage = null
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun selectDay(dayOfWeek: DayOfWeek) {
        _uiState.update { state ->
            state.copy(
                selectedDay = dayOfWeek,
                schedulesForSelectedDay = state.allSchedules.filter { it.localDayOfWeek == dayOfWeek }
            )
        }
    }
}

