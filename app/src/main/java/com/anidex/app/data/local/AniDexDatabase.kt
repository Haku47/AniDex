package com.anidex.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anidex.app.data.local.dao.AnimeDao
import com.anidex.app.data.local.dao.WatchlistDao
import com.anidex.app.data.local.entity.AnimeEntity
import com.anidex.app.data.local.entity.WatchlistEntity

@Database(
    entities = [
        AnimeEntity::class,
        WatchlistEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AniDexDatabase : RoomDatabase() {
    abstract val animeDao: AnimeDao
    abstract val watchlistDao: WatchlistDao
}

