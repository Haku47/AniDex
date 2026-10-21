package com.anidex.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class AniListRequest(
    @SerialName("query") val query: String,
    @SerialName("variables") val variables: JsonObject? = null
)

@Serializable
data class AniListResponse<T>(
    @SerialName("data") val data: T? = null,
    @SerialName("errors") val errors: List<AniListErrorDto>? = null
)

@Serializable
data class AniListErrorDto(
    @SerialName("message") val message: String
)

@Serializable
data class AniListPageContainer(
    @SerialName("Page") val page: AniListPageDto? = null
)

@Serializable
data class AniListSingleMediaContainer(
    @SerialName("Media") val media: AniListMediaDto? = null
)

@Serializable
data class AniListPageDto(
    @SerialName("media") val media: List<AniListMediaDto> = emptyList(),
    @SerialName("airingSchedules") val airingSchedules: List<AniListAiringScheduleDto> = emptyList()
)

@Serializable
data class AniListMediaDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: AniListTitleDto? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("coverImage") val coverImage: AniListCoverImageDto? = null,
    @SerialName("bannerImage") val bannerImage: String? = null,
    @SerialName("averageScore") val averageScore: Int? = null, // e.g. 84 (0-100)
    @SerialName("meanScore") val meanScore: Int? = null,
    @SerialName("popularity") val popularity: Int? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("duration") val duration: Int? = null, // minutes
    @SerialName("format") val format: String? = null, // TV, MOVIE, OVA, ONA, etc.
    @SerialName("status") val status: String? = null, // FINISHED, RELEASING, NOT_YET_RELEASED
    @SerialName("genres") val genres: List<String> = emptyList(),
    @SerialName("studios") val studios: AniListStudiosContainer? = null,
    @SerialName("nextAiringEpisode") val nextAiringEpisode: AniListNextAiringDto? = null,
    @SerialName("seasonYear") val seasonYear: Int? = null,
    @SerialName("season") val season: String? = null
)

@Serializable
data class AniListTitleDto(
    @SerialName("romaji") val romaji: String? = null,
    @SerialName("english") val english: String? = null,
    @SerialName("native") val nativeTitle: String? = null
)

@Serializable
data class AniListCoverImageDto(
    @SerialName("large") val large: String? = null,
    @SerialName("extraLarge") val extraLarge: String? = null,
    @SerialName("medium") val medium: String? = null
)

@Serializable
data class AniListStudiosContainer(
    @SerialName("nodes") val nodes: List<AniListStudioNodeDto> = emptyList()
)

@Serializable
data class AniListStudioNodeDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String
)

@Serializable
data class AniListNextAiringDto(
    @SerialName("airingAt") val airingAt: Long, // Epoch seconds
    @SerialName("timeUntilAiring") val timeUntilAiring: Long, // Seconds
    @SerialName("episode") val episode: Int
)

@Serializable
data class AniListAiringScheduleDto(
    @SerialName("id") val id: Int,
    @SerialName("airingAt") val airingAt: Long,
    @SerialName("timeUntilAiring") val timeUntilAiring: Long,
    @SerialName("episode") val episode: Int,
    @SerialName("media") val media: AniListMediaDto? = null
)

