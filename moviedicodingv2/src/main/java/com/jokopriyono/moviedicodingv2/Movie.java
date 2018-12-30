package com.jokopriyono.moviedicodingv2;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.jokopriyono.moviedicodingv2.DatabaseContract.MovieColumns;
import static com.jokopriyono.moviedicodingv2.DatabaseContract.getDoubleColumn;
import static com.jokopriyono.moviedicodingv2.DatabaseContract.getIntColumn;
import static com.jokopriyono.moviedicodingv2.DatabaseContract.getStringColumn;

public class Movie implements Serializable {

    @SerializedName("overview")
    private String overview;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("video")
    private boolean video;

    @SerializedName("title")
    private String title;

    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("id")
    private int id;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("vote_count")
    private int voteCount;

    public Movie() {
    }

    public Movie(Cursor cursor) {
        String genreDb = getStringColumn(cursor, MovieColumns.GENRE_IDS);
        ArrayList<Integer> ids = new ArrayList<>();
        if (genreDb.equals("")) {
            this.genreIds = ids;
        } else {
            String[] stringIds = genreDb.split(",");
            for (String string : stringIds) {
                ids.add(Integer.parseInt(string));
            }
            this.genreIds = ids;
        }

        this.id = getIntColumn(cursor, MovieColumns.ID);
        this.voteCount = getIntColumn(cursor, MovieColumns.VOTE_COUNT);
        this.video = Boolean.parseBoolean(getStringColumn(cursor, MovieColumns.VIDEO));
        this.voteAverage = getIntColumn(cursor, MovieColumns.VOTE_AVERAGE);
        this.title = getStringColumn(cursor, MovieColumns.TITLE);
        this.popularity = getDoubleColumn(cursor, MovieColumns.POPULARITY);
        this.posterPath = getStringColumn(cursor, MovieColumns.POSTER_PATH);
        this.originalLanguage = getStringColumn(cursor, MovieColumns.ORIGINAL_LANG);
        this.originalTitle = getStringColumn(cursor, MovieColumns.ORIGINAL_TITLE);
        this.backdropPath = getStringColumn(cursor, MovieColumns.BACKDROP_PATH);
        this.adult = Boolean.parseBoolean(getStringColumn(cursor, MovieColumns.ADULT));
        this.overview = getStringColumn(cursor, MovieColumns.OVERVIEW);
        this.releaseDate = getStringColumn(cursor, MovieColumns.RELEASE_DATE);

    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return
                "ResultsItem{" +
                        "overview = '" + overview + '\'' +
                        ",original_language = '" + originalLanguage + '\'' +
                        ",original_title = '" + originalTitle + '\'' +
                        ",video = '" + video + '\'' +
                        ",title = '" + title + '\'' +
                        ",genre_ids = '" + genreIds + '\'' +
                        ",poster_path = '" + posterPath + '\'' +
                        ",backdrop_path = '" + backdropPath + '\'' +
                        ",release_date = '" + releaseDate + '\'' +
                        ",vote_average = '" + voteAverage + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        ",id = '" + id + '\'' +
                        ",adult = '" + adult + '\'' +
                        ",vote_count = '" + voteCount + '\'' +
                        "}";
    }
}