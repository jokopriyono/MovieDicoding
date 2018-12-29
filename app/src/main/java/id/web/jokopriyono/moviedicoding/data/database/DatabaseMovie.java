package id.web.jokopriyono.moviedicoding.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.web.jokopriyono.moviedicoding.data.response.genre.Genre;

import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.GenreColumns;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.TABLE_GENRE;

public class DatabaseMovie {
    private Context context;
    private DBHelper dbHelper;

    private SQLiteDatabase database;

    public DatabaseMovie(Context context) {
        this.context = context;
    }

    public DatabaseMovie open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public List<Genre> selectGenre(int[] ids) {
        List<Genre> arrayList = new ArrayList<>();
        Cursor cursor;
        if (ids != null) {
            String[] idsGenre = Arrays.toString(ids).split("[\\[\\]]")[1].split(", ");
            String whereClause = "";
            for (int i = 0; i <= ids.length; i++) {
                if (i == 0)
                    whereClause = whereClause.concat(GenreColumns.ID + ids[i] + "= ? ");
                else
                    whereClause = whereClause.concat("OR " + GenreColumns.ID + ids[i] + "= ? ");
            }
            cursor = database.query(TABLE_GENRE, null, whereClause, idsGenre, null, null, null, null);
        } else {
            cursor = database.query(TABLE_GENRE, null, null, null, null, null, null, null);
        }
        cursor.moveToFirst();
        Genre genre;
        if (cursor.getCount() > 0) {
            do {
                genre = new Genre(cursor.getInt(cursor.getColumnIndexOrThrow(GenreColumns.ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(GenreColumns.NAME)));
                arrayList.add(genre);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertGenre(Genre genre) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(GenreColumns.ID, genre.getId());
        initialValues.put(GenreColumns.NAME, genre.getName());
        return database.insert(TABLE_GENRE, null, initialValues);
    }

    public int deleteAllGenre() {
        return database.delete(TABLE_GENRE, null, null);
    }

}
