package com.anidex.app.ui.screens.discover

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.anidex.app.R
import com.anidex.app.domain.model.Anime
import com.anidex.app.domain.model.RecommendedAnime
import com.anidex.app.ui.components.AnimeGridCard
import com.anidex.app.ui.components.EmptyState
import com.anidex.app.ui.components.ErrorState
import com.anidex.app.ui.components.LoadingState
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.BrandYellow
import com.anidex.app.ui.theme.CardBorder
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.SurfaceDark
import com.anidex.app.ui.theme.SurfaceVariantDark
import com.anidex.app.ui.theme.TextPrimary
import com.anidex.app.ui.theme.TextSecondary

private data class CategoryItem(val key: String, val labelRes: Int)

private val categoriesList = listOf(
    CategoryItem("All", R.string.category_all),
    CategoryItem("Action", R.string.category_action),
    CategoryItem("Adventure", R.string.category_adventure),
    CategoryItem("Comedy", R.string.category_comedy),
    CategoryItem("Fantasy", R.string.category_fantasy),
    CategoryItem("Romance", R.string.category_romance),
    CategoryItem("Sci-Fi", R.string.category_scifi),
    CategoryItem("Drama", R.string.category_drama),
    CategoryItem("Supernatural", R.string.category_supernatural)
)

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
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // App Brand Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
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
                    text = stringResource(R.string.app_subtitle),
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.search_placeholder),
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
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

        Spacer(modifier = Modifier.height(12.dp))

        // Only show tabs and categories when not searching
        if (state.searchQuery.isBlank()) {
            // Main Discovery Feature Tabs (Trending, Top Rated, Airing, Recommendations)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DiscoverTabChip(
                    icon = Icons.Default.LocalFireDepartment,
                    label = stringResource(R.string.tab_trending),
                    isSelected = state.selectedTab == DiscoverTab.TRENDING && state.selectedCategory == null,
                    tint = BrandOrange,
                    onClick = { viewModel.selectTab(DiscoverTab.TRENDING) },
                    modifier = Modifier.weight(1f)
                )
                DiscoverTabChip(
                    icon = Icons.Default.Star,
                    label = stringResource(R.string.tab_top_rated),
                    isSelected = state.selectedTab == DiscoverTab.TOP_RATED && state.selectedCategory == null,
                    tint = BrandYellow,
                    onClick = { viewModel.selectTab(DiscoverTab.TOP_RATED) },
                    modifier = Modifier.weight(1f)
                )
                DiscoverTabChip(
                    icon = Icons.Default.LiveTv,
                    label = stringResource(R.string.tab_airing),
                    isSelected = state.selectedTab == DiscoverTab.AIRING && state.selectedCategory == null,
                    tint = BrandBlue,
                    onClick = { viewModel.selectTab(DiscoverTab.AIRING) },
                    modifier = Modifier.weight(1f)
                )
                DiscoverTabChip(
                    icon = Icons.Default.AutoAwesome,
                    label = stringResource(R.string.tab_recommendations),
                    isSelected = state.selectedTab == DiscoverTab.RECOMMENDED && state.selectedCategory == null,
                    tint = BrandCyan,
                    onClick = { viewModel.selectTab(DiscoverTab.RECOMMENDED) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Genre / Category Horizontal Selector
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categoriesList) { item ->
                    val isSelected = (item.key == "All" && state.selectedCategory == null) ||
                            (state.selectedCategory.equals(item.key, ignoreCase = true))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) BrandOrange.copy(alpha = 0.2f) else SurfaceDark,
                        border = if (isSelected) BorderStroke(1.dp, BrandOrange) else null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                viewModel.selectCategory(if (item.key == "All") null else item.key)
                            }
                    ) {
                        Text(
                            text = stringResource(item.labelRes),
                            color = if (isSelected) BrandOrange else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        when {
            state.isLoading -> {
                LoadingState(message = stringResource(R.string.loading_catalog))
            }
            state.errorMessage != null -> {
                ErrorState(
                    message = state.errorMessage ?: stringResource(R.string.error_load_catalog),
                    onRetry = viewModel::loadDiscoverData
                )
            }
            state.searchQuery.isNotBlank() -> {
                // Search Results Grid
                if (state.searchResults.isEmpty() && !state.isSearching) {
                    EmptyState(
                        title = stringResource(R.string.empty_search_title),
                        subtitle = stringResource(R.string.empty_search_subtitle, state.searchQuery)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 96.dp)
                    ) {
                        items(state.searchResults, key = { it.id }) { anime ->
                            AnimeGridCard(
                                anime = anime,
                                onAnimeClick = { onAnimeClick(anime.id) },
                                onAddToWatchlist = {
                                    viewModel.addToWatchlist(anime)
                                    Toast.makeText(context, context.getString(R.string.msg_added_to_watchlist), Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
            state.selectedCategory != null -> {
                // Category Filter View
                if (state.isCategoryLoading) {
                    LoadingState(message = stringResource(R.string.loading_category, state.selectedCategory ?: ""))
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 96.dp)
                    ) {
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = stringResource(R.string.label_category_prefix, state.selectedCategory ?: ""),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        items(state.categoryAnime, key = { it.id }) { anime ->
                            AnimeGridCard(
                                anime = anime,
                                onAnimeClick = { onAnimeClick(anime.id) },
                                onAddToWatchlist = {
                                    viewModel.addToWatchlist(anime)
                                    Toast.makeText(context, context.getString(R.string.msg_added_to_watchlist), Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
            state.selectedTab == DiscoverTab.TOP_RATED -> {
                // Top Rated Tab View
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = stringResource(R.string.section_top_anime),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(state.topRatedAnime, key = { it.id }) { anime ->
                        AnimeGridCard(
                            anime = anime,
                            onAnimeClick = { onAnimeClick(anime.id) },
                            onAddToWatchlist = {
                                viewModel.addToWatchlist(anime)
                                Toast.makeText(context, context.getString(R.string.msg_added_to_watchlist), Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
            state.selectedTab == DiscoverTab.AIRING -> {
                // Airing Tab View
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = stringResource(R.string.section_airing_now),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(state.airingAnime, key = { it.id }) { anime ->
                        AnimeGridCard(
                            anime = anime,
                            onAnimeClick = { onAnimeClick(anime.id) },
                            onAddToWatchlist = {
                                viewModel.addToWatchlist(anime)
                                Toast.makeText(context, context.getString(R.string.msg_added_to_watchlist), Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
            state.selectedTab == DiscoverTab.RECOMMENDED -> {
                // Recommendations Tab View
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = stringResource(R.string.section_recommendations),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(state.recommendations, key = { it.anime.id }) { rec ->
                        AnimeGridCard(
                            anime = rec.anime,
                            onAnimeClick = { onAnimeClick(rec.anime.id) },
                            onAddToWatchlist = {
                                viewModel.addToWatchlist(rec.anime)
                                Toast.makeText(context, context.getString(R.string.msg_added_to_watchlist), Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
            else -> {
                // Default: TRENDING View with Highlights, Recommendations, and Top Anime
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    // Trending Highlights Carousel
                    if (state.trendingAnime.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = BrandOrange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(R.string.section_trending_now),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = TextPrimary
                                    )
                                }

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(vertical = 6.dp)
                                ) {
                                    itemsIndexed(state.trendingAnime.take(10), key = { _, anime -> anime.id }) { index, anime ->
                                        TrendingHeroCard(
                                            anime = anime,
                                            rank = index + 1,
                                            onClick = { onAnimeClick(anime.id) },
                                            onAdd = {
                                                viewModel.addToWatchlist(anime)
                                                Toast.makeText(context, context.getString(R.string.msg_added_to_watchlist), Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }

                    // Smart Recommendations Carousel
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
                                        text = stringResource(R.string.section_recommendations),
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
                                                Toast.makeText(context, context.getString(R.string.msg_added_to_watchlist), Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }

                    // Top Anime Section Title
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = stringResource(R.string.section_top_anime),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    // Top Anime Grid Items
                    items(state.topAnime, key = { it.id }) { anime ->
                        AnimeGridCard(
                            anime = anime,
                            onAnimeClick = { onAnimeClick(anime.id) },
                            onAddToWatchlist = {
                                viewModel.addToWatchlist(anime)
                                Toast.makeText(context, context.getString(R.string.msg_added_to_watchlist), Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiscoverTabChip(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) tint.copy(alpha = 0.2f) else SurfaceDark,
        border = if (isSelected) BorderStroke(1.dp, tint) else null,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) tint else TextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) tint else TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TrendingHeroCard(
    anime: Anime,
    rank: Int,
    onClick: () -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(180.dp)
            .height(240.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.displayTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            // Rank Badge Top-Start
            Surface(
                shape = RoundedCornerShape(bottomEnd = 10.dp),
                color = if (rank <= 3) BrandOrange else SurfaceDark.copy(alpha = 0.85f),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = "#$rank",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Quick Add Icon Top-End
            IconButton(
                onClick = onAdd,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(28.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = "Add",
                            tint = BrandOrange,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Title and Details at Bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                Text(
                    text = anime.displayTitle,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (anime.score != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = BrandYellow,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = String.format("%.1f", anime.score),
                                color = BrandYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "• ${anime.format.name}",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
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
                            text = "${rec.matchPercentage}%",
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

            val reasonText = when {
                rec.matchedGenres.isNotEmpty() && rec.matchedStudios.isNotEmpty() ->
                    stringResource(R.string.rec_reason_genres_studio, rec.matchedGenres.take(2).joinToString(" & "), rec.matchedStudios.first())
                rec.matchedGenres.isNotEmpty() ->
                    stringResource(R.string.rec_reason_genres, rec.matchedGenres.take(2).joinToString(" & "))
                else ->
                    stringResource(R.string.rec_reason_popular)
            }

            Text(
                text = reasonText,
                fontSize = 10.sp,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 13.sp
            )
        }
    }
}
