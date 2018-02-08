package id.web.jokopriyono.moviedicoding.adapter;

/*
 * Created by Joko Priyono on 26/12/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.response.ResultsItem;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {
    private List<ResultsItem> dataFilm;
    private Context context;

    public FilmAdapter(List<ResultsItem> response, Context context) {
        this.dataFilm = response;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_film, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtJudul.setText(dataFilm.get(position).getTitle());
        if (dataFilm.get(position).getPosterPath() != null)
            Picasso.with(context).load(BuildConfig.ImageURL + dataFilm.get(position).getPosterPath()).into(holder.imgFilm);
    }

    @Override
    public int getItemCount() {
        return dataFilm.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtJudul;
        ImageView imgFilm;

        ViewHolder(final View view) {
            super(view);
            txtJudul = view.findViewById(R.id.txt_judul);
            imgFilm = view.findViewById(R.id.img_film);
        }
    }
}
