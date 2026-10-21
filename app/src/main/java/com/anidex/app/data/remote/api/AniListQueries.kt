package com.anidex.app.data.remote.api

import com.anidex.app.data.remote.dto.AniListRequest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object AniListQueries {

    private const val MEDIA_FIELDS = """
        id
        title {
            romaji
            english
            native
        }
        description
        coverImage {
            extraLarge
            large
            medium
        }
        bannerImage
        averageScore
        meanScore
        popularity
        episodes
        duration
        format
        status
        genres
        studios(isMain: true) {
            nodes {
                id
                name
            }
        }
        nextAiringEpisode {
            airingAt
            timeUntilAiring
            episode
        }
        seasonYear
        season
    """

    fun buildTopAnimeRequest(page: Int = 1, perPage: Int = 25): AniListRequest {
        val query = """
            query (${'$'}page: Int, ${'$'}perPage: Int) {
                Page(page: ${'$'}page, perPage: ${'$'}perPage) {
                    media(sort: POPULARITY_DESC, type: ANIME) {
                        $MEDIA_FIELDS
                    }
                }
            }
        """.trimIndent()

        val variables = buildJsonObject {
            put("page", page)
            put("perPage", perPage)
        }

        return AniListRequest(query = query, variables = variables)
    }

    fun buildSeasonalAnimeRequest(year: Int, season: String, page: Int = 1, perPage: Int = 25): AniListRequest {
        val query = """
            query (${'$'}year: Int, ${'$'}season: MediaSeason, ${'$'}page: Int, ${'$'}perPage: Int) {
                Page(page: ${'$'}page, perPage: ${'$'}perPage) {
                    media(seasonYear: ${'$'}year, season: ${'$'}season, sort: POPULARITY_DESC, type: ANIME) {
                        $MEDIA_FIELDS
                    }
                }
            }
        """.trimIndent()

        val variables = buildJsonObject {
            put("year", year)
            put("season", season.uppercase())
            put("page", page)
            put("perPage", perPage)
        }

        return AniListRequest(query = query, variables = variables)
    }

    fun buildAiringSchedulesRequest(page: Int = 1, perPage: Int = 50): AniListRequest {
        val query = """
            query (${'$'}page: Int, ${'$'}perPage: Int) {
                Page(page: ${'$'}page, perPage: ${'$'}perPage) {
                    media(status: RELEASING, sort: POPULARITY_DESC, type: ANIME) {
                        $MEDIA_FIELDS
                    }
                }
            }
        """.trimIndent()

        val variables = buildJsonObject {
            put("page", page)
            put("perPage", perPage)
        }

        return AniListRequest(query = query, variables = variables)
    }

    fun buildMediaDetailRequest(id: Int): AniListRequest {
        val query = """
            query (${'$'}id: Int) {
                Media(id: ${'$'}id, type: ANIME) {
                    $MEDIA_FIELDS
                }
            }
        """.trimIndent()

        val variables = buildJsonObject {
            put("id", id)
        }

        return AniListRequest(query = query, variables = variables)
    }

    fun buildSearchAnimeRequest(queryStr: String, page: Int = 1, perPage: Int = 25): AniListRequest {
        val query = """
            query (${'$'}search: String, ${'$'}page: Int, ${'$'}perPage: Int) {
                Page(page: ${'$'}page, perPage: ${'$'}perPage) {
                    media(search: ${'$'}search, type: ANIME, sort: SEARCH_MATCH) {
                        $MEDIA_FIELDS
                    }
                }
            }
        """.trimIndent()

        val variables = buildJsonObject {
            put("search", queryStr)
            put("page", page)
            put("perPage", perPage)
        }

        return AniListRequest(query = query, variables = variables)
    }
}

