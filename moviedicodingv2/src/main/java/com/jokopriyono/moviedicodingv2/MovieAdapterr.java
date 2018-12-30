package com.jokopriyono.moviedicodingv2;

/*
 * Created by Joko Priyono on 26/12/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jokopriyono.moviedicodingv2.DatabaseContract.MovieColumns;
import static com.jokopriyono.moviedicodingv2.DatabaseContract.getStringColumn;

public class MovieAdapterr extends CursorAdapter implements View.OnClickListener {
    @BindView(R.id.txt_judul)
    TextView txtJudul;
    @BindView(R.id.txt_deskripsi)
    TextView txtDeskripsi;
    @BindView(R.id.card_movie)
    CardView cardView;
    @BindView(R.id.img_film)
    ImageView imgMovie;

    public MovieAdapterr(Cursor cursor, Context context, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false);
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null) {
            ButterKnife.bind(this, view);
            cardView.setOnClickListener(this);

            String title, desc;
            if (getStringColumn(cursor, MovieColumns.TITLE).length() > 16)
                title = getStringColumn(cursor, MovieColumns.TITLE).substring(0, 16) + "..";
            else
                title = getStringColumn(cursor, MovieColumns.TITLE);

            if (getStringColumn(cursor, MovieColumns.OVERVIEW).length() > 30)
                desc = getStringColumn(cursor, MovieColumns.OVERVIEW).substring(0, 30) + "..";
            else
                desc = getStringColumn(cursor, MovieColumns.OVERVIEW);

            txtJudul.setText(title);
            txtDeskripsi.setText(desc);

            Picasso.get().load("https://image.tmdb.org/t/p/w500" + getStringColumn(cursor, MovieColumns.POSTER_PATH)).into(imgMovie);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
