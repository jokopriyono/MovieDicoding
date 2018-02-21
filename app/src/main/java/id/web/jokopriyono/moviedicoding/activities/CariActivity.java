package id.web.jokopriyono.moviedicoding.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import id.web.jokopriyono.moviedicoding.ApiServices;
import id.web.jokopriyono.moviedicoding.BuildConfig;
import id.web.jokopriyono.moviedicoding.MethodHelper;
import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.adapter.FilmAdapter;
import id.web.jokopriyono.moviedicoding.response.CariResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CariActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtCari;
    private RecyclerView recycler;
    private LinearLayout linearLayout;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        linearLayout = findViewById(R.id.linear_kosong);
        Button btnCari = findViewById(R.id.btn_cari);
        btnCari.setOnClickListener(this);
        edtCari = findViewById(R.id.edt_cari);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cari:
                if (edtCari.getText().toString().equals(""))
                    Toast.makeText(this, "Kolom pencarian jangan lupa diisi gan", Toast.LENGTH_SHORT).show();
                else if (!MethodHelper.checkInternet(this))
                    Toast.makeText(this, "Wah cek koneksi internet dulu gan", Toast.LENGTH_SHORT).show();
                else {
                    linearLayout.setVisibility(View.GONE);
                    recycler.setVisibility(View.VISIBLE);
                    cariFilm(edtCari.getText().toString());
                }
                break;
        }
    }

    /**
     * Pengecekan query film yang akan dicari ke themoviedb.org
     * @param query keyword yang akan dicari
     */
    private void cariFilm(String query) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.ApiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServices services = retrofit.create(ApiServices.class);
        Call<CariResponse> result = services.cariFilm(BuildConfig.ApiKey, query);
        result.enqueue(new Callback<CariResponse>() {
            @Override
            public void onResponse(Call<CariResponse> call, Response<CariResponse> response) {
                loading.dismiss();
                if (response.body()!=null){
                    if (response.body().getResults() != null){
                        FilmAdapter adapter = new FilmAdapter(response.body().getResults(), getApplicationContext());
                        recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        recycler.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(CariActivity.this, "Wah query film begituan gak ada gan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    recycler.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(CariActivity.this, "Lah kok respon server null gan?", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CariResponse> call, Throwable t) {
                loading.dismiss();
                t.printStackTrace();
                Toast.makeText(CariActivity.this, "Nampaknya ada error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
