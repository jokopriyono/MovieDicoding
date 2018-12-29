package id.web.jokopriyono.moviedicoding.activities.main;

import android.os.Bundle;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.bottom_view)
    BottomNavigationView bottom;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int navPosition = 0;
    private Gson gson = new Gson();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.ApiURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private ApiServices services = retrofit.create(ApiServices.class);
    private DatabaseMovie databaseMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getGenre();

        toolbar.setTitle(R.string.now_playing);
        loadFragment(new NowPlayingFragment());

        bottom.setSelectedItemId(R.id.menu_last);
        bottom.setOnNavigationItemSelectedListener(this);
    }

    private void getGenre() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseMovie = new DatabaseMovie(getApplicationContext());
                databaseMovie.open();
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
                                }
                                databaseMovie.close();
                            }

                            @Override
                            public void onFailure(@NonNull Call<GenreResponse> call, @NonNull Throwable t) {
                                databaseMovie.close();
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
                    toolbar.setTitle(R.string.now_playing);
                    loadFragment(new NowPlayingFragment());
                    navPosition = 0;
                }
                return true;
            case R.id.menu_next:
                if (navPosition != 1) {
                    toolbar.setTitle(R.string.up_coming);
                    loadFragment(new NextComingFragment());
                    navPosition = 1;
                }
                return true;
            case R.id.menu_search:
                if (navPosition != 2) {
                    toolbar.setTitle(R.string.search);
                    loadFragment(new SearchFragment());
                    navPosition = 2;
                }
                return true;
            case R.id.menu_favorite:
                if (navPosition != 3) {
                    toolbar.setTitle(R.string.favorite);
                    loadFragment(new FavoriteFragment());
                    navPosition = 3;
                }
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
