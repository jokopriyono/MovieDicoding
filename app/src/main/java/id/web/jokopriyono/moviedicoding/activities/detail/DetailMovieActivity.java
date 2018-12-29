package id.web.jokopriyono.moviedicoding.activities.detail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.data.response.movie.ResultsItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            ResultsItem movie = (ResultsItem) getIntent().getExtras().getSerializable(INTENT_DATA);
            if (movie != null) showDetailMovie(movie);
        }

        fab.setOnClickListener(this);
    }

    private void showDetailMovie(ResultsItem movie) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                finish();
        }
    }
}
