package com.anidex.app.domain.repository

import com.anidex.app.core.common.Resource
import com.anidex.app.domain.model.Anime

interface AnimeRepository {
    suspend fun getAnimeDetail(animeId: Int): Resource<Anime>
    suspend fun getTopAnime(page: Int = 1, limit: Int = 25): Resource<List<Anime>>
    suspend fun getSeasonalAnime(year: Int, season: String, page: Int = 1): Resource<List<Anime>>
    suspend fun getAiringSchedules(dayOfWeek: String? = null): Resource<List<Anime>>
    suspend fun searchAnime(query: String, page: Int = 1): Resource<List<Anime>>
}

