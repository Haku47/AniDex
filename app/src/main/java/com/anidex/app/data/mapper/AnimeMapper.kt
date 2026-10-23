package com.anidex.app.data.mapper

import com.anidex.app.data.local.entity.AnimeEntity
import com.anidex.app.data.local.entity.WatchlistEntity
import com.anidex.app.data.remote.dto.AniListMediaDto
import com.anidex.app.data.remote.dto.AnimeDto
import com.anidex.app.data.remote.dto.KitsuAnimeDataDto
import com.anidex.app.domain.model.Anime
import com.anidex.app.domain.model.AnimeFormat
import com.anidex.app.domain.model.AnimeStatus
import com.anidex.app.domain.model.Genre
import com.anidex.app.domain.model.Studio
import com.anidex.app.domain.model.WatchStatus
import com.anidex.app.domain.model.WatchlistEntry
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object AnimeMapper {

    private val JST_ZONE = ZoneId.of("Asia/Tokyo")

    fun parseDurationToMinutes(durationStr: String?): Int? {
        if (durationStr.isNullOrBlank()) return null
        var totalMinutes = 0
        val hrRegex = Regex("(\\d+)\\s*(?:hr|hour|hours)")
        val minRegex = Regex("(\\d+)\\s*(?:min|mins|minute|minutes)")

        hrRegex.find(durationStr)?.let {
            totalMinutes += it.groupValues[1].toIntOrNull()?.times(60) ?: 0
        }
        minRegex.find(durationStr)?.let {
            totalMinutes += it.groupValues[1].toIntOrNull() ?: 0
        }

        return if (totalMinutes > 0) totalMinutes else null
    }

    fun cleanHtmlDescription(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        return raw.replace(Regex("<[^>]*>"), "").trim()
    }

    // Kitsu DTO -> Domain Model
    fun KitsuAnimeDataDto.toDomain(): Anime {
        val attr = attributes
        val intId = id.toIntOrNull() ?: kotlin.math.abs(id.hashCode())

        val calculatedScore = attr.averageRating?.toDoubleOrNull()?.let { it / 10.0 }
        val mappedStatus = when (attr.status?.lowercase()) {
            "current" -> AnimeStatus.AIRING
            "finished" -> AnimeStatus.FINISHED
            "unreleased" -> AnimeStatus.NOT_YET_AIRED
            else -> AnimeStatus.UNKNOWN
        }

        // Parse air day & time (prioritize accurate nextRelease ISO timestamp, fallback to startDate)
        var bDay: String? = null
        var bTime: String? = null
        var year: Int? = null

        if (!attr.nextRelease.isNullOrBlank()) {
            runCatching {
                val zdt = ZonedDateTime.parse(attr.nextRelease)
                val jstZdt = zdt.withZoneSameInstant(JST_ZONE)
                bDay = jstZdt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                bTime = String.format("%02d:%02d", jstZdt.hour, jstZdt.minute)
                year = jstZdt.year
            }
        }

        if (bDay == null && !attr.startDate.isNullOrBlank()) {
            runCatching {
                val parsedDate = LocalDate.parse(attr.startDate, DateTimeFormatter.ISO_LOCAL_DATE)
                bDay = parsedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                bTime = "23:00"
                year = parsedDate.year
            }
        }

        return Anime(
            id = intId,
            title = attr.canonicalTitle ?: attr.titles?.enJp ?: attr.titles?.en ?: "Anime",
            englishTitle = attr.titles?.en,
            synopsis = cleanHtmlDescription(attr.synopsis ?: attr.description),
            imageUrl = attr.posterImage?.large ?: attr.posterImage?.original ?: attr.posterImage?.medium ?: "",
            bannerUrl = attr.coverImage?.large ?: attr.coverImage?.original,
            score = calculatedScore,
            scoredBy = attr.userCount,
            rank = attr.ratingRank,
            popularity = attr.popularityRank,
            episodes = attr.episodeCount,
            episodeDurationMinutes = attr.episodeLength ?: 24,
            format = AnimeFormat.fromString(attr.subtype),
            status = mappedStatus,
            genres = listOf(
                Genre(id = 1, name = "Action"),
                Genre(id = 2, name = "Adventure")
            ),
            studios = emptyList(),
            broadcastDay = bDay ?: "Sundays",
            broadcastTime = bTime ?: "23:00",
            broadcastTimezone = "Asia/Tokyo",
            year = year,
            season = null
        )
    }

    // AniList Media DTO -> Domain Model
    fun AniListMediaDto.toDomain(): Anime {
        val calculatedScore = averageScore?.let { it / 10.0 } ?: meanScore?.let { it / 10.0 }
        val mappedStatus = when (status?.uppercase()) {
            "RELEASING" -> AnimeStatus.AIRING
            "FINISHED" -> AnimeStatus.FINISHED
            "NOT_YET_RELEASED" -> AnimeStatus.NOT_YET_AIRED
            else -> AnimeStatus.UNKNOWN
        }

        var bDay: String? = null
        var bTime: String? = null
        nextAiringEpisode?.let { nextEp ->
            val jstInstant = Instant.ofEpochSecond(nextEp.airingAt).atZone(JST_ZONE)
            bDay = jstInstant.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
            bTime = String.format("%02d:%02d", jstInstant.hour, jstInstant.minute)
        }

        return Anime(
            id = id,
            title = title?.romaji ?: title?.english ?: "Unknown Anime",
            englishTitle = title?.english,
            synopsis = cleanHtmlDescription(description),
            imageUrl = coverImage?.extraLarge ?: coverImage?.large ?: coverImage?.medium ?: "",
            bannerUrl = bannerImage,
            score = calculatedScore,
            scoredBy = popularity,
            rank = null,
            popularity = popularity,
            episodes = episodes,
            episodeDurationMinutes = duration,
            format = AnimeFormat.fromString(format),
            status = mappedStatus,
            genres = genres.mapIndexed { index, name -> Genre(id = index, name = name) },
            studios = studios?.nodes?.mapIndexed { index, s -> Studio(id = s.id ?: index, name = s.name) } ?: emptyList(),
            broadcastDay = bDay,
            broadcastTime = bTime,
            broadcastTimezone = "Asia/Tokyo",
            year = seasonYear,
            season = season
        )
    }

    // Jikan DTO -> Domain Model (Legacy fallback)
    fun AnimeDto.toDomain(): Anime {
        return Anime(
            id = malId,
            title = title,
            englishTitle = titleEnglish,
            synopsis = synopsis ?: "",
            imageUrl = images?.webp?.largeImageUrl ?: images?.jpg?.largeImageUrl ?: images?.jpg?.imageUrl ?: "",
            bannerUrl = images?.webp?.largeImageUrl ?: images?.jpg?.largeImageUrl,
            score = score,
            scoredBy = scoredBy,
            rank = rank,
            popularity = popularity,
            episodes = episodes,
            episodeDurationMinutes = parseDurationToMinutes(duration),
            format = AnimeFormat.fromString(type),
            status = AnimeStatus.fromString(status),
            genres = genres.map { Genre(id = it.malId, name = it.name) },
            studios = studios.map { Studio(id = it.malId, name = it.name) },
            broadcastDay = broadcast?.day,
            broadcastTime = broadcast?.time,
            broadcastTimezone = broadcast?.timezone,
            year = year,
            season = season
        )
    }

    // Entity -> Domain Model
    fun AnimeEntity.toDomain(): Anime {
        val parsedGenres = genresJson.split(",").filter { it.isNotBlank() }.mapIndexed { index, name ->
            Genre(id = index, name = name.trim())
        }
        val parsedStudios = studiosJson.split(",").filter { it.isNotBlank() }.mapIndexed { index, name ->
            Studio(id = index, name = name.trim())
        }

        return Anime(
            id = id,
            title = title,
            englishTitle = englishTitle,
            synopsis = synopsis,
            imageUrl = imageUrl,
            bannerUrl = bannerUrl,
            score = score,
            scoredBy = scoredBy,
            rank = rank,
            popularity = popularity,
            episodes = episodes,
            episodeDurationMinutes = episodeDurationMinutes,
            format = AnimeFormat.fromString(format),
            status = AnimeStatus.fromString(status),
            genres = parsedGenres,
            studios = parsedStudios,
            broadcastDay = broadcastDay,
            broadcastTime = broadcastTime,
            broadcastTimezone = broadcastTimezone,
            year = year,
            season = season
        )
    }

    // Domain Model -> Entity
    fun Anime.toEntity(): AnimeEntity {
        return AnimeEntity(
            id = id,
            title = title,
            englishTitle = englishTitle,
            synopsis = synopsis,
            imageUrl = imageUrl,
            bannerUrl = bannerUrl,
            score = score,
            scoredBy = scoredBy,
            rank = rank,
            popularity = popularity,
            episodes = episodes,
            episodeDurationMinutes = episodeDurationMinutes,
            format = format.name,
            status = status.name,
            genresJson = genres.joinToString(",") { it.name },
            studiosJson = studios.joinToString(",") { it.name },
            broadcastDay = broadcastDay,
            broadcastTime = broadcastTime,
            broadcastTimezone = broadcastTimezone,
            year = year,
            season = season
        )
    }

    // Entity -> Domain WatchlistEntry
    fun WatchlistEntity.toDomain(anime: Anime): WatchlistEntry {
        return WatchlistEntry(
            anime = anime,
            status = runCatching { WatchStatus.valueOf(status) }.getOrDefault(WatchStatus.PLAN_TO_WATCH),
            episodesWatched = episodesWatched,
            userScore = userScore,
            lastUpdatedEpochMillis = lastUpdatedEpochMillis,
            notes = notes,
            startDateEpochMillis = startDateEpochMillis,
            finishDateEpochMillis = finishDateEpochMillis
        )
    }

    // Domain -> Entity WatchlistEntry
    fun WatchlistEntry.toEntity(): WatchlistEntity {
        return WatchlistEntity(
            animeId = anime.id,
            status = status.name,
            episodesWatched = episodesWatched,
            userScore = userScore,
            lastUpdatedEpochMillis = lastUpdatedEpochMillis,
            notes = notes,
            startDateEpochMillis = startDateEpochMillis,
            finishDateEpochMillis = finishDateEpochMillis
        )
    }
}
