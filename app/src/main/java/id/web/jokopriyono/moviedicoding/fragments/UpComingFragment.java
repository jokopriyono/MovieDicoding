package id.web.jokopriyono.moviedicoding.fragments;

/*
 * Created by Joko Priyono on 21/02/18.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.activities.SplashActivity;
import id.web.jokopriyono.moviedicoding.adapter.CariFilmAdapter;
import id.web.jokopriyono.moviedicoding.api.carifilm.CariResponse;

public class UpComingFragment extends Fragment {
    public UpComingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_movies, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Bundle bundle = this.getArguments();
        if (bundle!=null) {
            CariResponse response = (CariResponse) bundle.getSerializable(SplashActivity.UP_COMING);
            if (response!=null) {
                CariFilmAdapter adapter = new CariFilmAdapter(response.getResults(), getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
        return view;
    }
}
