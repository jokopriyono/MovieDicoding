package id.web.jokopriyono.moviedicoding.activities.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class SearchFragment extends Fragment implements Callback<MoviesResponse>, View.OnClickListener {
    private static final String BUNDLE_DATA = "movies";
    private static final String BUNDLE_QUERY = "query";

    @BindView(R.id.linearlayout)
    LinearLayout linearLayout;

    @BindView(R.id.btn_cari)
    Button btnCari;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.edt_cari)
    EditText edtCari;

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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        btnCari.setOnClickListener(this);

        if (savedInstanceState != null) {
            String query = savedInstanceState.getString(BUNDLE_QUERY);
            moviesResponse = (MoviesResponse) savedInstanceState.getSerializable(BUNDLE_DATA);
            if (moviesResponse != null) {
                progressBar.setVisibility(View.GONE);
                setAdapterMovie(moviesResponse);
            }
            edtCari.setText(query);
        } else {
            searchMovie(getContext(), "Spiderman");
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(BUNDLE_DATA, moviesResponse);
        outState.putString(BUNDLE_QUERY, edtCari.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void searchMovie(Context context, String query) {
        progressBar.setVisibility(View.VISIBLE);
        if (CommonHelper.checkInternet(context)) {
            Call<MoviesResponse> call = services.searchMovie(BuildConfig.ApiKey, query, false);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
        progressBar.setVisibility(View.GONE);

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
        progressBar.setVisibility(View.GONE);
        Snackbar.make(linearLayout, t.getLocalizedMessage(), Snackbar.LENGTH_SHORT);
        t.printStackTrace();
    }

    @Override
    public void onClick(View view) {
        if (getContext() != null) searchMovie(getContext(), edtCari.getText().toString().trim());
    }
}
