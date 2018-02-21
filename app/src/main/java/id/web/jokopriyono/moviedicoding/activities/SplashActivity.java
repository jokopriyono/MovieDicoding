package id.web.jokopriyono.moviedicoding.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jaredrummler.android.widget.AnimatedSvgView;

import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    private AnimatedSvgView animatedSvgView;
    private final static int SPLASH_TIME = 3000;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        animatedSvgView = findViewById(R.id.animate_svg_logo);
        animatedSvgView.postDelayed(new Runnable() {
            @Override
            public void run() {
                animatedSvgView.start();
            }
        }, 500);

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.ApiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), NavActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME);
    }


}
