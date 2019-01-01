package id.web.jokopriyono.moviedicoding.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.R;

import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.CONTENT_URI;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.MovieColumns;
import static id.web.jokopriyono.moviedicoding.data.database.DatabaseContract.getStringColumn;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<String> urlMovie = new ArrayList<>();
    private Context mContext;

    StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        selectAllData();
    }

    @Override
    public void onDataSetChanged() {

    }

    private void selectAllData() {
        final Cursor cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    String url = BuildConfig.ImageURL + getStringColumn(cursor, MovieColumns.BACKDROP_PATH);
                    urlMovie.add(url);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return urlMovie.size();
    }

    @Override
    public RemoteViews getViewAt(int pos) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        try {
            Bitmap bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(urlMovie.get(pos))
                    .apply(new RequestOptions().centerCrop())
                    .submit()
                    .get();
            rv.setImageViewBitmap(R.id.imageView, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putInt(MovieWidget.EXTRA_ITEM, pos);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
