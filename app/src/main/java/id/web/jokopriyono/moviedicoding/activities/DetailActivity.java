package id.web.jokopriyono.moviedicoding.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.api.carifilm.ResultsItem;

public class DetailActivity extends AppCompatActivity {
    public static final String DATA_INTENT = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView txtRilis = findViewById(R.id.txt_release_date);
        TextView txtPopularitas = findViewById(R.id.txt_popularity);
        TextView txtBahasa = findViewById(R.id.txt_language);
        TextView txtDesc = findViewById(R.id.txt_desc);
        RatingBar ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setNumStars(5);
        ImageView imgFilm = findViewById(R.id.img_film);

        if (getIntent()!=null){
            ResultsItem dataItem = (ResultsItem) getIntent().getSerializableExtra(DATA_INTENT);
            if (dataItem !=null) {
                if (dataItem.getPosterPath() != null)
                    Picasso.with(this).load(BuildConfig.ImageURL + dataItem.getPosterPath()).into(imgFilm);
                if (dataItem.getTitle() != null)
                    toolbar.setTitle(dataItem.getTitle());
                if (dataItem.getReleaseDate() != null) {
                    txtRilis.setText(dataItem.getReleaseDate());
                }
                if (dataItem.getPopularity() != null) {
                    String text = dataItem.getPopularity().toString();
                    txtPopularitas.setText(text);
                }
                if (dataItem.getOriginalLanguage() != null) {
                    txtBahasa.setText(dataItem.getOriginalLanguage());
                }
                if (dataItem.getVoteAverage()!=null)
                    ratingBar.setRating(dataItem.getVoteAverage().floatValue() / 2);
                if (dataItem.getOverview()!=null)
                    txtDesc.setText(dataItem.getOverview());
            }
        }
    }
}
