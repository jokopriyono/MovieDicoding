package id.web.jokopriyono.moviedicoding.data.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import id.web.jokopriyono.moviedicoding.BuildConfig;

public class DatabaseContract {
    static final String TABLE_GENRE = "genre";
    static final String TABLE_MOVIE = "movie";
    static final String AUTHORITY = BuildConfig.APPLICATION_ID;
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

    public static final class MovieColumns implements BaseColumns {
        public static String ID = "id";
        public static String VOTE_COUNT = "vote_count";
        public static String VIDEO = "video";
        public static String VOTE_AVERAGE = "vote_average";
        public static String TITLE = "title";
        public static String POPULARITY = "popularity";
        public static String POSTER_PATH = "poster_path";
        public static String ORIGINAL_LANG = "original_lang";
        public static String ORIGINAL_TITLE = "original_title";
        public static String GENRE_IDS = "genre_ids";
        public static String BACKDROP_PATH = "backdrop_path";
        public static String ADULT = "adult";
        public static String OVERVIEW = "overview";
        public static String RELEASE_DATE = "release_date";
    }
}
