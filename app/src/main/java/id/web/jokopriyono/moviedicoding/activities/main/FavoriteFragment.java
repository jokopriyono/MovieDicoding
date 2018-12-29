package id.web.jokopriyono.moviedicoding.activities.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.adapter.MovieAdapter;
import id.web.jokopriyono.moviedicoding.data.database.DatabaseMovie;
import id.web.jokopriyono.moviedicoding.data.response.movie.ResultsItem;

public class FavoriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private DatabaseMovie databaseMovie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);

        databaseMovie = new DatabaseMovie(getContext());
        databaseMovie.open();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        if (getActivity() != null) showAllFavorite(getActivity());

        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseMovie.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) showAllFavorite(getActivity());
    }

    private void showAllFavorite(final Activity activity) {
        swipeRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<ResultsItem> movies = databaseMovie.selectMovie(null);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showRecycler(movies);
                    }
                });
            }
        }).start();
    }

    private void showRecycler(List<ResultsItem> movies) {
        swipeRefreshLayout.setRefreshing(false);
        if (movies.size() > 0) {
            MovieAdapter adapter = new MovieAdapter(movies, getContext());
            recyclerView.setAdapter(adapter);
        } else {
            Snackbar.make(recyclerView, R.string.nothing_to_show, Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onRefresh() {
        if (getActivity() != null) showAllFavorite(getActivity());
    }
}
