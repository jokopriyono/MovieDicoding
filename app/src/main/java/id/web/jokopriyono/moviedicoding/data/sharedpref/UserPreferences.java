package id.web.jokopriyono.moviedicoding.data.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String MOVIE_PREF = "dicodingmoviepref";
    private static final String KEY_GENRES = "genres";

    private SharedPreferences preferences;

    public UserPreferences(Context context) {
        preferences = context.getSharedPreferences(MOVIE_PREF, Context.MODE_PRIVATE);
    }

    public void setGenres() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_GENRES, true);
        editor.apply();
    }

    public boolean getGenres() {
        return preferences.getBoolean(KEY_GENRES, false);
    }
}
