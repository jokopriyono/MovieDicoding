package com.jokopriyono.moviedicodingv2;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    static final String TABLE_GENRE = "genre";
    static final String TABLE_MOVIE = "movie";
    static final String AUTHORITY = "id.web.jokopriyono.moviedicoding";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

    public static String getStringColumn(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getIntColumn(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static double getDoubleColumn(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    static final class GenreColumns implements BaseColumns {
        static String ID = "id";
        static String NAME = "name";
    }

    static final class MovieColumns implements BaseColumns {
        static String ID = "id";
        static String VOTE_COUNT = "vote_count";
        static String VIDEO = "video";
        static String VOTE_AVERAGE = "vote_average";
        static String TITLE = "title";
        static String POPULARITY = "popularity";
        static String POSTER_PATH = "poster_path";
        static String ORIGINAL_LANG = "original_lang";
        static String ORIGINAL_TITLE = "original_title";
        static String GENRE_IDS = "genre_ids";
        static String BACKDROP_PATH = "backdrop_path";
        static String ADULT = "adult";
        static String OVERVIEW = "overview";
        static String RELEASE_DATE = "release_date";
    }
}
