package id.web.jokopriyono.moviedicoding;

/*
 * Created by Joko Priyono on 08/02/2018.
 */

import id.web.jokopriyono.moviedicoding.response.CariResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("3/search/movie")
    Call<CariResponse> cariFilm(@Query("api_key") String apiKey,
                                @Query("query") String query,
                                @Query("include_adult") boolean includeAdult);
}
