package com.anidex.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String = "",
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {
    // Top-Level / Auth Flows
    data object Splash : Screen(route = "splash")
    data object Onboarding : Screen(route = "onboarding")
    data object Login : Screen(route = "login")
    data object Register : Screen(route = "register")
    data object ForgotPassword : Screen(route = "forgot_password")

    // Main App Scaffold (Bottom Bar Tabs)
    data object Discover : Screen(
        route = "discover",
        title = "Jelajah",
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome
    )

    data object Schedule : Screen(
        route = "schedule",
        title = "Jadwal",
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth
    )

    data object Watchlist : Screen(
        route = "watchlist",
        title = "Watchlist",
        selectedIcon = Icons.Filled.VideoLibrary,
        unselectedIcon = Icons.Outlined.VideoLibrary
    )

    data object Stats : Screen(
        route = "stats",
        title = "Statistik",
        selectedIcon = Icons.Filled.Leaderboard,
        unselectedIcon = Icons.Outlined.Leaderboard
    )

    data object Profile : Screen(
        route = "profile",
        title = "Profil",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    // Secondary / Detail Screens
    data object AnimeDetail : Screen(route = "anime_detail/{animeId}") {
        fun createRoute(animeId: Int): String = "anime_detail/$animeId"
    }

    data object Settings : Screen(route = "settings")
    data object EditProfile : Screen(route = "edit_profile")
}

val BottomNavItems = listOf(
    Screen.Discover,
    Screen.Schedule,
    Screen.Watchlist,
    Screen.Stats,
    Screen.Profile
)
