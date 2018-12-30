package id.web.jokopriyono.moviedicoding.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.web.jokopriyono.moviedicoding.data.response.genre.Genre;
import id.web.jokopriyono.moviedicoding.data.response.movie.ResultsItem;

import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.GenreColumns;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.MovieColumns;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.TABLE_GENRE;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.TABLE_MOVIE;

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
        dbHelper = null;
    }

    public boolean isOpen() {
        return dbHelper != null;
    }

    public List<Genre> selectGenre(int[] ids) {
        List<Genre> arrayList = new ArrayList<>();
        Cursor cursor;
        if (ids != null && ids.length != 0) {
            String[] idsGenre = Arrays.toString(ids).split("[\\[\\]]")[1].split(", ");
            String whereClause = "";
            for (int i = 0; i < ids.length; i++) {
                if (i == 0)
                    whereClause = whereClause.concat(GenreColumns.ID + "= ? ");
                else
                    whereClause = whereClause.concat("OR " + GenreColumns.ID + "= ? ");
            }
            cursor = database.query(TABLE_GENRE, null, whereClause, idsGenre, null, null, null, null);
        } else {
            cursor = database.query(TABLE_GENRE, null, null, null, null, null, null, null);
        }
        Genre genre;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
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

    public List<ResultsItem> selectMovie(@Nullable Integer idMovie) {
        List<ResultsItem> arrayList = new ArrayList<>();
        Cursor cursor;
        if (idMovie != null) {
            String[] idsGenre = {String.valueOf(idMovie)};
            String whereClause = MovieColumns.ID + "= ? ";
            cursor = database.query(TABLE_MOVIE, null, whereClause, idsGenre, null, null, null, null);
        } else {
            cursor = database.query(TABLE_MOVIE, null, null, null, null, null, null, null);
        }
        ResultsItem movie;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                movie = new ResultsItem();
                String genreDb = cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.GENRE_IDS));
                ArrayList<Integer> ids = new ArrayList<>();
                if (genreDb.equals("")) {
                    movie.setGenreIds(ids);
                } else {
                    String[] stringIds = genreDb.split(",");
                    for (String string : stringIds) {
                        ids.add(Integer.parseInt(string));
                    }
                    movie.setGenreIds(ids);
                }
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MovieColumns.ID)));
                movie.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(MovieColumns.VOTE_COUNT)));
                movie.setVideo(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.ID))));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(MovieColumns.VOTE_AVERAGE)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.TITLE)));
                movie.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(MovieColumns.POPULARITY)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.POSTER_PATH)));
                movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.ORIGINAL_LANG)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.ORIGINAL_TITLE)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.BACKDROP_PATH)));
                movie.setAdult(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.ADULT))));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.RELEASE_DATE)));
                arrayList.add(movie);
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

    public long insertMovie(ResultsItem movie) {
        String ids = "";
        for (int i = 0; i < movie.getGenreIds().size(); i++) {
            if (i == 0)
                ids = ids.concat("" + movie.getGenreIds().get(i));
            else
                ids = ids.concat("," + movie.getGenreIds().get(i));
        }

        ContentValues initialValues = new ContentValues();
        initialValues.put(MovieColumns.ID, movie.getId());
        initialValues.put(MovieColumns.VOTE_COUNT, movie.getVoteCount());
        initialValues.put(MovieColumns.VIDEO, movie.isVideo());
        initialValues.put(MovieColumns.VOTE_AVERAGE, movie.getVoteAverage());
        initialValues.put(MovieColumns.TITLE, movie.getTitle());
        initialValues.put(MovieColumns.POPULARITY, movie.getPopularity());
        initialValues.put(MovieColumns.POSTER_PATH, movie.getPosterPath());
        initialValues.put(MovieColumns.ORIGINAL_LANG, movie.getOriginalLanguage());
        initialValues.put(MovieColumns.ORIGINAL_TITLE, movie.getOriginalTitle());
        initialValues.put(MovieColumns.GENRE_IDS, ids);
        initialValues.put(MovieColumns.BACKDROP_PATH, movie.getBackdropPath());
        initialValues.put(MovieColumns.ADULT, movie.isAdult());
        initialValues.put(MovieColumns.OVERVIEW, movie.getOverview());
        initialValues.put(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
        return database.insert(TABLE_MOVIE, null, initialValues);
    }

    public int deleteAllGenre() {
        return database.delete(TABLE_GENRE, null, null);
    }

    public int deleteMovie(int idMovie) {
        String[] idsGenre = {"" + idMovie};
        String whereClause = MovieColumns.ID + "= ? ";
        return database.delete(TABLE_MOVIE, whereClause, idsGenre);
    }

    public Cursor providerSelectMovie(Integer idMovie) {
        if (idMovie != null) {
            return database.query(TABLE_MOVIE, null
                    , MovieColumns.ID + " = ?"
                    , new String[]{idMovie + ""}
                    , null, null, null, null);
        } else {
            return database.query(TABLE_MOVIE, null, null, null, null, null, null, null);
        }
    }

    public long providerInsertMovie(ContentValues values) {
        return database.insert(TABLE_MOVIE, null, values);
    }

    public int providerDeleteMovie(String idMovie) {
        return database.delete(TABLE_MOVIE, MovieColumns.ID + " = ?"
                , new String[]{idMovie});
    }
}
