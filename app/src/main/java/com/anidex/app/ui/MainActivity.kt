package com.anidex.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.anidex.app.domain.repository.AuthRepository
import com.anidex.app.ui.navigation.MainAppScaffold
import com.anidex.app.ui.theme.AniDexTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AniDexTheme {
                MainAppScaffold(authRepository = authRepository)
            }
        }
    }
}
