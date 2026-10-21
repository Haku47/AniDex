package com.anidex.app.ui.screens.watchlist

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.ui.components.EmptyState
import com.anidex.app.ui.components.WatchProgressCard
import com.anidex.app.ui.components.WatchTimeHeaderCard
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.SurfaceDark
import com.anidex.app.ui.theme.TextPrimary

@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Dynamic Watch Progress & Time Spent Calculator Header (Fire & Ice Gradient)
        WatchTimeHeaderCard(summary = state.watchTimeSummary)

        Spacer(modifier = Modifier.height(14.dp))

        // Status filter tabs
        val filterOptions: List<Pair<String, WatchStatus?>> = listOf(
            "Semua" to null,
            "Menonton" to WatchStatus.WATCHING,
            "Selesai" to WatchStatus.COMPLETED,
            "Rencana" to WatchStatus.PLAN_TO_WATCH,
            "Ditunda" to WatchStatus.ON_HOLD,
            "Ditinggalkan" to WatchStatus.DROPPED
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filterOptions) { (label, status) ->
                val isSelected = state.selectedStatusFilter == status
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) BrandOrange else SurfaceDark,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { viewModel.setStatusFilter(status) }
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.White else TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (state.filteredEntries.isEmpty()) {
            EmptyState(
                title = "Watchlist Kosong",
                subtitle = "Kamu belum menambahkan anime ke kategori ini. Jelajahi katalog dan tekan ikon bookmark untuk mulai melacak!"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(state.filteredEntries, key = { it.anime.id }) { entry ->
                    WatchProgressCard(
                        entry = entry,
                        onIncrementEpisode = { viewModel.incrementEpisode(entry) },
                        onDecrementEpisode = { viewModel.decrementEpisode(entry) },
                        onDelete = { viewModel.deleteEntry(entry.anime.id) },
                        onStatusChange = { newStatus -> viewModel.updateStatus(entry, newStatus) }
                    )
                }
            }
        }
    }
}
