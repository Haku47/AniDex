package com.anidex.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anidex.app.R
import com.anidex.app.domain.model.WatchTimeSummary
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.BrandRed
import com.anidex.app.ui.theme.BrandYellow
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.TextPrimary
import com.anidex.app.ui.theme.TextSecondary

@Composable
fun WatchTimeHeaderCard(
    summary: WatchTimeSummary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            BrandOrange,
                            BrandRed,
                            BrandBlue
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.label_total_watch_time).uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    val completionPercent = (summary.completionRate * 100).toInt()
                    Text(
                        text = stringResource(R.string.label_completed_percent, completionPercent),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = BrandCyan
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                val daysUnit = stringResource(R.string.unit_days, summary.days)
                val hoursUnit = stringResource(R.string.unit_hours_short, summary.hours)
                val minutesUnit = stringResource(R.string.unit_minutes_short, summary.minutes)
                val timeParts = mutableListOf<String>()
                if (summary.days > 0) timeParts.add(daysUnit)
                if (summary.hours > 0) timeParts.add(hoursUnit)
                if (summary.minutes > 0 || timeParts.isEmpty()) timeParts.add(minutesUnit)

                // Big duration display
                Text(
                    text = timeParts.joinToString(", "),
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Metric mini badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatPill(
                        icon = Icons.Default.PlayCircle,
                        label = stringResource(R.string.label_episodes),
                        value = "${summary.totalEpisodesWatched} Ep",
                        modifier = Modifier.weight(1f)
                    )

                    StatPill(
                        icon = Icons.Default.HourglassTop,
                        label = stringResource(R.string.stats_total_hours),
                        value = stringResource(R.string.unit_hours, String.format("%.1f", summary.totalHoursPrecise)),
                        modifier = Modifier.weight(1f)
                    )

                    summary.meanUserRating?.let { rating ->
                        StatPill(
                            icon = Icons.Default.Star,
                            label = stringResource(R.string.label_mean_score),
                            value = String.format("%.1f", rating),
                            tint = BrandYellow,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    icon: ImageVector,
    label: String,
    value: String,
    tint: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(DeepBackground.copy(alpha = 0.45f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = " $label",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.White
            )
        }
    }
}
