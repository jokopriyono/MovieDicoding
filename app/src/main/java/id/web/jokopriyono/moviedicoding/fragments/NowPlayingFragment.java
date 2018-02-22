package id.web.jokopriyono.moviedicoding.fragments;

/*
 * Created by Joko Priyono on 21/02/18.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import id.web.jokopriyono.moviedicoding.R;

public class NowPlayingFragment extends Fragment {
    public NowPlayingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_movies, container, false);

        Bundle bundle = this.getArguments();
        if (bundle!=null)
            Toast.makeText(getContext(), "Now play gak null", Toast.LENGTH_SHORT).show();

        return view;
    }
}
