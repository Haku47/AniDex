package com.anidex.app.domain.model

data class Genre(
    val id: Int,
    val name: String
)

data class Studio(
    val id: Int,
    val name: String
)

enum class AnimeFormat {
    TV,
    MOVIE,
    OVA,
    ONA,
    SPECIAL,
    MUSIC,
    UNKNOWN;

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

enum class AnimeStatus {
    AIRING,
    FINISHED,
    NOT_YET_AIRED,
    UNKNOWN;

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

