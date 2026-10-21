package com.anidex.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KitsuListResponse(
    @SerialName("data") val data: List<KitsuAnimeDataDto> = emptyList()
)

@Serializable
data class KitsuSingleResponse(
    @SerialName("data") val data: KitsuAnimeDataDto? = null
)

@Serializable
data class KitsuAnimeDataDto(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String = "anime",
    @SerialName("attributes") val attributes: KitsuAttributesDto
)

@Serializable
data class KitsuAttributesDto(
    @SerialName("slug") val slug: String? = null,
    @SerialName("synopsis") val synopsis: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("titles") val titles: KitsuTitlesDto? = null,
    @SerialName("canonicalTitle") val canonicalTitle: String? = null,
    @SerialName("averageRating") val averageRating: String? = null, // e.g. "84.4"
    @SerialName("userCount") val userCount: Int? = null,
    @SerialName("startDate") val startDate: String? = null, // "2013-04-07"
    @SerialName("nextRelease") val nextRelease: String? = null, // "2026-10-25T09:30:00.000+09:00"
    @SerialName("popularityRank") val popularityRank: Int? = null,
    @SerialName("ratingRank") val ratingRank: Int? = null,
    @SerialName("subtype") val subtype: String? = null, // "TV", "movie", "OVA"
    @SerialName("status") val status: String? = null, // "finished", "current", "unreleased"
    @SerialName("posterImage") val posterImage: KitsuPosterDto? = null,
    @SerialName("coverImage") val coverImage: KitsuCoverDto? = null,
    @SerialName("episodeCount") val episodeCount: Int? = null,
    @SerialName("episodeLength") val episodeLength: Int? = null // in minutes
)

@Serializable
data class KitsuTitlesDto(
    @SerialName("en") val en: String? = null,
    @SerialName("en_jp") val enJp: String? = null,
    @SerialName("ja_jp") val jaJp: String? = null
)

@Serializable
data class KitsuPosterDto(
    @SerialName("tiny") val tiny: String? = null,
    @SerialName("small") val small: String? = null,
    @SerialName("medium") val medium: String? = null,
    @SerialName("large") val large: String? = null,
    @SerialName("original") val original: String? = null
)

@Serializable
data class KitsuCoverDto(
    @SerialName("tiny") val tiny: String? = null,
    @SerialName("small") val small: String? = null,
    @SerialName("large") val large: String? = null,
    @SerialName("original") val original: String? = null
)
