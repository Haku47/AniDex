package com.anidex.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anidex.app.domain.repository.AuthRepository
import com.anidex.app.ui.screens.auth.AuthViewModel
import com.anidex.app.ui.screens.auth.ForgotPasswordScreen
import com.anidex.app.ui.screens.auth.LoginScreen
import com.anidex.app.ui.screens.auth.RegisterScreen
import com.anidex.app.ui.screens.detail.AnimeDetailScreen
import com.anidex.app.ui.screens.detail.AnimeDetailViewModel
import com.anidex.app.ui.screens.discover.DiscoverScreen
import com.anidex.app.ui.screens.discover.DiscoverViewModel
import com.anidex.app.ui.screens.onboarding.OnboardingScreen
import com.anidex.app.ui.screens.profile.EditProfileScreen
import com.anidex.app.ui.screens.profile.ProfileScreen
import com.anidex.app.ui.screens.profile.ProfileViewModel
import com.anidex.app.ui.screens.schedule.ScheduleScreen
import com.anidex.app.ui.screens.schedule.ScheduleViewModel
import com.anidex.app.ui.screens.settings.SettingsScreen
import com.anidex.app.ui.screens.splash.SplashScreen
import com.anidex.app.ui.screens.stats.StatsScreen
import com.anidex.app.ui.screens.stats.StatsViewModel
import com.anidex.app.ui.screens.watchlist.WatchlistScreen
import com.anidex.app.ui.screens.watchlist.WatchlistViewModel
import com.anidex.app.ui.theme.BrandOrange
import com.anidex.app.ui.theme.CardBorder
import com.anidex.app.ui.theme.DeepBackground
import com.anidex.app.ui.theme.TextSecondary

@Composable
fun MainAppScaffold(
    authRepository: AuthRepository,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show Bottom Bar only on the 5 main bottom-nav tabs
    val bottomBarRoutes = BottomNavItems.map { it.route }
    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBackground),
        containerColor = DeepBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                androidx.compose.foundation.layout.Column {
                    HorizontalDivider(color = CardBorder.copy(alpha = 0.6f), thickness = 0.5.dp)
                    NavigationBar(
                        containerColor = DeepBackground,
                        tonalElevation = 0.dp
                    ) {
                        BottomNavItems.forEach { screen ->
                            val selected = currentRoute == screen.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (currentRoute != screen.route) {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    screen.selectedIcon?.let { selIcon ->
                                        val icon = if (selected) selIcon else screen.unselectedIcon ?: selIcon
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = screen.title
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = if (screen.titleRes != 0) stringResource(screen.titleRes) else screen.title,
                                        fontSize = 10.sp
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = BrandOrange,
                                    indicatorColor = BrandOrange,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Splash Screen
            composable(Screen.Splash.route) {
                SplashScreen(
                    authRepository = authRepository,
                    onNavigate = { targetRoute ->
                        navController.navigate(targetRoute) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // 2. Onboarding Screen
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    authRepository = authRepository,
                    onFinish = { targetRoute ->
                        navController.navigate(targetRoute) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // 3. Login Screen
            composable(Screen.Login.route) {
                val viewModel: AuthViewModel = hiltViewModel()
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Discover.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                    onGuestLogin = {
                        navController.navigate(Screen.Discover.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // 4. Register Screen
            composable(Screen.Register.route) {
                val viewModel: AuthViewModel = hiltViewModel()
                RegisterScreen(
                    viewModel = viewModel,
                    onRegisterSuccess = {
                        navController.navigate(Screen.Discover.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onNavigateBackToLogin = { navController.popBackStack() }
                )
            }

            // 5. Forgot Password Screen
            composable(Screen.ForgotPassword.route) {
                val viewModel: AuthViewModel = hiltViewModel()
                ForgotPasswordScreen(
                    viewModel = viewModel,
                    onNavigateBackToLogin = { navController.popBackStack() }
                )
            }

            // 6. Main Tab: Discover
            composable(Screen.Discover.route) {
                val viewModel: DiscoverViewModel = hiltViewModel()
                DiscoverScreen(
                    viewModel = viewModel,
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.AnimeDetail.createRoute(animeId))
                    }
                )
            }

            // 7. Main Tab: Schedule
            composable(Screen.Schedule.route) {
                val viewModel: ScheduleViewModel = hiltViewModel()
                ScheduleScreen(
                    viewModel = viewModel,
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.AnimeDetail.createRoute(animeId))
                    }
                )
            }

            // 8. Main Tab: Watchlist
            composable(Screen.Watchlist.route) {
                val viewModel: WatchlistViewModel = hiltViewModel()
                WatchlistScreen(viewModel = viewModel)
            }

            // 9. Main Tab: Stats
            composable(Screen.Stats.route) {
                val viewModel: StatsViewModel = hiltViewModel()
                StatsScreen(viewModel = viewModel)
            }

            // 10. Main Tab: Profile
            composable(Screen.Profile.route) {
                val viewModel: ProfileViewModel = hiltViewModel()
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToEditProfile = {
                        navController.navigate(Screen.EditProfile.route) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // 11. Secondary: Anime Detail Screen
            composable(
                route = Screen.AnimeDetail.route,
                arguments = listOf(navArgument("animeId") { type = NavType.IntType })
            ) {
                val viewModel: AnimeDetailViewModel = hiltViewModel()
                AnimeDetailScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 12. Secondary: Settings Screen
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 13. Secondary: Edit Profile Screen
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    authRepository = authRepository,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
