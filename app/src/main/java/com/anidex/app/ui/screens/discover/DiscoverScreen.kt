package com.anidex.app.ui.screens.discover

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.anidex.app.R
import com.anidex.app.domain.model.RecommendedAnime
import com.anidex.app.ui.components.AnimeGridCard
import com.anidex.app.ui.components.EmptyState
import com.anidex.app.ui.components.ErrorState
import com.anidex.app.ui.components.LoadingState
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.SurfaceDark
import com.anidex.app.ui.theme.TextPrimary
import com.anidex.app.ui.theme.TextSecondary

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    onAnimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // App Brand Header with Mascot Logo (Centered)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_anidex),
                contentDescription = "AniDex Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(52.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = BrandOrange, fontWeight = FontWeight.Black)) {
                            append("Ani")
                        }
                        withStyle(style = SpanStyle(color = BrandBlue, fontWeight = FontWeight.Black)) {
                            append("Dex")
                        }
                    },
                    fontSize = 24.sp,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Anime Catalog & Smart Tracker",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar with Brand Accent
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cari anime berdasarkan judul...", color = TextSecondary, fontSize = 14.sp) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = BrandOrange)
            },
            trailingIcon = {
                if (state.isSearching) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = BrandOrange, strokeWidth = 2.dp)
                } else if (state.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextSecondary)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedBorderColor = BrandOrange,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        when {
            state.isLoading -> {
                LoadingState(message = "Memuat katalog anime...")
            }
            state.errorMessage != null -> {
                ErrorState(
                    message = state.errorMessage ?: "Terjadi kesalahan",
                    onRetry = viewModel::loadDiscoverData
                )
            }
            state.searchQuery.isNotBlank() -> {
                if (state.searchResults.isEmpty() && !state.isSearching) {
                    EmptyState(
                        title = "Tidak Ditemukan",
                        subtitle = "Tidak ada anime yang cocok dengan '${state.searchQuery}'"
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(state.searchResults, key = { it.id }) { anime ->
                            AnimeGridCard(
                                anime = anime,
                                onAnimeClick = { onAnimeClick(anime.id) },
                                onAddToWatchlist = {
                                    viewModel.addToWatchlist(anime)
                                    Toast.makeText(context, "Ditambahkan ke Watchlist!", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    // Recommendation Carousel
                    if (state.recommendations.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = BrandCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Rekomendasi Berbobot Untukmu",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = TextPrimary
                                    )
                                }

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(vertical = 6.dp)
                                ) {
                                    items(state.recommendations, key = { it.anime.id }) { rec ->
                                        RecommendationCard(
                                            rec = rec,
                                            onAdd = {
                                                viewModel.addToWatchlist(rec.anime)
                                                Toast.makeText(context, "Ditambahkan ke Watchlist!", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                            }
                        }
                    }

                    // Top Anime Section Title
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = "Anime Terpopuler & Rating Tertinggi",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    // Anime Grid Items
                    items(state.topAnime, key = { it.id }) { anime ->
                        AnimeGridCard(
                            anime = anime,
                            onAnimeClick = { onAnimeClick(anime.id) },
                            onAddToWatchlist = {
                                viewModel.addToWatchlist(anime)
                                Toast.makeText(context, "Ditambahkan ke Watchlist!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationCard(
    rec: RecommendedAnime,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(260.dp)
            .clip(RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = rec.anime.imageUrl,
                    contentDescription = rec.anime.displayTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 54.dp, height = 76.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = BrandCyan.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Cocok ${rec.matchPercentage}%",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = BrandCyan,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = rec.anime.displayTitle,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onAdd, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.BookmarkAdd, contentDescription = "Add", tint = BrandOrange)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = rec.reasonDescription,
                fontSize = 10.sp,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 13.sp
            )
        }
    }
}
