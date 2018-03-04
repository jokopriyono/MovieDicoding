package id.web.jokopriyono.moviedicoding.fragments;

/*
 * Created by Joko Priyono on 21/02/18.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.activities.SplashActivity;
import id.web.jokopriyono.moviedicoding.adapter.CariFilmAdapter;
import id.web.jokopriyono.moviedicoding.api.carifilm.CariResponse;
import id.web.jokopriyono.moviedicoding.database.LocalDB;

public class NowPlayingFragment extends Fragment {


    public NowPlayingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null)
            Log.d("pesan", "bundle2 gak null");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_movies, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        CariResponse response = LocalDB.getAllNowPlaying(getContext());
        if (response!=null) {
            CariFilmAdapter adapter = new CariFilmAdapter(response.getResults(), getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        return view;
    }
}
