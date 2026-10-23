package com.anidex.app.core.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class LocalizedActivityContext(
    base: Activity,
    private val localizedContext: Context
) : ContextWrapper(base) {
    override fun getResources(): Resources {
        return localizedContext.resources
    }
}

object LanguageManager {
    private const val PREFS_NAME = "anidex_language_prefs"
    private const val KEY_LANGUAGE = "app_language"

    const val LANG_ENGLISH = "en"
    const val LANG_INDONESIAN = "in"

    private val _currentLanguage = MutableStateFlow(LANG_ENGLISH)
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val savedLang = prefs?.getString(KEY_LANGUAGE, LANG_ENGLISH) ?: LANG_ENGLISH
            _currentLanguage.value = savedLang
        }
    }

    fun setLanguage(context: Context, langCode: String) {
        val validCode = if (langCode == LANG_INDONESIAN) LANG_INDONESIAN else LANG_ENGLISH
        prefs?.edit()?.putString(KEY_LANGUAGE, validCode)?.apply()
        _currentLanguage.value = validCode
    }

    fun getLocalizedContext(context: Context, langCode: String): Context {
        val locale = when (langCode) {
            LANG_INDONESIAN -> Locale("in")
            else -> Locale.ENGLISH
        }
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        return context.createConfigurationContext(config)
    }

    fun getDisplayName(langCode: String): String {
        return when (langCode) {
            LANG_INDONESIAN -> "Bahasa Indonesia"
            else -> "English (Default)"
        }
    }
}
