package com.anidex.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.anidex.app.core.common.Resource
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

    private val prefs: SharedPreferences = context.getSharedPreferences("anidex_user_prefs", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override val isOnboardingCompleted: Boolean
        get() = prefs.getBoolean("onboarding_done", false)

    override fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("onboarding_done", completed).apply()
    }

    init {
        val savedEmail = prefs.getString("user_email", null)
        val savedName = prefs.getString("user_name", null)
        if (!savedEmail.isNullOrBlank() && !savedName.isNullOrBlank()) {
            _currentUser.value = User(
                id = "usr_1",
                name = savedName,
                email = savedEmail,
                avatarUrl = null,
                bio = prefs.getString("user_bio", "Otaku & Anime Collector") ?: "",
                joinedDate = prefs.getString("user_joined", "Oktober 2026") ?: "Oktober 2026"
            )
        }
    }

    override suspend fun login(email: String, pass: String): Resource<User> {
        delay(800) // Simulates network request
        if (email.isBlank() || pass.isBlank()) {
            return Resource.Error("Email dan password tidak boleh kosong.")
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resource.Error("Format email tidak valid.")
        }
        if (pass.length < 6) {
            return Resource.Error("Password minimal 6 karakter.")
        }

        val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        val user = User(
            id = "usr_${System.currentTimeMillis() % 1000}",
            name = name,
            email = email,
            avatarUrl = null,
            bio = "Anime Otaku & Tracker",
            joinedDate = "Oktober 2026"
        )

        prefs.edit()
            .putString("user_email", user.email)
            .putString("user_name", user.name)
            .putString("user_bio", user.bio)
            .putString("user_joined", user.joinedDate)
            .apply()

        _currentUser.value = user
        return Resource.Success(user)
    }

    override suspend fun register(name: String, email: String, pass: String): Resource<User> {
        delay(900)
        if (name.isBlank()) return Resource.Error("Nama tidak boleh kosong.")
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resource.Error("Email tidak valid.")
        }
        if (pass.length < 6) {
            return Resource.Error("Password minimal 6 karakter.")
        }

        val user = User(
            id = "usr_${System.currentTimeMillis() % 1000}",
            name = name,
            email = email,
            avatarUrl = null,
            bio = "Pengguna Baru AniDex",
            joinedDate = "Oktober 2026"
        )

        prefs.edit()
            .putString("user_email", user.email)
            .putString("user_name", user.name)
            .putString("user_bio", user.bio)
            .putString("user_joined", user.joinedDate)
            .apply()

        _currentUser.value = user
        return Resource.Success(user)
    }

    override suspend fun forgotPassword(email: String): Resource<String> {
        delay(800)
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resource.Error("Masukkan email yang terdaftar dengan benar.")
        }
        return Resource.Success("Tautan reset password telah dikirim ke $email. Silakan periksa inbox atau spam.")
    }

    override suspend fun logout() {
        prefs.edit()
            .remove("user_email")
            .remove("user_name")
            .remove("user_bio")
            .apply()
        _currentUser.value = null
    }

    override suspend fun updateProfile(name: String, bio: String): Resource<User> {
        val current = _currentUser.value ?: return Resource.Error("Sesi tidak ditemukan.")
        val updated = current.copy(name = name, bio = bio)
        prefs.edit()
            .putString("user_name", updated.name)
            .putString("user_bio", updated.bio)
            .apply()
        _currentUser.value = updated
        return Resource.Success(updated)
    }
}

