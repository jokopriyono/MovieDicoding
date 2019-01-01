package com.jokopriyono.moviedicodingv2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jokopriyono.moviedicodingv2.DatabaseContract.CONTENT_URI;

public class FavoriteActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_fav)
    RecyclerView recyclerView;

    @BindView(R.id.swipelayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadAllFavorite();
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllFavorite();
    }

    private void loadAllFavorite() {
        swipeRefreshLayout.setRefreshing(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        final ArrayList<Movie> movies = new ArrayList<>();
                        for (int i = 0; i < cursor.getCount(); i++) {
                            Movie movie = new Movie(cursor);
                            movies.add(movie);
                            cursor.moveToNext();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MovieAdapter adapter = new MovieAdapter(movies, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    }
                    cursor.close();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onRefresh() {
        loadAllFavorite();
    }
}
