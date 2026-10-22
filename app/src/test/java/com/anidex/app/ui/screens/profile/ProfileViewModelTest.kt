package com.anidex.app.ui.screens.profile

import com.anidex.app.core.common.DispatcherProvider
import com.anidex.app.core.common.Resource
import com.anidex.app.domain.model.User
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchlistEntry
import com.anidex.app.domain.repository.AuthRepository
import com.anidex.app.domain.repository.WatchlistRepository
import com.anidex.app.domain.usecase.CalculateWatchTimeUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val fakeAuthRepository = object : AuthRepository {
        private val _user = MutableStateFlow<User?>(
            User(
                id = "usr_1",
                name = "Ahmad Anime",
                email = "ahmad@example.com",
                avatarUrl = "mascot_fox",
                bio = "Otaku sejati",
                joinedDate = "Oktober 2026"
            )
        )
        override val currentUser: StateFlow<User?> = _user.asStateFlow()
        override val isOnboardingCompleted: Boolean = true
        override fun setOnboardingCompleted(completed: Boolean) {}

        override suspend fun login(email: String, pass: String): Resource<User> = Resource.Success(_user.value!!)
        override suspend fun register(name: String, email: String, pass: String): Resource<User> = Resource.Success(_user.value!!)
        override suspend fun forgotPassword(email: String): Resource<String> = Resource.Success("Sent")
        override suspend fun loginAsGuest(): Resource<User> {
            val guest = User("guest", "Pengguna Tamu", "tamu@anidex.app", null, "Bio Tamu", "Oktober 2026")
            _user.value = guest
            return Resource.Success(guest)
        }
        override suspend fun logout() {
            _user.value = null
        }
        override suspend fun updateProfile(name: String, bio: String, avatarUrl: String?): Resource<User> {
            if (name.isBlank()) return Resource.Error("Nama profil tidak boleh kosong.")
            val current = _user.value ?: return Resource.Error("Sesi profil tidak ditemukan.")
            val updated = current.copy(name = name, bio = bio, avatarUrl = avatarUrl)
            _user.value = updated
            return Resource.Success(updated)
        }
    }

    private val fakeWatchlistRepo = object : WatchlistRepository {
        override fun getWatchlistStream(): Flow<List<WatchlistEntry>> = flowOf(emptyList())
        override fun getWatchlistByStatusStream(status: WatchStatus): Flow<List<WatchlistEntry>> = flowOf(emptyList())
        override suspend fun getWatchlist(): List<WatchlistEntry> = emptyList()
        override suspend fun getWatchlistEntry(animeId: Int): WatchlistEntry? = null
        override suspend fun upsertWatchlistEntry(entry: WatchlistEntry) {}
        override suspend fun updateEpisodeProgress(animeId: Int, episodeWatched: Int) {}
        override suspend fun deleteWatchlistEntry(animeId: Int) {}
    }

    private val testDispatchers = object : DispatcherProvider {
        override val main: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
        override val default: CoroutineDispatcher = testDispatcher
        override val unconfined: CoroutineDispatcher = testDispatcher
    }

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val calculateWatchTimeUseCase = CalculateWatchTimeUseCase(fakeWatchlistRepo, testDispatchers)
        viewModel = ProfileViewModel(fakeAuthRepository, calculateWatchTimeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateProfile successfully updates user name bio and avatar`() = runTest(testDispatcher) {
        var callbackInvoked = false

        viewModel.updateProfile(
            name = "Luffy Gear 5",
            bio = "Aku akan menjadi Raja Bajak Laut!",
            avatarUrl = "avatar_fighter",
            onSuccess = { callbackInvoked = true }
        )

        advanceUntilIdle()

        assertTrue(callbackInvoked)
        val user = fakeAuthRepository.currentUser.value
        assertNotNull(user)
        assertEquals("Luffy Gear 5", user!!.name)
        assertEquals("Aku akan menjadi Raja Bajak Laut!", user.bio)
        assertEquals("avatar_fighter", user.avatarUrl)
    }

    @Test
    fun `updateProfile with blank name fails with error message`() = runTest(testDispatcher) {
        var callbackInvoked = false

        viewModel.updateProfile(
            name = "",
            bio = "Bio tetap",
            avatarUrl = "mascot_fox",
            onSuccess = { callbackInvoked = true }
        )

        advanceUntilIdle()

        assertTrue(!callbackInvoked)
        val user = fakeAuthRepository.currentUser.value
        assertEquals("Ahmad Anime", user!!.name) // unchanged
    }

    @Test
    fun `logout sets current user to null`() = runTest(testDispatcher) {
        var loggedOut = false

        viewModel.logout { loggedOut = true }
        advanceUntilIdle()

        assertTrue(loggedOut)
        assertNull(fakeAuthRepository.currentUser.value)
    }
}
