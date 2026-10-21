package com.anidex.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Pure Black AMOLED Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = BrandOrange,
    onPrimary = TextPrimary,
    primaryContainer = BrandOrange.copy(alpha = 0.15f),
    onPrimaryContainer = BrandOrangeLight,
    secondary = BrandBlue,
    onSecondary = TextPrimary,
    secondaryContainer = BrandBlue.copy(alpha = 0.15f),
    onSecondaryContainer = BrandCyan,
    tertiary = BrandCyan,
    onTertiary = DeepBackground,
    background = DeepBackground,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = CardBorder,
    error = BrandRed
)

@Composable
fun AniDexTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeepBackground.toArgb()
            window.navigationBarColor = DeepBackground.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
