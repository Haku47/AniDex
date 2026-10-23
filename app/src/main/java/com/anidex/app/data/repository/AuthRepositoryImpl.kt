package com.anidex.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import com.anidex.app.R
import com.anidex.app.core.common.Resource
import com.anidex.app.core.util.LanguageManager
import com.anidex.app.domain.model.User
import com.anidex.app.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("anidex_auth_prefs", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override val isOnboardingCompleted: Boolean
        get() = prefs.getBoolean("onboarding_completed", false)

    override fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("onboarding_completed", completed).apply()
    }

    init {
        loadSavedUser()
    }

    private fun loadSavedUser() {
        val savedEmail = prefs.getString("user_email", null)
        val savedName = prefs.getString("user_name", null)
        val savedAvatar = prefs.getString("user_avatar", "kitsune")
        val savedBio = prefs.getString("user_bio", "Anime Tracker Enthusiast")
        val savedJoined = prefs.getString("user_joined", "October 2026") ?: "October 2026"

        if (!savedEmail.isNullOrBlank() && !savedName.isNullOrBlank()) {
            _currentUser.value = User(
                id = prefs.getString("user_id", "usr_1") ?: "usr_1",
                name = savedName,
                email = savedEmail,
                avatarUrl = savedAvatar,
                bio = savedBio ?: "Anime Tracker Enthusiast",
                joinedDate = savedJoined
            )
        } else if (!savedName.isNullOrBlank()) {
            _currentUser.value = User(
                id = "guest_001",
                name = savedName,
                email = "guest@anidex.app",
                avatarUrl = savedAvatar,
                bio = savedBio ?: "Anime Tracker Enthusiast",
                joinedDate = savedJoined
            )
        }
    }

    private fun saveUserToPrefs(user: User) {
        prefs.edit()
            .putString("user_id", user.id)
            .putString("user_name", user.name)
            .putString("user_email", user.email)
            .putString("user_avatar", user.avatarUrl)
            .putString("user_bio", user.bio)
            .putString("user_joined", user.joinedDate)
            .apply()
    }

    private fun getLocalizedText(@StringRes resId: Int, vararg formatArgs: Any): String {
        val localizedCtx = LanguageManager.getLocalizedContext(context, LanguageManager.currentLanguage.value)
        return localizedCtx.getString(resId, *formatArgs)
    }

    override suspend fun login(email: String, pass: String): Resource<User> {
        delay(900)
        if (email.isBlank() || pass.isBlank()) {
            return Resource.Error(getLocalizedText(R.string.auth_err_empty_fields))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resource.Error(getLocalizedText(R.string.auth_err_invalid_email))
        }
        if (pass.length < 6) {
            return Resource.Error(getLocalizedText(R.string.auth_err_password_too_short))
        }

        val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        val user = User(
            id = "usr_${System.currentTimeMillis() % 10000}",
            name = name,
            email = email,
            avatarUrl = "kitsune",
            bio = "Anime Tracker Enthusiast",
            joinedDate = "October 2026"
        )

        saveUserToPrefs(user)
        _currentUser.value = user
        return Resource.Success(user)
    }

    override suspend fun register(name: String, email: String, pass: String): Resource<User> {
        delay(1000)
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            return Resource.Error(getLocalizedText(R.string.auth_err_all_fields_required))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resource.Error(getLocalizedText(R.string.auth_err_invalid_email))
        }
        if (pass.length < 6) {
            return Resource.Error(getLocalizedText(R.string.auth_err_password_too_short))
        }

        val user = User(
            id = "usr_${System.currentTimeMillis() % 10000}",
            name = name.trim(),
            email = email.trim(),
            avatarUrl = "kitsune",
            bio = "New AniDex User",
            joinedDate = "October 2026"
        )

        saveUserToPrefs(user)
        _currentUser.value = user
        return Resource.Success(user)
    }

    override suspend fun loginAsGuest(): Resource<User> {
        val guest = User(
            id = "guest_${System.currentTimeMillis() % 1000}",
            name = "Guest User",
            email = "guest@anidex.app",
            avatarUrl = "kitsune",
            bio = "Anime Tracker Enthusiast",
            joinedDate = "October 2026"
        )
        saveUserToPrefs(guest)
        _currentUser.value = guest
        return Resource.Success(guest)
    }

    override suspend fun forgotPassword(email: String): Resource<String> {
        delay(800)
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resource.Error(getLocalizedText(R.string.auth_err_invalid_email))
        }
        return Resource.Success(getLocalizedText(R.string.auth_reset_link_sent, email))
    }

    override suspend fun logout() {
        prefs.edit()
            .remove("user_id")
            .remove("user_email")
            .remove("user_name")
            .remove("user_avatar")
            .remove("user_bio")
            .remove("user_joined")
            .apply()
        _currentUser.value = null
    }

    override suspend fun updateProfile(name: String, bio: String, avatarUrl: String?): Resource<User> {
        val current = _currentUser.value ?: User(
            id = "guest_001",
            name = name.ifBlank { "Guest User" },
            email = "guest@anidex.app",
            avatarUrl = avatarUrl ?: "kitsune",
            bio = bio.ifBlank { "Anime Tracker Enthusiast" },
            joinedDate = "October 2026"
        )
        val updated = current.copy(
            name = name.ifBlank { current.name },
            bio = bio.ifBlank { current.bio },
            avatarUrl = avatarUrl ?: current.avatarUrl ?: "kitsune"
        )
        saveUserToPrefs(updated)
        _currentUser.value = updated
        return Resource.Success(updated)
    }
}
