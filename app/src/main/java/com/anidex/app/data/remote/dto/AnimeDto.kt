package com.anidex.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JikanResponse<T>(
    @SerialName("data") val data: T
)

@Serializable
data class AnimeDto(
    @SerialName("mal_id") val malId: Int,
    @SerialName("title") val title: String,
    @SerialName("title_english") val titleEnglish: String? = null,
    @SerialName("synopsis") val synopsis: String? = null,
    @SerialName("images") val images: AnimeImagesDto? = null,
    @SerialName("score") val score: Double? = null,
    @SerialName("scored_by") val scoredBy: Int? = null,
    @SerialName("rank") val rank: Int? = null,
    @SerialName("popularity") val popularity: Int? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("duration") val duration: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("genres") val genres: List<GenreDto> = emptyList(),
    @SerialName("studios") val studios: List<StudioDto> = emptyList(),
    @SerialName("broadcast") val broadcast: BroadcastDto? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("season") val season: String? = null
)

@Serializable
data class AnimeImagesDto(
    @SerialName("jpg") val jpg: ImageDetailDto? = null,
    @SerialName("webp") val webp: ImageDetailDto? = null
)

@Serializable
data class ImageDetailDto(
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("large_image_url") val largeImageUrl: String? = null
)

@Serializable
data class GenreDto(
    @SerialName("mal_id") val malId: Int,
    @SerialName("name") val name: String
)

@Serializable
data class StudioDto(
    @SerialName("mal_id") val malId: Int,
    @SerialName("name") val name: String
)

@Serializable
data class BroadcastDto(
    @SerialName("day") val day: String? = null,
    @SerialName("time") val time: String? = null,
    @SerialName("timezone") val timezone: String? = null,
    @SerialName("string") val string: String? = null
)

