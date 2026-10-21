package com.anidex.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.anidex.app.data.local.entity.AnimeEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AnimeDao_Impl implements AnimeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AnimeEntity> __insertionAdapterOfAnimeEntity;

  public AnimeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAnimeEntity = new EntityInsertionAdapter<AnimeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `animes` (`id`,`title`,`englishTitle`,`synopsis`,`imageUrl`,`bannerUrl`,`score`,`scoredBy`,`rank`,`popularity`,`episodes`,`episodeDurationMinutes`,`format`,`status`,`genresJson`,`studiosJson`,`broadcastDay`,`broadcastTime`,`broadcastTimezone`,`year`,`season`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AnimeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getEnglishTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEnglishTitle());
        }
        statement.bindString(4, entity.getSynopsis());
        statement.bindString(5, entity.getImageUrl());
        if (entity.getBannerUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getBannerUrl());
        }
        if (entity.getScore() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getScore());
        }
        if (entity.getScoredBy() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getScoredBy());
        }
        if (entity.getRank() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getRank());
        }
        if (entity.getPopularity() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getPopularity());
        }
        if (entity.getEpisodes() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getEpisodes());
        }
        if (entity.getEpisodeDurationMinutes() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getEpisodeDurationMinutes());
        }
        statement.bindString(13, entity.getFormat());
        statement.bindString(14, entity.getStatus());
        statement.bindString(15, entity.getGenresJson());
        statement.bindString(16, entity.getStudiosJson());
        if (entity.getBroadcastDay() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getBroadcastDay());
        }
        if (entity.getBroadcastTime() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getBroadcastTime());
        }
        if (entity.getBroadcastTimezone() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getBroadcastTimezone());
        }
        if (entity.getYear() == null) {
          statement.bindNull(20);
        } else {
          statement.bindLong(20, entity.getYear());
        }
        if (entity.getSeason() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getSeason());
        }
      }
    };
  }

  @Override
  public Object insertAnime(final AnimeEntity anime, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAnimeEntity.insert(anime);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAnimes(final List<AnimeEntity> animes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAnimeEntity.insert(animes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAnimeById(final int id, final Continuation<? super AnimeEntity> $completion) {
    final String _sql = "SELECT * FROM animes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AnimeEntity>() {
      @Override
      @Nullable
      public AnimeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfEnglishTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "englishTitle");
          final int _cursorIndexOfSynopsis = CursorUtil.getColumnIndexOrThrow(_cursor, "synopsis");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfBannerUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "bannerUrl");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfScoredBy = CursorUtil.getColumnIndexOrThrow(_cursor, "scoredBy");
          final int _cursorIndexOfRank = CursorUtil.getColumnIndexOrThrow(_cursor, "rank");
          final int _cursorIndexOfPopularity = CursorUtil.getColumnIndexOrThrow(_cursor, "popularity");
          final int _cursorIndexOfEpisodes = CursorUtil.getColumnIndexOrThrow(_cursor, "episodes");
          final int _cursorIndexOfEpisodeDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeDurationMinutes");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfGenresJson = CursorUtil.getColumnIndexOrThrow(_cursor, "genresJson");
          final int _cursorIndexOfStudiosJson = CursorUtil.getColumnIndexOrThrow(_cursor, "studiosJson");
          final int _cursorIndexOfBroadcastDay = CursorUtil.getColumnIndexOrThrow(_cursor, "broadcastDay");
          final int _cursorIndexOfBroadcastTime = CursorUtil.getColumnIndexOrThrow(_cursor, "broadcastTime");
          final int _cursorIndexOfBroadcastTimezone = CursorUtil.getColumnIndexOrThrow(_cursor, "broadcastTimezone");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfSeason = CursorUtil.getColumnIndexOrThrow(_cursor, "season");
          final AnimeEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new AnimeEntity(_tmpId,_tmpTitle,_tmpEnglishTitle,_tmpSynopsis,_tmpImageUrl,_tmpBannerUrl,_tmpScore,_tmpScoredBy,_tmpRank,_tmpPopularity,_tmpEpisodes,_tmpEpisodeDurationMinutes,_tmpFormat,_tmpStatus,_tmpGenresJson,_tmpStudiosJson,_tmpBroadcastDay,_tmpBroadcastTime,_tmpBroadcastTimezone,_tmpYear,_tmpSeason);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
