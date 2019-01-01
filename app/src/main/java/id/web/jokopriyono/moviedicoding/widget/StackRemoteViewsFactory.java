package id.web.jokopriyono.moviedicoding.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import id.web.jokopriyono.moviedicoding.R;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Bitmap> mWidgetItems = new ArrayList<>();
    private Context mContext;
//    private int mAppWidgetId;

    StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
//        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        for (int i = 0; i < 6; i++) {
            mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.example_appwidget_preview));
        }
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int pos) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(pos));

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
