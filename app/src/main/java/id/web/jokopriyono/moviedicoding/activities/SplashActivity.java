package id.web.jokopriyono.moviedicoding.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.jaredrummler.android.widget.AnimatedSvgView;

import java.lang.ref.WeakReference;

import id.web.jokopriyono.moviedicoding.api.ApiServices;
import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.MethodHelper;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.api.carifilm.CariResponse;
import id.web.jokopriyono.moviedicoding.database.DB;
import id.web.jokopriyono.moviedicoding.database.LocalDB;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    private final static int ANIMATION_TIME = 2000;
    private ApiServices services;
    public static final String NOW_PLAYING = "nowplaying";
    public static final String UP_COMING = "upcoming";
    private ProgressBar loading;
    private Thread thread;
    private static LocalDB localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AnimatedSvgView animatedSvgView = findViewById(R.id.animate_svg_logo);
        loading = findViewById(R.id.progress_bar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.ApiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        services = retrofit.create(ApiServices.class);

        thread = new Thread(new Runnable() {
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
        });
        animatedSvgView.start();
        animatedSvgView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.VISIBLE);
                thread.start();
            }
        }, ANIMATION_TIME);
    }

    private GetTaskHandler handler = new GetTaskHandler(this);
    private static class GetTaskHandler extends Handler {
        private final WeakReference<SplashActivity> weakReference;
        private final Activity activity;

        GetTaskHandler(SplashActivity activity) {
            weakReference = new WeakReference<>(activity);
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference.get() != null) {
                if (msg != null) {
                    Intent i = new Intent(activity, NavActivity.class);
                    if (msg.getData().getSerializable(UP_COMING) != null) {
                        LocalDB.saveUpComing(activity, (CariResponse) msg.getData().getSerializable(UP_COMING));
                    }
                    if (msg.getData().getSerializable(NOW_PLAYING) != null) {
                        LocalDB.saveNowPlaying(activity, (CariResponse) msg.getData().getSerializable(NOW_PLAYING));
                    }
                    activity.startActivity(i);
                    activity.finish();
                }
            }
        }
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
}
