package com.anidex.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.anidex.app.R
import com.anidex.app.domain.model.AiringSchedule
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.BrandYellow
import com.anidex.app.ui.theme.SurfaceDark
import com.anidex.app.ui.theme.SurfaceVariantDark
import com.anidex.app.ui.theme.TextPrimary
import com.anidex.app.ui.theme.TextSecondary

@Composable
fun AiringScheduleCard(
    schedule: AiringSchedule,
    modifier: Modifier = Modifier
) {
    val anime = schedule.anime

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Anime thumbnail
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.displayTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 70.dp, height = 96.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Local Time badge (Brand Blue background, Cyan text)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = BrandBlue.copy(alpha = 0.2f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = BrandCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = schedule.localFormattedTime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandCyan
                            )
                        }
                    }

                    // Countdown badge (Brand Orange)
                    val countdownFormatted = if (schedule.countdownMillis <= 0) {
                        stringResource(R.string.countdown_aired_or_soon)
                    } else {
                        val totalSeconds = schedule.countdownMillis / 1000
                        val days = totalSeconds / (24 * 3600)
                        val hours = (totalSeconds % (24 * 3600)) / 3600
                        val minutes = (totalSeconds % 3600) / 60
                        when {
                            days > 0 -> stringResource(R.string.countdown_in_days_hours_mins, days, hours, minutes)
                            hours > 0 -> stringResource(R.string.countdown_in_hours_mins, hours, minutes)
                            else -> stringResource(R.string.countdown_in_mins, minutes)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (schedule.isAiringToday) BrandOrange.copy(alpha = 0.25f) else SurfaceVariantDark
                    ) {
                        Text(
                            text = countdownFormatted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (schedule.isAiringToday) BrandOrange else TextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = anime.displayTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "JST: ${schedule.originalJstDay} ${schedule.originalJstTime}",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )

                    anime.score?.let { score ->
                        Text(
                            text = "• ★ ${String.format("%.1f", score)}",
                            fontSize = 10.sp,
                            color = BrandYellow,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                val genreList = anime.genres.take(2).joinToString(", ") { it.name }
                if (genreList.isNotEmpty()) {
                    Text(
                        text = genreList,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
