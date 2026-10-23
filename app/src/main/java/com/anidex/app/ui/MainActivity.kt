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
import com.anidex.app.core.util.LocalizedActivityContext
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
            val activity = this@MainActivity

            val localizedContext = remember(currentLanguage) {
                applyLocale(currentLanguage)
                val configContext = LanguageManager.getLocalizedContext(activity, currentLanguage)
                LocalizedActivityContext(activity, configContext)
            }
            val configuration = remember(currentLanguage) {
                Configuration(activity.resources.configuration).apply {
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

    private fun applyLocale(langCode: String) {
        val locale = when (langCode) {
            LanguageManager.LANG_INDONESIAN -> Locale("in")
            else -> Locale.ENGLISH
        }
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
