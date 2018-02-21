package id.web.jokopriyono.moviedicoding;

/*
 * Created by Joko Priyono on 08/02/2018.
 */

import id.web.jokopriyono.moviedicoding.response.carifilm.CariResponse;
import id.web.jokopriyono.moviedicoding.response.nowplaying.NowPlayingResponse;
import id.web.jokopriyono.moviedicoding.response.upcoming.UpComingResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("3/search/movie")
    Call<CariResponse> cariFilm(@Query("api_key") String apiKey,
                                @Query("query") String query);

    @GET("3/movie/now_playing")
    Call<NowPlayingResponse> nowPlaying(@Query("api_key") String apiKey);

    @GET("3/movie/upcoming")
    Call<UpComingResponse> upComing(@Query("api_key") String apiKey);
}
