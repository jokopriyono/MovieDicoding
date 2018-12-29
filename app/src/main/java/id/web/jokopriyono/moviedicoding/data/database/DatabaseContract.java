package id.web.jokopriyono.moviedicoding.data.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import id.web.jokopriyono.moviedicoding.BuildConfig;

public class DatabaseContract {
    static final String TABLE_GENRE = "genre";
    static final String TABLE_MOVIE = "movie";
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID;
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_GENRE)
            .build();

    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static double getDouble(Cursor cursor, String columnName) {
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
