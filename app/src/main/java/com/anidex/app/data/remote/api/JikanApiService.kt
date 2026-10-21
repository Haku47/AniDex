package com.anidex.app.data.remote.api

import com.anidex.app.data.remote.dto.AnimeDto
import com.anidex.app.data.remote.dto.JikanResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {

    @GET("v4/top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): JikanResponse<List<AnimeDto>>

    @GET("v4/seasons/{year}/{season}")
    suspend fun getSeasonalAnime(
        @Path("year") year: Int,
        @Path("season") season: String,
        @Query("page") page: Int = 1
    ): JikanResponse<List<AnimeDto>>

    @GET("v4/schedules")
    suspend fun getSchedules(
        @Query("filter") filterDay: String? = null
    ): JikanResponse<List<AnimeDto>>

    @GET("v4/anime/{id}/full")
    suspend fun getAnimeDetail(
        @Path("id") animeId: Int
    ): JikanResponse<AnimeDto>

    @GET("v4/anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): JikanResponse<List<AnimeDto>>
}

