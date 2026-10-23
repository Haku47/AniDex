package com.anidex.app.ui.screens.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anidex.app.R
import com.anidex.app.ui.components.AiringScheduleCard
import com.anidex.app.ui.components.EmptyState
import com.anidex.app.ui.components.ErrorState
import com.anidex.app.ui.components.LoadingState
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.SurfaceDark
import com.anidex.app.ui.theme.TextPrimary
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val today = LocalDate.now().dayOfWeek

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepBackground)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(R.string.schedule_title),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Day of Week Selector Bar
        val days = DayOfWeek.values()
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(days) { day ->
                val isSelected = day == state.selectedDay
                val isToday = day == today
                val dayLabel = when (day) {
                    DayOfWeek.MONDAY -> stringResource(R.string.day_monday)
                    DayOfWeek.TUESDAY -> stringResource(R.string.day_tuesday)
                    DayOfWeek.WEDNESDAY -> stringResource(R.string.day_wednesday)
                    DayOfWeek.THURSDAY -> stringResource(R.string.day_thursday)
                    DayOfWeek.FRIDAY -> stringResource(R.string.day_friday)
                    DayOfWeek.SATURDAY -> stringResource(R.string.day_saturday)
                    DayOfWeek.SUNDAY -> stringResource(R.string.day_sunday)
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) BrandOrange else SurfaceDark,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { viewModel.selectDay(day) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = dayLabel,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else TextPrimary,
                            fontSize = 13.sp
                        )
                        if (isToday) {
                            Text(
                                text = stringResource(R.string.today_label),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else BrandCyan
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when {
            state.isLoading -> {
                LoadingState(message = stringResource(R.string.loading_schedule))
            }
            state.errorMessage != null -> {
                ErrorState(
                    message = state.errorMessage ?: stringResource(R.string.schedule_load_error),
                    onRetry = viewModel::loadSchedules
                )
            }
            state.schedulesForSelectedDay.isEmpty() -> {
                EmptyState(
                    title = stringResource(R.string.empty_schedule_title),
                    subtitle = stringResource(R.string.empty_schedule_subtitle)
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(state.schedulesForSelectedDay, key = { it.anime.id }) { schedule ->
                        AiringScheduleCard(
                            schedule = schedule,
                            modifier = Modifier.clickable { onAnimeClick(schedule.anime.id) }
                        )
                    }
                }
            }
        }
    }
}
