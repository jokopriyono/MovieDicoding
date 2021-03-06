package id.web.jokopriyono.moviedicoding.adapter;

/*
 * Created by Joko Priyono on 26/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.activities.detail.DetailMovieActivity;
import id.web.jokopriyono.moviedicoding.data.response.movie.ResultsItem;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<ResultsItem> dataFilm;
    private Context context;

    public MovieAdapter(List<ResultsItem> response, Context context) {
        this.dataFilm = response;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String title, desc;
        if (dataFilm.get(position).getTitle().length() > 16)
            title = dataFilm.get(position).getTitle().substring(0, 16) + "..";
        else
            title = dataFilm.get(position).getTitle();

        if (dataFilm.get(position).getOverview().length() > 30)
            desc = dataFilm.get(position).getOverview().substring(0, 30) + "..";
        else
            desc = dataFilm.get(position).getOverview();

        holder.txtJudul.setText(title);
        holder.txtDeskripsi.setText(desc);

        Picasso.get().load(BuildConfig.ImageURL + dataFilm.get(position).getPosterPath()).into(holder.imgMovie);
    }

    @Override
    public int getItemCount() {
        return dataFilm.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_judul)
        TextView txtJudul;
        @BindView(R.id.txt_deskripsi)
        TextView txtDeskripsi;
        @BindView(R.id.card_movie)
        CardView cardView;
        @BindView(R.id.img_film)
        ImageView imgMovie;

        ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, DetailMovieActivity.class);
            i.putExtra(DetailMovieActivity.INTENT_DATA, dataFilm.get(getAdapterPosition()));
            context.startActivity(i);
        }
    }
}
