package id.web.jokopriyono.moviedicoding.fragments;

/*
 * Created by Joko Priyono on 21/02/18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.adapter.CariFilmAdapter;
import id.web.jokopriyono.moviedicoding.api.carifilm.CariResponse;
import id.web.jokopriyono.moviedicoding.database.LocalDB;

public class SettingFragment extends Fragment {
    public SettingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_setting, container, false);

        Button btnChange = view.findViewById(R.id.btn_change);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }
        });

        return view;
    }
}
