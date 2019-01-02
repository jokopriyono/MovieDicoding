package id.web.jokopriyono.moviedicoding.activities.main;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.web.jokopriyono.moviedicoding.ApiServices;
import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.CommonHelper;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.adapter.MovieAdapter;
import id.web.jokopriyono.moviedicoding.data.response.movie.MoviesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NowPlayingFragment extends Fragment implements Callback<MoviesResponse>, SwipeRefreshLayout.OnRefreshListener {
    private static final String BUNDLE_DATA = "movies";

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.relativelayout)
    RelativeLayout relativeLayout;

    @BindView(R.id.recycler_now_play)
    RecyclerView recyclerView;

    private Gson gson = new Gson();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.ApiURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private ApiServices services = retrofit.create(ApiServices.class);
    private MoviesResponse moviesResponse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_play, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        refreshLayout.setOnRefreshListener(this);

        if (savedInstanceState != null) {
            moviesResponse = (MoviesResponse) savedInstanceState.getSerializable(BUNDLE_DATA);
            if (moviesResponse != null) {
                setAdapterMovie(moviesResponse);
            }
        } else {
            getNowPlaying(getContext());
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(BUNDLE_DATA, moviesResponse);
        super.onSaveInstanceState(outState);
    }

    private void getNowPlaying(Context context) {
        refreshLayout.setRefreshing(true);
        if (CommonHelper.checkInternet(context)) {
            Call<MoviesResponse> call = services.nowPlaying(BuildConfig.ApiKey, "1");
            call.enqueue(this);
        } else {
            Toast.makeText(context, R.string.error_no_conn, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapterMovie(MoviesResponse movies) {
        moviesResponse = movies;
        MovieAdapter adapter = new MovieAdapter(movies.getResults(), getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
        refreshLayout.setRefreshing(false);

        if (response.body() != null) {
            if (response.body().getResults().size() > 0) {
                setAdapterMovie(response.body());
            } else {
                Snackbar.make(recyclerView, "Tidak ada data", Snackbar.LENGTH_SHORT);
            }
        } else {
            Snackbar.make(recyclerView, "Respon null", Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
        refreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        t.printStackTrace();
    }

    @Override
    public void onRefresh() {
        if (getContext() != null) getNowPlaying(getContext());
    }
}
