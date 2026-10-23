package com.anidex.app.domain.repository

import com.anidex.app.core.common.Resource
import com.anidex.app.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<User?>
    val isOnboardingCompleted: Boolean
    fun setOnboardingCompleted(completed: Boolean)
    suspend fun login(email: String, pass: String): Resource<User>
    suspend fun register(name: String, email: String, pass: String): Resource<User>
    suspend fun forgotPassword(email: String): Resource<String>
    suspend fun logout()
    suspend fun loginAsGuest(): Resource<User>
    suspend fun updateProfile(name: String, bio: String, avatarUrl: String? = null): Resource<User>
}
