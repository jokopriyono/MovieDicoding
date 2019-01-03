package id.web.jokopriyono.moviedicoding.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;

import java.util.Calendar;

import id.web.jokopriyono.moviedicoding.ApiServices;
import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.CommonHelper;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.data.response.movie.MoviesResponse;
import id.web.jokopriyono.moviedicoding.data.response.movie.ResultsItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NowPlayReceiver extends BroadcastReceiver implements Callback<MoviesResponse> {
    public static String CHANNEL_ID = "channel_now_play";
    public static CharSequence CHANNEL_NAME = "now play channel";

    private Gson gson = new Gson();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.ApiURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private ApiServices services = retrofit.create(ApiServices.class);
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (CommonHelper.checkInternet(context)) {
            Call<MoviesResponse> call = services.nowPlaying(BuildConfig.ApiKey, "1");
            call.enqueue(this);
        }
    }

    public void setRepeatingAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
//        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show();
    }

    private void showNotification(Context context, MoviesResponse response) {
        if (response.getResults().size() > 0) {
            ResultsItem movie = response.getResults().get(0);
            NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(movie.getTitle())
                    .setContentText(movie.getOverview())
                    .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(alarmSound);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
                builder.setChannelId(CHANNEL_ID);
                if (notificationManagerCompat != null) {
                    notificationManagerCompat.createNotificationChannel(channel);
                }
            }
            Notification notification = builder.build();
            if (notificationManagerCompat != null) {
                notificationManagerCompat.notify(1111, notification);
            }
        }
    }

    @Override
    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
        if (response.body() != null) {
            if (response.body().getResults().size() > 0) {
                showNotification(context, response.body());
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
        t.printStackTrace();
    }
}
