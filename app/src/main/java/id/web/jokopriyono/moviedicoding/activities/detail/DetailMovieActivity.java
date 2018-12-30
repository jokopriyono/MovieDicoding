package id.web.jokopriyono.moviedicoding.activities.detail;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tommykw.tagview.DataTransform;
import com.github.tommykw.tagview.TagView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.data.database.DatabaseMovie;
import id.web.jokopriyono.moviedicoding.data.response.genre.Genre;
import id.web.jokopriyono.moviedicoding.data.response.movie.ResultsItem;

import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.CONTENT_URI;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.MovieColumns;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String INTENT_DATA = "movie";

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_rating)
    TextView txtRating;
    @BindView(R.id.txt_adult)
    TextView txtAdult;
    @BindView(R.id.txt_lang)
    TextView txtLang;
    @BindView(R.id.txt_release)
    TextView txtRelease;
    @BindView(R.id.txt_overview)
    TextView txtOverview;

    @BindView(R.id.img_poster)
    ImageView imgPoster;

    @BindView(R.id.fab_exit)
    FloatingActionButton fab;
    @BindView(R.id.fab_favorite)
    FloatingActionButton fabFav;

    @BindView(R.id.tagview)
    TagView<Genre> tagView;

    private DatabaseMovie databaseMovie;
    private boolean isFavorite = false;
    private ResultsItem movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        databaseMovie = new DatabaseMovie(getApplicationContext());
        databaseMovie.open();

        fabFav.setImageResource(R.drawable.v_star_unactive);

        if (getIntent().getExtras() != null) {
            movie = (ResultsItem) getIntent().getExtras().getSerializable(INTENT_DATA);
            if (movie != null) {
                showDetailMovie(movie);
                checkMovieDB(movie);
            }
        }

        fab.setOnClickListener(this);
        fabFav.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!databaseMovie.isOpen()) databaseMovie.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (databaseMovie.isOpen()) databaseMovie.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseMovie.isOpen()) databaseMovie.close();
    }

    private void showDetailMovie(ResultsItem movie) {
        setGenre(movie.getGenreIds());

        txtTitle.setText(movie.getOriginalTitle());
        txtRating.setText(String.valueOf(movie.getVoteAverage()));
        String adult = (movie.isAdult()) ? getString(R.string.yes) : getString(R.string.no);
        txtAdult.setText(adult);
        txtLang.setText(movie.getOriginalLanguage().toUpperCase());
        txtRelease.setText(movie.getReleaseDate());
        txtOverview.setText(movie.getOverview());

        Picasso.get().load(BuildConfig.ImageURL + movie.getPosterPath())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imgPoster);
    }

    private void checkMovieDB(final ResultsItem movie) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getId());
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        isFavorite = !isFavorite;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switchFavorite();
                            }
                        });
                    }
                    cursor.close();
                }
            }
        }).start();
    }

    private void setGenre(final List<Integer> ids) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int[] genreIds = new int[ids.size()];
                for (int i = 0; i < ids.size(); i++) {
                    genreIds[i] = ids.get(i);
                }
                final List<Genre> genres = databaseMovie.selectGenre(genreIds);
                if (genres.size() != 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTag(genres);
                        }
                    });
                }
            }
        }).start();
    }

    private void setTag(List<Genre> genres) {
        tagView.setTags(genres, new DataTransform<Genre>() {
            @NotNull
            @Override
            public String transfer(Genre genre) {
                return genre.getName();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_favorite:
                if (!isFavorite)
                    saveMovietoDB(movie);
                else
                    deleteMovieDB(movie);
                break;
            default:
                finish();
        }
    }

    private void saveMovietoDB(final ResultsItem movie) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ids = "";
                    for (int i = 0; i < movie.getGenreIds().size(); i++) {
                        if (i == 0)
                            ids = ids.concat("" + movie.getGenreIds().get(i));
                        else
                            ids = ids.concat("," + movie.getGenreIds().get(i));
                    }
                    ContentValues values = new ContentValues();
                    values.put(MovieColumns.ID, movie.getId());
                    values.put(MovieColumns.VOTE_COUNT, movie.getVoteCount());
                    values.put(MovieColumns.VIDEO, movie.isVideo());
                    values.put(MovieColumns.VOTE_AVERAGE, movie.getVoteAverage());
                    values.put(MovieColumns.TITLE, movie.getTitle());
                    values.put(MovieColumns.POPULARITY, movie.getPopularity());
                    values.put(MovieColumns.POSTER_PATH, movie.getPosterPath());
                    values.put(MovieColumns.ORIGINAL_LANG, movie.getOriginalLanguage());
                    values.put(MovieColumns.ORIGINAL_TITLE, movie.getOriginalTitle());
                    values.put(MovieColumns.GENRE_IDS, ids);
                    values.put(MovieColumns.BACKDROP_PATH, movie.getBackdropPath());
                    values.put(MovieColumns.ADULT, movie.isAdult());
                    values.put(MovieColumns.OVERVIEW, movie.getOverview());
                    values.put(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
                    getContentResolver().insert(CONTENT_URI, values);
                    isFavorite = !isFavorite;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switchFavorite();
                            Toast.makeText(DetailMovieActivity.this, R.string.add_fav_success, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailMovieActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }).start();
    }

    private void switchFavorite() {
        if (isFavorite)
            fabFav.setImageResource(R.drawable.v_star_active);
        else
            fabFav.setImageResource(R.drawable.v_star_unactive);
    }

    private void deleteMovieDB(final ResultsItem movie) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int result = databaseMovie.deleteMovie(movie.getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result == 1) {
                            isFavorite = !isFavorite;
                            switchFavorite();
                            Toast.makeText(DetailMovieActivity.this, R.string.remove_fav_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailMovieActivity.this, R.string.remove_fav_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
}
