package com.anidex.app.data.repository

import com.anidex.app.core.common.DispatcherProvider
import com.anidex.app.core.common.Resource
import com.anidex.app.data.local.dao.AnimeDao
import com.anidex.app.data.mapper.AnimeMapper.toDomain
import com.anidex.app.data.mapper.AnimeMapper.toEntity
import com.anidex.app.data.remote.api.KitsuApiService
import com.anidex.app.domain.model.Anime
import com.anidex.app.domain.repository.AnimeRepository
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val api: KitsuApiService,
    private val animeDao: AnimeDao,
    private val dispatchers: DispatcherProvider
) : AnimeRepository {

    override suspend fun getAnimeDetail(animeId: Int): Resource<Anime> {
        return withContext(dispatchers.io) {
            try {
                val response = api.getAnimeDetail(animeId.toString())
                val animeData = response.data
                if (animeData != null) {
                    val domainAnime = animeData.toDomain()
                    animeDao.insertAnime(domainAnime.toEntity())
                    Resource.Success(domainAnime)
                } else {
                    val cached = animeDao.getAnimeById(animeId)?.toDomain()
                    if (cached != null) {
                        Resource.Success(cached)
                    } else {
                        Resource.Error("Anime dengan ID $animeId tidak ditemukan.")
                    }
                }
            } catch (e: HttpException) {
                val cached = animeDao.getAnimeById(animeId)?.toDomain()
                if (cached != null) {
                    Resource.Success(cached)
                } else {
                    Resource.Error("API error (HTTP ${e.code()}): ${e.message()}", e)
                }
            } catch (e: IOException) {
                val cached = animeDao.getAnimeById(animeId)?.toDomain()
                if (cached != null) {
                    Resource.Success(cached)
                } else {
                    Resource.Error("Koneksi jaringan terputus. Menampilkan data lokal jika ada.", e)
                }
            } catch (e: Exception) {
                Resource.Error("Terjadi kesalahan: ${e.localizedMessage ?: "Unknown error"}", e)
            }
        }
    }

    override suspend fun getTopAnime(page: Int, limit: Int): Resource<List<Anime>> {
        return withContext(dispatchers.io) {
            try {
                val safeLimit = limit.coerceIn(1, 20)
                val offset = (page - 1) * safeLimit
                val response = api.getTopAnime(sort = "-userCount", limit = safeLimit, offset = offset)
                val domainList = response.data.map { it.toDomain() }

                if (domainList.isNotEmpty()) {
                    animeDao.insertAnimes(domainList.map { it.toEntity() })
                }
                Resource.Success(domainList)
            } catch (e: HttpException) {
                Resource.Error("API error HTTP ${e.code()}: ${e.message()}", e)
            } catch (e: IOException) {
                Resource.Error("Gagal terhubung ke API anime. Periksa koneksi internet.", e)
            } catch (e: Exception) {
                Resource.Error("Gagal memuat anime populer: ${e.localizedMessage}", e)
            }
        }
    }

    override suspend fun getSeasonalAnime(year: Int, season: String, page: Int): Resource<List<Anime>> {
        return withContext(dispatchers.io) {
            try {
                val response = api.getSeasonalAnime(year = year, season = season.lowercase(), limit = 20)
                val list = response.data.map { it.toDomain() }
                Resource.Success(list)
            } catch (e: Exception) {
                Resource.Error("Gagal memuat seasonal anime: ${e.localizedMessage}", e)
            }
        }
    }

    override suspend fun getAiringSchedules(dayOfWeek: String?): Resource<List<Anime>> {
        return withContext(dispatchers.io) {
            try {
                // Fetch currently airing anime (Kitsu max limit = 20)
                val response = api.getAiringAnime(status = "current", limit = 20)
                val list = response.data.map { it.toDomain() }
                Resource.Success(list)
            } catch (e: HttpException) {
                Resource.Error("Gagal memuat jadwal tayang (HTTP ${e.code()}): ${e.message()}", e)
            } catch (e: Exception) {
                Resource.Error("Gagal memuat jadwal anime tayang: ${e.localizedMessage}", e)
            }
        }
    }

    override suspend fun searchAnime(query: String, page: Int): Resource<List<Anime>> {
        return withContext(dispatchers.io) {
            try {
                val response = api.searchAnime(query = query, limit = 20)
                val list = response.data.map { it.toDomain() }
                Resource.Success(list)
            } catch (e: Exception) {
                Resource.Error("Gagal mencari anime '$query': ${e.localizedMessage}", e)
            }
        }
    }
}
