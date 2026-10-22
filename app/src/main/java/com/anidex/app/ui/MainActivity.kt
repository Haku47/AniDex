package com.anidex.app.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.anidex.app.core.util.LanguageManager
import com.anidex.app.domain.repository.AuthRepository
import com.anidex.app.ui.navigation.MainAppScaffold
import com.anidex.app.ui.theme.AniDexTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.init(this)
        enableEdgeToEdge()
        setContent {
            val currentLanguage by LanguageManager.currentLanguage.collectAsState()
            val context = LocalContext.current

            val localizedContext = remember(currentLanguage) {
                LanguageManager.getLocalizedContext(context, currentLanguage)
            }
            val configuration = remember(currentLanguage) {
                Configuration(context.resources.configuration).apply {
                    val locale = when (currentLanguage) {
                        LanguageManager.LANG_INDONESIAN -> Locale("in")
                        else -> Locale.ENGLISH
                    }
                    setLocale(locale)
                }
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalConfiguration provides configuration
            ) {
                AniDexTheme {
                    MainAppScaffold(authRepository = authRepository)
                }
            }
        }
    }
}
