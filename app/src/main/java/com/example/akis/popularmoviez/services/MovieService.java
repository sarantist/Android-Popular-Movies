package com.example.akis.popularmoviez.services;

import com.example.akis.popularmoviez.classes.MovieList;
import com.example.akis.popularmoviez.classes.MovieReviewList;
import com.example.akis.popularmoviez.classes.MovieTrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    String apiKey = "your_api_key";

    @GET("movie/{sortBy}?api_key=" + apiKey)
    Call<MovieList> getMovies(@Path("sortBy")String sortBy,
                              @Query("page") int pageIndex);

    @GET("movie/{movie_id}/videos?api_key=" + apiKey)
    Call<MovieTrailerList> getMovieTrailers(@Path("movie_id") Integer movie_id);

    @GET("movie/{movie_id}/reviews?api_key=" + apiKey)
    Call<MovieReviewList> getMovieReviews(@Path("movie_id") Integer movie_id);
}