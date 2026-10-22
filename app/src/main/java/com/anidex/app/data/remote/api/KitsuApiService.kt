package com.anidex.app.data.remote.api

import com.anidex.app.data.remote.dto.KitsuListResponse
import com.anidex.app.data.remote.dto.KitsuSingleResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KitsuApiService {

    @GET("trending/anime")
    suspend fun getTrendingAnime(
        @Query("limit") limit: Int = 20
    ): KitsuListResponse

    @GET("anime")
    suspend fun getTopAnime(
        @Query("sort") sort: String = "-userCount",
        @Query("page[limit]") limit: Int = 20,
        @Query("page[offset]") offset: Int = 0
    ): KitsuListResponse

    @GET("anime")
    suspend fun getTopRatedAnime(
        @Query("sort") sort: String = "-averageRating",
        @Query("page[limit]") limit: Int = 20,
        @Query("page[offset]") offset: Int = 0
    ): KitsuListResponse

    @GET("anime")
    suspend fun getAiringAnime(
        @Query("filter[status]") status: String = "current",
        @Query("sort") sort: String = "-userCount",
        @Query("page[limit]") limit: Int = 20,
        @Query("page[offset]") offset: Int = 0
    ): KitsuListResponse

    @GET("anime")
    suspend fun getSeasonalAnime(
        @Query("filter[seasonYear]") year: Int,
        @Query("filter[season]") season: String,
        @Query("sort") sort: String = "-userCount",
        @Query("page[limit]") limit: Int = 20
    ): KitsuListResponse

    @GET("anime")
    suspend fun getAnimeByCategory(
        @Query("filter[categories]") category: String,
        @Query("sort") sort: String = "-userCount",
        @Query("page[limit]") limit: Int = 20,
        @Query("page[offset]") offset: Int = 0
    ): KitsuListResponse

    @GET("anime")
    suspend fun searchAnime(
        @Query("filter[text]") query: String,
        @Query("page[limit]") limit: Int = 20
    ): KitsuListResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(
        @Path("id") id: String
    ): KitsuSingleResponse
}
