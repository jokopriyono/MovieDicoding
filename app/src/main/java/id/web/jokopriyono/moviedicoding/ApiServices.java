package id.web.jokopriyono.moviedicoding;

/*
 * Created by Joko Priyono on 08/02/2018.
 */

import id.web.jokopriyono.moviedicoding.response.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("3/search/movie")
    Call<MoviesResponse> searchMovie(@Query("api_key") String apiKey,
                                     @Query("query") String query,
                                     @Query("include_adult") boolean includeAdult);

    @GET("3/movie/now_playing")
    Call<MoviesResponse> nowPlaying(@Query("api_key") String apiKey,
                                    @Query("page") String page);

    @GET("3/movie/upcoming")
    Call<MoviesResponse> upComing(@Query("api_key") String apiKey,
                                  @Query("page") String page);
}
