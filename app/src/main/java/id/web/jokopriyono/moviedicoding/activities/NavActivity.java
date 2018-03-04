package id.web.jokopriyono.moviedicoding.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.MethodHelper;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.api.ApiServices;
import id.web.jokopriyono.moviedicoding.api.carifilm.CariResponse;
import id.web.jokopriyono.moviedicoding.database.LocalDB;
import id.web.jokopriyono.moviedicoding.fragments.NowPlayingFragment;
import id.web.jokopriyono.moviedicoding.fragments.SettingFragment;
import id.web.jokopriyono.moviedicoding.fragments.UpComingFragment;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static id.web.jokopriyono.moviedicoding.activities.SplashActivity.NOW_PLAYING;
import static id.web.jokopriyono.moviedicoding.activities.SplashActivity.UP_COMING;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG_NOW_PLAYING = "nowplaying";
    private static final String TAG_UP_COMING = "upcoming";
    private static final String TAG_SETTING = "setting";
    private static String CURRENT_TAG = TAG_NOW_PLAYING;

    private static int navItemIndex = 0;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private CariResponse upComingResponse;
    private CariResponse nowPlayingResponse;
    private ApiServices services;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        loadMovies();

        navigationView.setNavigationItemSelectedListener(this);
    }

    protected void loadMovies(){
        navItemIndex = 0;
        CURRENT_TAG = TAG_NOW_PLAYING;
        loadHomeFragment();
    }

    private void loadHomeFragment() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        Fragment fragment = pilihFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_fragment, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment pilihFragment() {
        switch (navItemIndex){
            case 0:
                setTitle(R.string.now_playing);
                return new NowPlayingFragment();
            case 1:
                setTitle(R.string.up_coming);
                return new UpComingFragment();
            case 2:
                setTitle(R.string.setting);
                return new SettingFragment();
            default:
                setTitle(R.string.now_playing);
                return new NowPlayingFragment();
        }
    }

    GetTaskHandler handler = new GetTaskHandler(this);
    private static class GetTaskHandler extends Handler {
        private final WeakReference<NavActivity> weakReference;
        private final Activity activity;

        GetTaskHandler(NavActivity activity) {
            weakReference = new WeakReference<>(activity);
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference.get() != null) {
                if (msg != null) {
                    CariResponse upComing = (CariResponse)msg.getData().getSerializable(UP_COMING);
                    if (upComing!=null)
                        LocalDB.saveUpComing(activity, (CariResponse) msg.getData().getSerializable(UP_COMING));
                    CariResponse nowPlaying = (CariResponse)msg.getData().getSerializable(NOW_PLAYING);
                    if (nowPlaying!=null)
                        LocalDB.saveNowPlaying(activity, (CariResponse) msg.getData().getSerializable(NOW_PLAYING));

                    ((NavActivity)activity).hideLoading();
                    ((NavActivity)activity).loadMovies();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            startActivity(new Intent(this, CariActivity.class));
            return true;
        } else if (id == R.id.action_refresh){
            showLoading();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.ApiURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            services = retrofit.create(ApiServices.class);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    Bundle result =new Bundle();
                    if (MethodHelper.checkInternet(getApplicationContext())) {
                        result.putSerializable(NOW_PLAYING, getNowPlaying());
                        result.putSerializable(UP_COMING, getUpComing());
                    }
                    msg.setData(result);
                    handler.sendMessage(msg);
                }
            }).start();
        }

        return super.onOptionsItemSelected(item);
    }

    private CariResponse getUpComing(){
        Call<CariResponse> responseCall = services.upComing(BuildConfig.ApiKey);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            return responseCall.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private CariResponse getNowPlaying(){
        Call<CariResponse> responseCall = services.nowPlaying(BuildConfig.ApiKey);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            return responseCall.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_now_playing) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_NOW_PLAYING;
                loadHomeFragment();
            }
        } else if (id == R.id.nav_up_coming) {
            if (navItemIndex != 1) {
                navItemIndex = 1;
                CURRENT_TAG = TAG_UP_COMING;
                loadHomeFragment();
            }
        } else if (id == R.id.nav_setting){
            if (navItemIndex != 2) {
                navItemIndex = 2;
                CURRENT_TAG = TAG_SETTING;
                loadHomeFragment();
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLoading(){
        if (loading==null) {
            loading = new ProgressDialog(this);
            loading.setCancelable(false);
            loading.setMessage("Tunggu gan..");
            loading.show();
        }
    }

    protected void hideLoading(){
        if (loading!=null){
            loading.dismiss();
            loading = null;
        }
    }
}
