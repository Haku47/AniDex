package com.anidex.app.domain.model

import androidx.annotation.StringRes
import com.anidex.app.R

data class Genre(
    val id: Int,
    val name: String
)

data class Studio(
    val id: Int,
    val name: String
)

enum class AnimeFormat(@StringRes val titleRes: Int) {
    TV(R.string.format_tv),
    MOVIE(R.string.format_movie),
    OVA(R.string.format_ova),
    ONA(R.string.format_ona),
    SPECIAL(R.string.format_special),
    MUSIC(R.string.format_music),
    UNKNOWN(R.string.format_unknown);

    companion object {
        fun fromString(type: String?): AnimeFormat {
            return when (type?.uppercase()) {
                "TV" -> TV
                "MOVIE" -> MOVIE
                "OVA" -> OVA
                "ONA" -> ONA
                "SPECIAL" -> SPECIAL
                "MUSIC" -> MUSIC
                else -> UNKNOWN
            }
        }
    }
}

enum class AnimeStatus(@StringRes val titleRes: Int) {
    AIRING(R.string.anime_status_airing),
    FINISHED(R.string.anime_status_finished),
    NOT_YET_AIRED(R.string.anime_status_not_yet),
    UNKNOWN(R.string.anime_status_unknown);

    companion object {
        fun fromString(status: String?): AnimeStatus {
            return when (status?.lowercase()) {
                "currently airing", "airing" -> AIRING
                "finished airing", "finished" -> FINISHED
                "not yet aired" -> NOT_YET_AIRED
                else -> UNKNOWN
            }
        }
    }
}

