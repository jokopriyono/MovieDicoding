package id.web.jokopriyono.moviedicoding.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jaredrummler.android.widget.AnimatedSvgView;

import java.io.IOException;
import java.lang.ref.WeakReference;

import id.web.jokopriyono.moviedicoding.ApiServices;
import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.MethodHelper;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.response.nowplaying.NowPlayingResponse;
import id.web.jokopriyono.moviedicoding.response.upcoming.UpComingResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    private AnimatedSvgView animatedSvgView;
    private final static int ANIMATION_TIME = 500;
    private ApiServices services;
    public static final String NOW_PLAYING = "nowplaying";
    public static final String UP_COMING = "upcoming";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        animatedSvgView = findViewById(R.id.animate_svg_logo);
        ProgressBar loading = findViewById(R.id.progress_bar);
        animatedSvgView.postDelayed(new Runnable() {
            @Override
            public void run() {
                animatedSvgView.start();
            }
        }, ANIMATION_TIME);
        loading.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.ApiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        services = retrofit.create(ApiServices.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("pesan", "masuk");
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
    }

    GetTaskHandler handler = new GetTaskHandler(this);
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
                        i.putExtra(UP_COMING, msg.getData().getSerializable(UP_COMING));
                    }
                    if (msg.getData().getSerializable(NOW_PLAYING) != null) {
                        i.putExtra(NOW_PLAYING, msg.getData().getSerializable(NOW_PLAYING));
                    }
                    activity.startActivity(i);
                    activity.finish();
                }
            }
        }
    }

    private UpComingResponse getUpComing(){
        Call<UpComingResponse> responseCall = services.upComing(BuildConfig.ApiKey);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            return responseCall.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private NowPlayingResponse getNowPlaying(){
        Call<NowPlayingResponse> responseCall = services.nowPlaying(BuildConfig.ApiKey);
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
