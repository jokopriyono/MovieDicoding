package id.web.jokopriyono.moviedicoding.api;

/*
 * Created by Joko Priyono on 08/02/2018.
 */

import id.web.jokopriyono.moviedicoding.api.carifilm.CariResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("3/search/movie")
    Call<CariResponse> cariFilm(@Query("api_key") String apiKey,
                                @Query("query") String query);

    @GET("3/movie/now_playing")
    Call<CariResponse> nowPlaying(@Query("api_key") String apiKey);

    @GET("3/movie/upcoming")
    Call<CariResponse> upComing(@Query("api_key") String apiKey);
}
