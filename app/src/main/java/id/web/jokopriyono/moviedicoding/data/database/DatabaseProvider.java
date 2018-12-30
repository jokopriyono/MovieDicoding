package id.web.jokopriyono.moviedicoding.data.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.AUTHORITY;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.CONTENT_URI;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.TABLE_MOVIE;

public class DatabaseProvider extends ContentProvider {
    public static final int MOVIE = 1;
    public static final int MOVIE_ID = 2;
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.dicoding.mynotesapp/note
        uriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);

        // content://com.dicoding.mynotesapp/note/id
        uriMatcher.addURI(AUTHORITY,
                TABLE_MOVIE + "/#",
                MOVIE_ID);
    }

    private DatabaseMovie databaseMovie;

    @Override
    public boolean onCreate() {
        databaseMovie = new DatabaseMovie(getContext());
        databaseMovie.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                cursor = databaseMovie.providerSelectMovie(null);
                break;
            case MOVIE_ID:
                cursor = databaseMovie.providerSelectMovie(Integer.parseInt(uri.getLastPathSegment()));
                break;
            default:
                cursor = null;
                break;
        }
        if (cursor != null && getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long added;
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                added = databaseMovie.providerInsertMovie(contentValues);
                break;
            default:
                added = 0;
                break;
        }
        if (added > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deleted;
        switch (uriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = databaseMovie.providerDeleteMovie(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
