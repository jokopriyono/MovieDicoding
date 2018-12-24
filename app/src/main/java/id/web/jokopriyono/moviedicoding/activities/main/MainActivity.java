package id.web.jokopriyono.moviedicoding.activities.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import id.web.jokopriyono.moviedicoding.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private int navPosition = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new SearchFragment());

        BottomNavigationView bottom = findViewById(R.id.bottom_view);
        bottom.setSelectedItemId(R.id.menu_search);
        bottom.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_last:
                if (navPosition != 0) {
                    loadFragment(new NowPlayingFragment());
                    navPosition = 0;
                }
                return true;
            case R.id.menu_next:
                if (navPosition != 1) {
                    loadFragment(new NextComingFragment());
                    navPosition = 1;
                }
                return true;
            case R.id.menu_search:
                if (navPosition != 2) {
                    loadFragment(new SearchFragment());
                    navPosition = 2;
                }
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
