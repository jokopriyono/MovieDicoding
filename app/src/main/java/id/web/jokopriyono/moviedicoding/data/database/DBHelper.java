package id.web.jokopriyono.moviedicoding.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.GenreColumns;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.MovieColumns;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.TABLE_GENRE;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.TABLE_MOVIE;

class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL)",
            TABLE_GENRE,
            GenreColumns.ID,
            GenreColumns.NAME
    );
    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL," +
                    " %s TEXT NULL)",
            TABLE_MOVIE,
            MovieColumns.ID,
            MovieColumns.VOTE_COUNT,
            MovieColumns.VIDEO,
            MovieColumns.VOTE_AVERAGE,
            MovieColumns.TITLE,
            MovieColumns.POPULARITY,
            MovieColumns.POSTER_PATH,
            MovieColumns.ORIGINAL_LANG,
            MovieColumns.ORIGINAL_TITLE,
            MovieColumns.GENRE_IDS,
            MovieColumns.BACKDROP_PATH,
            MovieColumns.ADULT,
            MovieColumns.OVERVIEW,
            MovieColumns.RELEASE_DATE
    );
    private static String DATABASE_NAME = "dbmoviedicoding";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
