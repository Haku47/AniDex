package com.anidex.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.anidex.app.R
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange

@Composable
fun UserAvatarBadge(
    avatarId: String?,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    ringWidth: Dp = 2.5.dp,
    showRing: Boolean = true
) {
    val gradientBrush = when (avatarId?.lowercase()) {
        "shinobi" -> Brush.linearGradient(listOf(Color(0xFFFF3366), Color(0xFF9C27B0)))
        "otaku" -> Brush.linearGradient(listOf(BrandCyan, BrandBlue))
        "sorcerer" -> Brush.linearGradient(listOf(Color(0xFFB388FF), Color(0xFFE040FB)))
        else -> Brush.linearGradient(listOf(BrandOrange, BrandBlue))
    }

    val boxModifier = modifier
        .size(size)
        .clip(CircleShape)
        .background(Color(0xFF000000))
        .then(
            if (showRing) {
                Modifier.border(width = ringWidth, brush = gradientBrush, shape = CircleShape)
            } else {
                Modifier
            }
        )

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center
    ) {
        val innerSize = size * 0.72f
        val isCustomImage = avatarId != null && (
            avatarId.startsWith("http", ignoreCase = true) ||
            avatarId.startsWith("content:", ignoreCase = true) ||
            avatarId.startsWith("file:", ignoreCase = true) ||
            avatarId.startsWith("/") ||
            (!avatarId.equals("kitsune", ignoreCase = true) &&
             !avatarId.equals("shinobi", ignoreCase = true) &&
             !avatarId.equals("otaku", ignoreCase = true) &&
             !avatarId.equals("sorcerer", ignoreCase = true))
        )

        when {
            isCustomImage -> {
                AsyncImage(
                    model = avatarId,
                    contentDescription = "Avatar Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(innerSize)
                        .clip(CircleShape)
                )
            }
            avatarId.equals("shinobi", ignoreCase = true) -> {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = "Shadow Shinobi Avatar",
                    tint = Color(0xFFFF3366),
                    modifier = Modifier.size(innerSize * 0.75f)
                )
            }
            avatarId.equals("otaku", ignoreCase = true) -> {
                Icon(
                    imageVector = Icons.Filled.SmartToy,
                    contentDescription = "Cyber Otaku Avatar",
                    tint = BrandCyan,
                    modifier = Modifier.size(innerSize * 0.75f)
                )
            }
            avatarId.equals("sorcerer", ignoreCase = true) -> {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = "Star Sorcerer Avatar",
                    tint = Color(0xFFB388FF),
                    modifier = Modifier.size(innerSize * 0.75f)
                )
            }
            else -> {
                // Default: Kitsune Maskot Logo Fox
                Image(
                    painter = painterResource(id = R.drawable.logo_anidex),
                    contentDescription = "Kitsune Mascot Avatar",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(innerSize)
                )
            }
        }
    }
}

