package com.anidex.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.anidex.app.data.local.entity.AnimeEntity;
import com.anidex.app.data.local.entity.WatchlistEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WatchlistDao_Impl implements WatchlistDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WatchlistEntity> __insertionAdapterOfWatchlistEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateEpisodeCount;

  private final SharedSQLiteStatement __preparedStmtOfDeleteEntry;

  public WatchlistDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWatchlistEntity = new EntityInsertionAdapter<WatchlistEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `watchlist` (`animeId`,`status`,`episodesWatched`,`userScore`,`lastUpdatedEpochMillis`,`notes`,`startDateEpochMillis`,`finishDateEpochMillis`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WatchlistEntity entity) {
        statement.bindLong(1, entity.getAnimeId());
        statement.bindString(2, entity.getStatus());
        statement.bindLong(3, entity.getEpisodesWatched());
        if (entity.getUserScore() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getUserScore());
        }
        statement.bindLong(5, entity.getLastUpdatedEpochMillis());
        statement.bindString(6, entity.getNotes());
        if (entity.getStartDateEpochMillis() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getStartDateEpochMillis());
        }
        if (entity.getFinishDateEpochMillis() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getFinishDateEpochMillis());
        }
      }
    };
    this.__preparedStmtOfUpdateEpisodeCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE watchlist SET episodesWatched = ?, lastUpdatedEpochMillis = ? WHERE animeId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteEntry = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM watchlist WHERE animeId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertWatchlist(final WatchlistEntity entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWatchlistEntity.insert(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEpisodeCount(final int animeId, final int episodes, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateEpisodeCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, episodes);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, animeId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateEpisodeCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteEntry(final int animeId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteEntry.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, animeId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteEntry.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WatchlistWithAnimeEntity>> observeAllWithAnime() {
    final String _sql = "SELECT * FROM watchlist ORDER BY lastUpdatedEpochMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"animes",
        "watchlist"}, new Callable<List<WatchlistWithAnimeEntity>>() {
      @Override
      @NonNull
      public List<WatchlistWithAnimeEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfAnimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "animeId");
            final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
            final int _cursorIndexOfEpisodesWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "episodesWatched");
            final int _cursorIndexOfUserScore = CursorUtil.getColumnIndexOrThrow(_cursor, "userScore");
            final int _cursorIndexOfLastUpdatedEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdatedEpochMillis");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfStartDateEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startDateEpochMillis");
            final int _cursorIndexOfFinishDateEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "finishDateEpochMillis");
            final LongSparseArray<AnimeEntity> _collectionAnime = new LongSparseArray<AnimeEntity>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfAnimeId);
              _collectionAnime.put(_tmpKey, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipanimesAscomAnidexAppDataLocalEntityAnimeEntity(_collectionAnime);
            final List<WatchlistWithAnimeEntity> _result = new ArrayList<WatchlistWithAnimeEntity>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final WatchlistWithAnimeEntity _item;
              final WatchlistEntity _tmpWatchlist;
              final int _tmpAnimeId;
              _tmpAnimeId = _cursor.getInt(_cursorIndexOfAnimeId);
              final String _tmpStatus;
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
              final int _tmpEpisodesWatched;
              _tmpEpisodesWatched = _cursor.getInt(_cursorIndexOfEpisodesWatched);
              final Double _tmpUserScore;
              if (_cursor.isNull(_cursorIndexOfUserScore)) {
                _tmpUserScore = null;
              } else {
                _tmpUserScore = _cursor.getDouble(_cursorIndexOfUserScore);
              }
              final long _tmpLastUpdatedEpochMillis;
              _tmpLastUpdatedEpochMillis = _cursor.getLong(_cursorIndexOfLastUpdatedEpochMillis);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final Long _tmpStartDateEpochMillis;
              if (_cursor.isNull(_cursorIndexOfStartDateEpochMillis)) {
                _tmpStartDateEpochMillis = null;
              } else {
                _tmpStartDateEpochMillis = _cursor.getLong(_cursorIndexOfStartDateEpochMillis);
              }
              final Long _tmpFinishDateEpochMillis;
              if (_cursor.isNull(_cursorIndexOfFinishDateEpochMillis)) {
                _tmpFinishDateEpochMillis = null;
              } else {
                _tmpFinishDateEpochMillis = _cursor.getLong(_cursorIndexOfFinishDateEpochMillis);
              }
              _tmpWatchlist = new WatchlistEntity(_tmpAnimeId,_tmpStatus,_tmpEpisodesWatched,_tmpUserScore,_tmpLastUpdatedEpochMillis,_tmpNotes,_tmpStartDateEpochMillis,_tmpFinishDateEpochMillis);
              final AnimeEntity _tmpAnime;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAnimeId);
              _tmpAnime = _collectionAnime.get(_tmpKey_1);
              _item = new WatchlistWithAnimeEntity(_tmpWatchlist,_tmpAnime);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<WatchlistWithAnimeEntity>> observeByStatusWithAnime(final String status) {
    final String _sql = "SELECT * FROM watchlist WHERE status = ? ORDER BY lastUpdatedEpochMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, status);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"animes",
        "watchlist"}, new Callable<List<WatchlistWithAnimeEntity>>() {
      @Override
      @NonNull
      public List<WatchlistWithAnimeEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfAnimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "animeId");
            final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
            final int _cursorIndexOfEpisodesWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "episodesWatched");
            final int _cursorIndexOfUserScore = CursorUtil.getColumnIndexOrThrow(_cursor, "userScore");
            final int _cursorIndexOfLastUpdatedEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdatedEpochMillis");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfStartDateEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startDateEpochMillis");
            final int _cursorIndexOfFinishDateEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "finishDateEpochMillis");
            final LongSparseArray<AnimeEntity> _collectionAnime = new LongSparseArray<AnimeEntity>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfAnimeId);
              _collectionAnime.put(_tmpKey, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipanimesAscomAnidexAppDataLocalEntityAnimeEntity(_collectionAnime);
            final List<WatchlistWithAnimeEntity> _result = new ArrayList<WatchlistWithAnimeEntity>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final WatchlistWithAnimeEntity _item;
              final WatchlistEntity _tmpWatchlist;
              final int _tmpAnimeId;
              _tmpAnimeId = _cursor.getInt(_cursorIndexOfAnimeId);
              final String _tmpStatus;
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
              final int _tmpEpisodesWatched;
              _tmpEpisodesWatched = _cursor.getInt(_cursorIndexOfEpisodesWatched);
              final Double _tmpUserScore;
              if (_cursor.isNull(_cursorIndexOfUserScore)) {
                _tmpUserScore = null;
              } else {
                _tmpUserScore = _cursor.getDouble(_cursorIndexOfUserScore);
              }
              final long _tmpLastUpdatedEpochMillis;
              _tmpLastUpdatedEpochMillis = _cursor.getLong(_cursorIndexOfLastUpdatedEpochMillis);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final Long _tmpStartDateEpochMillis;
              if (_cursor.isNull(_cursorIndexOfStartDateEpochMillis)) {
                _tmpStartDateEpochMillis = null;
              } else {
                _tmpStartDateEpochMillis = _cursor.getLong(_cursorIndexOfStartDateEpochMillis);
              }
              final Long _tmpFinishDateEpochMillis;
              if (_cursor.isNull(_cursorIndexOfFinishDateEpochMillis)) {
                _tmpFinishDateEpochMillis = null;
              } else {
                _tmpFinishDateEpochMillis = _cursor.getLong(_cursorIndexOfFinishDateEpochMillis);
              }
              _tmpWatchlist = new WatchlistEntity(_tmpAnimeId,_tmpStatus,_tmpEpisodesWatched,_tmpUserScore,_tmpLastUpdatedEpochMillis,_tmpNotes,_tmpStartDateEpochMillis,_tmpFinishDateEpochMillis);
              final AnimeEntity _tmpAnime;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAnimeId);
              _tmpAnime = _collectionAnime.get(_tmpKey_1);
              _item = new WatchlistWithAnimeEntity(_tmpWatchlist,_tmpAnime);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllWithAnime(
      final Continuation<? super List<WatchlistWithAnimeEntity>> $completion) {
    final String _sql = "SELECT * FROM watchlist";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<WatchlistWithAnimeEntity>>() {
      @Override
      @NonNull
      public List<WatchlistWithAnimeEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfAnimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "animeId");
            final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
            final int _cursorIndexOfEpisodesWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "episodesWatched");
            final int _cursorIndexOfUserScore = CursorUtil.getColumnIndexOrThrow(_cursor, "userScore");
            final int _cursorIndexOfLastUpdatedEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdatedEpochMillis");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfStartDateEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startDateEpochMillis");
            final int _cursorIndexOfFinishDateEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "finishDateEpochMillis");
            final LongSparseArray<AnimeEntity> _collectionAnime = new LongSparseArray<AnimeEntity>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfAnimeId);
              _collectionAnime.put(_tmpKey, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipanimesAscomAnidexAppDataLocalEntityAnimeEntity(_collectionAnime);
            final List<WatchlistWithAnimeEntity> _result = new ArrayList<WatchlistWithAnimeEntity>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final WatchlistWithAnimeEntity _item;
              final WatchlistEntity _tmpWatchlist;
              final int _tmpAnimeId;
              _tmpAnimeId = _cursor.getInt(_cursorIndexOfAnimeId);
              final String _tmpStatus;
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
              final int _tmpEpisodesWatched;
              _tmpEpisodesWatched = _cursor.getInt(_cursorIndexOfEpisodesWatched);
              final Double _tmpUserScore;
              if (_cursor.isNull(_cursorIndexOfUserScore)) {
                _tmpUserScore = null;
              } else {
                _tmpUserScore = _cursor.getDouble(_cursorIndexOfUserScore);
              }
              final long _tmpLastUpdatedEpochMillis;
              _tmpLastUpdatedEpochMillis = _cursor.getLong(_cursorIndexOfLastUpdatedEpochMillis);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final Long _tmpStartDateEpochMillis;
              if (_cursor.isNull(_cursorIndexOfStartDateEpochMillis)) {
                _tmpStartDateEpochMillis = null;
              } else {
                _tmpStartDateEpochMillis = _cursor.getLong(_cursorIndexOfStartDateEpochMillis);
              }
              final Long _tmpFinishDateEpochMillis;
              if (_cursor.isNull(_cursorIndexOfFinishDateEpochMillis)) {
                _tmpFinishDateEpochMillis = null;
              } else {
                _tmpFinishDateEpochMillis = _cursor.getLong(_cursorIndexOfFinishDateEpochMillis);
              }
              _tmpWatchlist = new WatchlistEntity(_tmpAnimeId,_tmpStatus,_tmpEpisodesWatched,_tmpUserScore,_tmpLastUpdatedEpochMillis,_tmpNotes,_tmpStartDateEpochMillis,_tmpFinishDateEpochMillis);
              final AnimeEntity _tmpAnime;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAnimeId);
              _tmpAnime = _collectionAnime.get(_tmpKey_1);
              _item = new WatchlistWithAnimeEntity(_tmpWatchlist,_tmpAnime);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getEntryWithAnime(final int animeId,
      final Continuation<? super WatchlistWithAnimeEntity> $completion) {
    final String _sql = "SELECT * FROM watchlist WHERE animeId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, animeId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<WatchlistWithAnimeEntity>() {
      @Override
      @Nullable
      public WatchlistWithAnimeEntity call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfAnimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "animeId");
            final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
            final int _cursorIndexOfEpisodesWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "episodesWatched");
            final int _cursorIndexOfUserScore = CursorUtil.getColumnIndexOrThrow(_cursor, "userScore");
            final int _cursorIndexOfLastUpdatedEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdatedEpochMillis");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfStartDateEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startDateEpochMillis");
            final int _cursorIndexOfFinishDateEpochMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "finishDateEpochMillis");
            final LongSparseArray<AnimeEntity> _collectionAnime = new LongSparseArray<AnimeEntity>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfAnimeId);
              _collectionAnime.put(_tmpKey, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipanimesAscomAnidexAppDataLocalEntityAnimeEntity(_collectionAnime);
            final WatchlistWithAnimeEntity _result;
            if (_cursor.moveToFirst()) {
              final WatchlistEntity _tmpWatchlist;
              final int _tmpAnimeId;
              _tmpAnimeId = _cursor.getInt(_cursorIndexOfAnimeId);
              final String _tmpStatus;
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
              final int _tmpEpisodesWatched;
              _tmpEpisodesWatched = _cursor.getInt(_cursorIndexOfEpisodesWatched);
              final Double _tmpUserScore;
              if (_cursor.isNull(_cursorIndexOfUserScore)) {
                _tmpUserScore = null;
              } else {
                _tmpUserScore = _cursor.getDouble(_cursorIndexOfUserScore);
              }
              final long _tmpLastUpdatedEpochMillis;
              _tmpLastUpdatedEpochMillis = _cursor.getLong(_cursorIndexOfLastUpdatedEpochMillis);
              final String _tmpNotes;
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              final Long _tmpStartDateEpochMillis;
              if (_cursor.isNull(_cursorIndexOfStartDateEpochMillis)) {
                _tmpStartDateEpochMillis = null;
              } else {
                _tmpStartDateEpochMillis = _cursor.getLong(_cursorIndexOfStartDateEpochMillis);
              }
              final Long _tmpFinishDateEpochMillis;
              if (_cursor.isNull(_cursorIndexOfFinishDateEpochMillis)) {
                _tmpFinishDateEpochMillis = null;
              } else {
                _tmpFinishDateEpochMillis = _cursor.getLong(_cursorIndexOfFinishDateEpochMillis);
              }
              _tmpWatchlist = new WatchlistEntity(_tmpAnimeId,_tmpStatus,_tmpEpisodesWatched,_tmpUserScore,_tmpLastUpdatedEpochMillis,_tmpNotes,_tmpStartDateEpochMillis,_tmpFinishDateEpochMillis);
              final AnimeEntity _tmpAnime;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfAnimeId);
              _tmpAnime = _collectionAnime.get(_tmpKey_1);
              _result = new WatchlistWithAnimeEntity(_tmpWatchlist,_tmpAnime);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipanimesAscomAnidexAppDataLocalEntityAnimeEntity(
      @NonNull final LongSparseArray<AnimeEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipanimesAscomAnidexAppDataLocalEntityAnimeEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`title`,`englishTitle`,`synopsis`,`imageUrl`,`bannerUrl`,`score`,`scoredBy`,`rank`,`popularity`,`episodes`,`episodeDurationMinutes`,`format`,`status`,`genresJson`,`studiosJson`,`broadcastDay`,`broadcastTime`,`broadcastTimezone`,`year`,`season` FROM `animes` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfTitle = 1;
      final int _cursorIndexOfEnglishTitle = 2;
      final int _cursorIndexOfSynopsis = 3;
      final int _cursorIndexOfImageUrl = 4;
      final int _cursorIndexOfBannerUrl = 5;
      final int _cursorIndexOfScore = 6;
      final int _cursorIndexOfScoredBy = 7;
      final int _cursorIndexOfRank = 8;
      final int _cursorIndexOfPopularity = 9;
      final int _cursorIndexOfEpisodes = 10;
      final int _cursorIndexOfEpisodeDurationMinutes = 11;
      final int _cursorIndexOfFormat = 12;
      final int _cursorIndexOfStatus = 13;
      final int _cursorIndexOfGenresJson = 14;
      final int _cursorIndexOfStudiosJson = 15;
      final int _cursorIndexOfBroadcastDay = 16;
      final int _cursorIndexOfBroadcastTime = 17;
      final int _cursorIndexOfBroadcastTimezone = 18;
      final int _cursorIndexOfYear = 19;
      final int _cursorIndexOfSeason = 20;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final AnimeEntity _item_1;
          final int _tmpId;
          _tmpId = _cursor.getInt(_cursorIndexOfId);
          final String _tmpTitle;
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
          final String _tmpEnglishTitle;
          if (_cursor.isNull(_cursorIndexOfEnglishTitle)) {
            _tmpEnglishTitle = null;
          } else {
            _tmpEnglishTitle = _cursor.getString(_cursorIndexOfEnglishTitle);
          }
          final String _tmpSynopsis;
          _tmpSynopsis = _cursor.getString(_cursorIndexOfSynopsis);
          final String _tmpImageUrl;
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
          final String _tmpBannerUrl;
          if (_cursor.isNull(_cursorIndexOfBannerUrl)) {
            _tmpBannerUrl = null;
          } else {
            _tmpBannerUrl = _cursor.getString(_cursorIndexOfBannerUrl);
          }
          final Double _tmpScore;
          if (_cursor.isNull(_cursorIndexOfScore)) {
            _tmpScore = null;
          } else {
            _tmpScore = _cursor.getDouble(_cursorIndexOfScore);
          }
          final Integer _tmpScoredBy;
          if (_cursor.isNull(_cursorIndexOfScoredBy)) {
            _tmpScoredBy = null;
          } else {
            _tmpScoredBy = _cursor.getInt(_cursorIndexOfScoredBy);
          }
          final Integer _tmpRank;
          if (_cursor.isNull(_cursorIndexOfRank)) {
            _tmpRank = null;
          } else {
            _tmpRank = _cursor.getInt(_cursorIndexOfRank);
          }
          final Integer _tmpPopularity;
          if (_cursor.isNull(_cursorIndexOfPopularity)) {
            _tmpPopularity = null;
          } else {
            _tmpPopularity = _cursor.getInt(_cursorIndexOfPopularity);
          }
          final Integer _tmpEpisodes;
          if (_cursor.isNull(_cursorIndexOfEpisodes)) {
            _tmpEpisodes = null;
          } else {
            _tmpEpisodes = _cursor.getInt(_cursorIndexOfEpisodes);
          }
          final Integer _tmpEpisodeDurationMinutes;
          if (_cursor.isNull(_cursorIndexOfEpisodeDurationMinutes)) {
            _tmpEpisodeDurationMinutes = null;
          } else {
            _tmpEpisodeDurationMinutes = _cursor.getInt(_cursorIndexOfEpisodeDurationMinutes);
          }
          final String _tmpFormat;
          _tmpFormat = _cursor.getString(_cursorIndexOfFormat);
          final String _tmpStatus;
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
          final String _tmpGenresJson;
          _tmpGenresJson = _cursor.getString(_cursorIndexOfGenresJson);
          final String _tmpStudiosJson;
          _tmpStudiosJson = _cursor.getString(_cursorIndexOfStudiosJson);
          final String _tmpBroadcastDay;
          if (_cursor.isNull(_cursorIndexOfBroadcastDay)) {
            _tmpBroadcastDay = null;
          } else {
            _tmpBroadcastDay = _cursor.getString(_cursorIndexOfBroadcastDay);
          }
          final String _tmpBroadcastTime;
          if (_cursor.isNull(_cursorIndexOfBroadcastTime)) {
            _tmpBroadcastTime = null;
          } else {
            _tmpBroadcastTime = _cursor.getString(_cursorIndexOfBroadcastTime);
          }
          final String _tmpBroadcastTimezone;
          if (_cursor.isNull(_cursorIndexOfBroadcastTimezone)) {
            _tmpBroadcastTimezone = null;
          } else {
            _tmpBroadcastTimezone = _cursor.getString(_cursorIndexOfBroadcastTimezone);
          }
          final Integer _tmpYear;
          if (_cursor.isNull(_cursorIndexOfYear)) {
            _tmpYear = null;
          } else {
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
          }
          final String _tmpSeason;
          if (_cursor.isNull(_cursorIndexOfSeason)) {
            _tmpSeason = null;
          } else {
            _tmpSeason = _cursor.getString(_cursorIndexOfSeason);
          }
          _item_1 = new AnimeEntity(_tmpId,_tmpTitle,_tmpEnglishTitle,_tmpSynopsis,_tmpImageUrl,_tmpBannerUrl,_tmpScore,_tmpScoredBy,_tmpRank,_tmpPopularity,_tmpEpisodes,_tmpEpisodeDurationMinutes,_tmpFormat,_tmpStatus,_tmpGenresJson,_tmpStudiosJson,_tmpBroadcastDay,_tmpBroadcastTime,_tmpBroadcastTimezone,_tmpYear,_tmpSeason);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
