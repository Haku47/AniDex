package com.anidex.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Pure Black AMOLED Theme (100% Pure Black #000000 for True AMOLED Depth)
val DeepBackground = Color(0xFF000000)      // Pure pitch black background
val SurfaceDark = Color(0xFF0C0D13)         // Very deep elevated card surface
val SurfaceVariantDark = Color(0xFF151822)  // Container & chips
val CardBorder = Color(0xFF202432)          // Crisp subtle border

// Mascot Logo Colors (Extracted from the new Fox & Letter 'A' Mascot)
val BrandOrange = Color(0xFFFF6E2E)         // Vibrant fiery fox coat & "Ani"
val BrandOrangeLight = Color(0xFFFF8E3C)
val BrandRed = Color(0xFFFF385C)            // Fiery tail gradient
val BrandBlue = Color(0xFF0091FF)           // Electric blue right leg of 'A' & "Dex"
val BrandCyan = Color(0xFF00D2D3)           // Cyber aqua accents
val BrandYellow = Color(0xFFFFB800)         // Amber star rating

// High-Contrast AMOLED Typography
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF98A2B3)
val TextTertiary = Color(0xFF5F6876)

// Functional
val SuccessGreen = Color(0xFF2ED573)
val ErrorRed = Color(0xFFFF4757)

// Signature Gradients
val LogoFireGradient = Brush.horizontalGradient(
    colors = listOf(BrandOrange, BrandRed)
)

val LogoIceGradient = Brush.horizontalGradient(
    colors = listOf(BrandCyan, BrandBlue)
)

val LogoBrandDuoGradient = Brush.horizontalGradient(
    colors = listOf(BrandOrange, BrandRed, BrandBlue)
)
