package com.anidex.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.anidex.app.data.local.dao.AnimeDao;
import com.anidex.app.data.local.dao.AnimeDao_Impl;
import com.anidex.app.data.local.dao.WatchlistDao;
import com.anidex.app.data.local.dao.WatchlistDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AniDexDatabase_Impl extends AniDexDatabase {
  private volatile AnimeDao _animeDao;

  private volatile WatchlistDao _watchlistDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `animes` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `englishTitle` TEXT, `synopsis` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `bannerUrl` TEXT, `score` REAL, `scoredBy` INTEGER, `rank` INTEGER, `popularity` INTEGER, `episodes` INTEGER, `episodeDurationMinutes` INTEGER, `format` TEXT NOT NULL, `status` TEXT NOT NULL, `genresJson` TEXT NOT NULL, `studiosJson` TEXT NOT NULL, `broadcastDay` TEXT, `broadcastTime` TEXT, `broadcastTimezone` TEXT, `year` INTEGER, `season` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `watchlist` (`animeId` INTEGER NOT NULL, `status` TEXT NOT NULL, `episodesWatched` INTEGER NOT NULL, `userScore` REAL, `lastUpdatedEpochMillis` INTEGER NOT NULL, `notes` TEXT NOT NULL, `startDateEpochMillis` INTEGER, `finishDateEpochMillis` INTEGER, PRIMARY KEY(`animeId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5a3e8057ae258f70229177521b11d895')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `animes`");
        db.execSQL("DROP TABLE IF EXISTS `watchlist`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAnimes = new HashMap<String, TableInfo.Column>(21);
        _columnsAnimes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("englishTitle", new TableInfo.Column("englishTitle", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("synopsis", new TableInfo.Column("synopsis", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("bannerUrl", new TableInfo.Column("bannerUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("score", new TableInfo.Column("score", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("scoredBy", new TableInfo.Column("scoredBy", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("rank", new TableInfo.Column("rank", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("popularity", new TableInfo.Column("popularity", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("episodes", new TableInfo.Column("episodes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("episodeDurationMinutes", new TableInfo.Column("episodeDurationMinutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("format", new TableInfo.Column("format", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("genresJson", new TableInfo.Column("genresJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("studiosJson", new TableInfo.Column("studiosJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("broadcastDay", new TableInfo.Column("broadcastDay", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("broadcastTime", new TableInfo.Column("broadcastTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("broadcastTimezone", new TableInfo.Column("broadcastTimezone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("year", new TableInfo.Column("year", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnimes.put("season", new TableInfo.Column("season", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAnimes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAnimes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAnimes = new TableInfo("animes", _columnsAnimes, _foreignKeysAnimes, _indicesAnimes);
        final TableInfo _existingAnimes = TableInfo.read(db, "animes");
        if (!_infoAnimes.equals(_existingAnimes)) {
          return new RoomOpenHelper.ValidationResult(false, "animes(com.anidex.app.data.local.entity.AnimeEntity).\n"
                  + " Expected:\n" + _infoAnimes + "\n"
                  + " Found:\n" + _existingAnimes);
        }
        final HashMap<String, TableInfo.Column> _columnsWatchlist = new HashMap<String, TableInfo.Column>(8);
        _columnsWatchlist.put("animeId", new TableInfo.Column("animeId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchlist.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchlist.put("episodesWatched", new TableInfo.Column("episodesWatched", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchlist.put("userScore", new TableInfo.Column("userScore", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchlist.put("lastUpdatedEpochMillis", new TableInfo.Column("lastUpdatedEpochMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchlist.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchlist.put("startDateEpochMillis", new TableInfo.Column("startDateEpochMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchlist.put("finishDateEpochMillis", new TableInfo.Column("finishDateEpochMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWatchlist = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWatchlist = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWatchlist = new TableInfo("watchlist", _columnsWatchlist, _foreignKeysWatchlist, _indicesWatchlist);
        final TableInfo _existingWatchlist = TableInfo.read(db, "watchlist");
        if (!_infoWatchlist.equals(_existingWatchlist)) {
          return new RoomOpenHelper.ValidationResult(false, "watchlist(com.anidex.app.data.local.entity.WatchlistEntity).\n"
                  + " Expected:\n" + _infoWatchlist + "\n"
                  + " Found:\n" + _existingWatchlist);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "5a3e8057ae258f70229177521b11d895", "4b79c604b0f7284c225a8f85da98cde3");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "animes","watchlist");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `animes`");
      _db.execSQL("DELETE FROM `watchlist`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AnimeDao.class, AnimeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WatchlistDao.class, WatchlistDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AnimeDao getAnimeDao() {
    if (_animeDao != null) {
      return _animeDao;
    } else {
      synchronized(this) {
        if(_animeDao == null) {
          _animeDao = new AnimeDao_Impl(this);
        }
        return _animeDao;
      }
    }
  }

  @Override
  public WatchlistDao getWatchlistDao() {
    if (_watchlistDao != null) {
      return _watchlistDao;
    } else {
      synchronized(this) {
        if(_watchlistDao == null) {
          _watchlistDao = new WatchlistDao_Impl(this);
        }
        return _watchlistDao;
      }
    }
  }
}
