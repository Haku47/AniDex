package com.anidex.app.data.remote.api

import com.anidex.app.data.remote.dto.AniListPageContainer
import com.anidex.app.data.remote.dto.AniListRequest
import com.anidex.app.data.remote.dto.AniListResponse
import com.anidex.app.data.remote.dto.AniListSingleMediaContainer
import retrofit2.http.Body
import retrofit2.http.POST

interface AniListApiService {

    @POST("/")
    suspend fun getPageMedia(
        @Body request: AniListRequest
    ): AniListResponse<AniListPageContainer>

    @POST("/")
    suspend fun getMediaDetail(
        @Body request: AniListRequest
    ): AniListResponse<AniListSingleMediaContainer>
}

