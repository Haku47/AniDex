package com.anidex.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.anidex.app.R
import com.anidex.app.ui.theme.BrandBlue
import com.anidex.app.ui.theme.BrandCyan
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.BrandRed
import com.anidex.app.ui.theme.SurfaceDark

data class AvatarPreset(
    val id: String,
    val name: String,
    val description: String,
    val ringColor: Color,
    val icon: ImageVector? = null,
    val isMascotLogo: Boolean = false
)

val AvailableAvatarPresets = listOf(
    AvatarPreset(
        id = "mascot_fox",
        name = "Kitsune Maskot",
        description = "Maskot Resmi AniDex",
        ringColor = BrandOrange,
        isMascotLogo = true
    ),
    AvatarPreset(
        id = "avatar_shinobi",
        name = "Shadow Shinobi",
        description = "Ninja Bayangan Gesit",
        ringColor = BrandRed,
        icon = Icons.Default.Security
    ),
    AvatarPreset(
        id = "avatar_cyber",
        name = "Cyber Otaku",
        description = "Netrunner & Sci-Fi",
        ringColor = BrandCyan,
        icon = Icons.Default.SmartToy
    ),
    AvatarPreset(
        id = "avatar_sorcerer",
        name = "Star Sorcerer",
        description = "Penyihir Bintang Fantasi",
        ringColor = Color(0xFFBA68C8),
        icon = Icons.Default.AutoAwesome
    ),
    AvatarPreset(
        id = "avatar_fighter",
        name = "Shonen Hero",
        description = "Semangat Pantang Menyerah",
        ringColor = Color(0xFFFFB74D),
        icon = Icons.Default.Bolt
    ),
    AvatarPreset(
        id = "avatar_pilot",
        name = "Mecha Pilot",
        description = "Pilot Tempur Mecha",
        ringColor = BrandBlue,
        icon = Icons.Default.RocketLaunch
    )
)

fun getAvatarPreset(id: String?): AvatarPreset {
    return AvailableAvatarPresets.find { it.id == id } ?: AvailableAvatarPresets.first()
}

@Composable
fun UserAvatarDisplay(
    avatarKeyOrUrl: String?,
    size: Dp = 100.dp,
    borderWidth: Dp = 2.5.dp,
    modifier: Modifier = Modifier
) {
    val isNetworkUrl = avatarKeyOrUrl != null &&
            (avatarKeyOrUrl.startsWith("http://") || avatarKeyOrUrl.startsWith("https://"))

    val preset = getAvatarPreset(avatarKeyOrUrl)

    val borderBrush = if (preset.isMascotLogo) {
        Brush.linearGradient(listOf(BrandOrange, BrandBlue))
    } else {
        Brush.linearGradient(listOf(preset.ringColor, preset.ringColor.copy(alpha = 0.6f)))
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0xFF0D1117))
            .border(
                width = borderWidth,
                brush = borderBrush,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isNetworkUrl) {
            SubcomposeAsyncImage(
                model = avatarKeyOrUrl,
                contentDescription = "Avatar Pengguna",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = BrandOrange,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(size * 0.4f)
                        )
                    }
                },
                error = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_anidex),
                        contentDescription = "Fallback Mascot",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(size * 0.76f)
                            .clip(CircleShape)
                    )
                }
            )
        } else if (preset.isMascotLogo) {
            Image(
                painter = painterResource(id = R.drawable.logo_anidex),
                contentDescription = "AniDex Mascot Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(size * 0.76f)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(preset.ringColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                preset.icon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = preset.name,
                        tint = preset.ringColor,
                        modifier = Modifier.size(size * 0.5f)
                    )
                }
            }
        }
    }
}
