package id.web.jokopriyono.moviedicoding.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.web.jokopriyono.moviedicoding.ApiServices;
import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.CommonHelper;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.data.database.DatabaseMovie;
import id.web.jokopriyono.moviedicoding.data.response.genre.Genre;
import id.web.jokopriyono.moviedicoding.data.response.genre.GenreResponse;
import id.web.jokopriyono.moviedicoding.data.sharedpref.UserPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener {
    private static final String TAG_NOW_PLAY = "fragmentnowplay";
    private static final String TAG_UP_COMING = "fragmentupcoming";
    private static final String TAG_SEARCH = "fragmentsearch";
    private static final String TAG_FAVORITE = "fragmentfavorite";
    private static final String TAG_ACTIVE = "fragmentfavorite";

    @BindView(R.id.bottom_view)
    BottomNavigationView bottom;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String TAG_FRAGMENT = TAG_NOW_PLAY;
    private int navPosition;
    private Gson gson = new Gson();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.ApiURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private ApiServices services = retrofit.create(ApiServices.class);
    private DatabaseMovie databaseMovie;
    private UserPreferences preferences;
    private NowPlayingFragment nowPlayingFragment;
    private NextComingFragment upComingFragment;
    private SearchFragment searchFragment;
    private FavoriteFragment favoriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.inflateMenu(R.menu.menu_setting);
        preferences = new UserPreferences(getApplicationContext());
        databaseMovie = new DatabaseMovie(getApplicationContext());
        databaseMovie.open();

        bottom.setOnNavigationItemSelectedListener(this);
        toolbar.setOnMenuItemClickListener(this);

        if (!preferences.getGenres())
            getGenre();

        if (savedInstanceState != null) {
            TAG_FRAGMENT = savedInstanceState.getString(TAG_ACTIVE);
            if (TAG_FRAGMENT != null) {
                selectFragmentState(TAG_FRAGMENT);
            }
        } else {
            toolbar.setTitle(R.string.now_playing);
            nowPlayingFragment = new NowPlayingFragment();
            loadFragment(nowPlayingFragment, TAG_NOW_PLAY);
            navPosition = 0;
            bottom.setSelectedItemId(R.id.menu_last);
        }
    }

    private void selectFragmentState(String tagFragment) {
        switch (tagFragment) {
            case TAG_NOW_PLAY:
                toolbar.setTitle(R.string.now_playing);
                nowPlayingFragment = (NowPlayingFragment)
                        getSupportFragmentManager().findFragmentByTag(TAG_NOW_PLAY);
                navPosition = 0;
                bottom.setSelectedItemId(R.id.menu_last);
                break;
            case TAG_UP_COMING:
                toolbar.setTitle(R.string.up_coming);
                upComingFragment = (NextComingFragment)
                        getSupportFragmentManager().findFragmentByTag(TAG_UP_COMING);
                navPosition = 1;
                bottom.setSelectedItemId(R.id.menu_next);
                break;
            case TAG_SEARCH:
                toolbar.setTitle(R.string.search);
                searchFragment = (SearchFragment)
                        getSupportFragmentManager().findFragmentByTag(TAG_SEARCH);
                navPosition = 2;
                bottom.setSelectedItemId(R.id.menu_search);
                break;
            case TAG_FAVORITE:
                toolbar.setTitle(R.string.favorite);
                favoriteFragment = (FavoriteFragment)
                        getSupportFragmentManager().findFragmentByTag(TAG_FAVORITE);
                navPosition = 3;
                bottom.setSelectedItemId(R.id.menu_favorite);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(TAG_ACTIVE, TAG_FRAGMENT);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseMovie.close();
    }

    private void getGenre() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (databaseMovie.selectGenre(null).size() == 0) {
                    if (CommonHelper.checkInternet(getApplicationContext())) {
                        Call<GenreResponse> call = services.getGenre(BuildConfig.ApiKey);
                        call.enqueue(new Callback<GenreResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<GenreResponse> call, @NonNull Response<GenreResponse> response) {
                                if (response.body() != null) {
                                    for (Genre genre : response.body().getGenres()) {
                                        databaseMovie.insertGenre(genre);
                                    }
                                    preferences.setGenres();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<GenreResponse> call, @NonNull Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_last:
                if (navPosition != 0) {
                    TAG_FRAGMENT = TAG_NOW_PLAY;
                    toolbar.setTitle(R.string.now_playing);
                    nowPlayingFragment = new NowPlayingFragment();
                    loadFragment(nowPlayingFragment, TAG_NOW_PLAY);
                    navPosition = 0;
                }
                return true;
            case R.id.menu_next:
                if (navPosition != 1) {
                    TAG_FRAGMENT = TAG_UP_COMING;
                    toolbar.setTitle(R.string.up_coming);
                    upComingFragment = new NextComingFragment();
                    loadFragment(upComingFragment, TAG_UP_COMING);
                    navPosition = 1;
                }
                return true;
            case R.id.menu_search:
                if (navPosition != 2) {
                    TAG_FRAGMENT = TAG_SEARCH;
                    toolbar.setTitle(R.string.search);
                    searchFragment = new SearchFragment();
                    loadFragment(searchFragment, TAG_SEARCH);
                    navPosition = 2;
                }
                return true;
            case R.id.menu_favorite:
                if (navPosition != 3) {
                    TAG_FRAGMENT = TAG_FAVORITE;
                    toolbar.setTitle(R.string.favorite);
                    favoriteFragment = new FavoriteFragment();
                    loadFragment(favoriteFragment, TAG_FAVORITE);
                    navPosition = 3;
                }
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content, fragment, tag);
        transaction.commit();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(mIntent);

        return true;
    }
}
