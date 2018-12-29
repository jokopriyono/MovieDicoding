package id.web.jokopriyono.moviedicoding.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_GENRE,
            DatabaseContract.GenreColumns.ID,
            DatabaseContract.GenreColumns.NAME
    );
    private static String DATABASE_NAME = "dbmoviedicoding";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
