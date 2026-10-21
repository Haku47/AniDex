package com.anidex.app.ui.screens.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.ui.components.ErrorState
import com.anidex.app.ui.components.LoadingState
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.BrandRed
import com.anidex.app.ui.theme.BrandYellow
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.SurfaceDark
import com.anidex.app.ui.theme.SurfaceVariantDark
import com.anidex.app.ui.theme.TextPrimary
import com.anidex.app.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimeDetailScreen(
    viewModel: AnimeDetailViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var isSynopsisExpanded by remember { mutableStateOf(false) }

    when {
        state.isLoading -> {
            LoadingState(message = "Memuat rincian anime...")
        }
        state.errorMessage != null && state.anime == null -> {
            ErrorState(
                message = state.errorMessage ?: "Gagal memuat detail anime",
                onRetry = viewModel::loadDetail
            )
        }
        state.anime != null -> {
            val anime = state.anime!!
            val entry = state.watchlistEntry

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DeepBackground)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Banner & Overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    AsyncImage(
                        model = anime.bannerUrl ?: anime.imageUrl,
                        contentDescription = anime.displayTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Dark Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.6f),
                                        Color.Transparent,
                                        DeepBackground
                                    )
                                )
                            )
                    )

                    // Top App Bar Icons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.5f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Kembali",
                                    tint = Color.White
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.5f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Membagikan: ${anime.displayTitle}", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // Overlapping Poster & Quick Info
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            modifier = Modifier
                                .width(110.dp)
                                .aspectRatio(0.7f)
                        ) {
                            AsyncImage(
                                model = anime.imageUrl,
                                contentDescription = anime.displayTitle,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = BrandOrange
                            ) {
                                Text(
                                    text = anime.format.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            anime.score?.let { score ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = BrandYellow,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = String.format("%.2f", score),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                // Titles & Metadata
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = anime.displayTitle,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        color = TextPrimary,
                        lineHeight = 28.sp
                    )

                    anime.englishTitle?.let { eng ->
                        if (eng != anime.displayTitle) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = eng,
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Watchlist Manager Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            if (entry == null) {
                                Button(
                                    onClick = { viewModel.updateWatchlistStatus(WatchStatus.WATCHING) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
                                ) {
                                    Icon(imageVector = Icons.Default.Bookmark, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "Tambahkan ke Watchlist", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = "Status Watchlist", fontSize = 11.sp, color = TextSecondary)
                                        Text(text = entry.status.displayName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BrandCyan)
                                    }

                                    IconButton(onClick = { viewModel.removeFromWatchlist() }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus", tint = BrandRed.copy(alpha = 0.8f))
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Status Switcher Chips
                                val statuses = WatchStatus.values()
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    statuses.forEach { status ->
                                        val isCurrent = entry.status == status
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isCurrent) BrandOrange else SurfaceVariantDark,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .clickable { viewModel.updateWatchlistStatus(status) }
                                        ) {
                                            Text(
                                                text = status.displayName,
                                                fontSize = 11.sp,
                                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isCurrent) Color.White else TextSecondary,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Episode Progress Stepper
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(SurfaceVariantDark, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val total = anime.episodes ?: 0
                                    Text(
                                        text = "Episode Ditonton: ${entry.episodesWatched} / ${if (total > 0) total else '?'}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        FilledTonalIconButton(
                                            onClick = { viewModel.updateEpisodesWatched(entry.episodesWatched - 1) },
                                            enabled = entry.episodesWatched > 0,
                                            modifier = Modifier.size(32.dp),
                                            colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = SurfaceDark)
                                        ) {
                                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Kurang", modifier = Modifier.size(16.dp))
                                        }

                                        Spacer(modifier = Modifier.width(6.dp))

                                        FilledTonalIconButton(
                                            onClick = { viewModel.updateEpisodesWatched(entry.episodesWatched + 1) },
                                            enabled = total == 0 || entry.episodesWatched < total,
                                            modifier = Modifier.size(32.dp),
                                            colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = BrandOrange, contentColor = Color.White)
                                        ) {
                                            Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah", modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Synopsis
                    Text(
                        text = "Sinopsis",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = anime.synopsis,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp,
                        maxLines = if (isSynopsisExpanded) Int.MAX_VALUE else 4,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = if (isSynopsisExpanded) "Tampilkan Lebih Sedikit" else "Baca Selengkapnya",
                        color = BrandBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable { isSynopsisExpanded = !isSynopsisExpanded }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Anime Specifications Grid
                    Text(
                        text = "Informasi Tambahan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            SpecRow("Status", anime.status.name)
                            SpecRow("Format", anime.format.name)
                            SpecRow("Durasi per Ep", "${anime.effectiveDurationMinutes} Menit")
                            SpecRow("Musim / Tahun", "${anime.season?.uppercase() ?: "-"} ${anime.year ?: ""}")
                            SpecRow("Jadwal Tayang", "${anime.broadcastDay ?: "-"} (${anime.broadcastTime ?: "-"} JST)")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}

