package com.example.akis.popularmoviez.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.akis.popularmoviez.adapters.ReviewsRecyclerViewAdapter;
import com.example.akis.popularmoviez.adapters.TrailersRecyclerViewAdapter;
import com.example.akis.popularmoviez.classes.Movie;
import com.example.akis.popularmoviez.R;
import com.example.akis.popularmoviez.classes.MovieReview;
import com.example.akis.popularmoviez.classes.MovieReviewList;
import com.example.akis.popularmoviez.classes.MovieTrailer;
import com.example.akis.popularmoviez.classes.MovieTrailerList;
import com.example.akis.popularmoviez.databinding.FragmentDetailedBinding;
import com.example.akis.popularmoviez.services.MovieService;
import com.example.akis.popularmoviez.services.ServiceGenerator;
import com.example.akis.popularmoviez.utilities.MovieDBHelper;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailedFragment extends Fragment {
    public Movie selected_movie;
    public boolean is_favorite = false;
    private List<MovieTrailer> trailerList = new ArrayList<>();
    private TrailersRecyclerViewAdapter mTrailerAdapter = new TrailersRecyclerViewAdapter(trailerList);
    private List<MovieReview> reviewList = new ArrayList<>();
    private ReviewsRecyclerViewAdapter mReviewAdapter = new ReviewsRecyclerViewAdapter(reviewList);

    public DetailedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selected_movie = getArguments().getParcelable("selected_item");
        FragmentDetailedBinding binding = FragmentDetailedBinding.inflate(inflater,container, false);

        String dateString = selected_movie.getReleaseDate();
        // Bind data to fragment
        binding.movieTitle.setText(selected_movie.getTitle());
        binding.movieOverview.setText(selected_movie.getOverview());
        binding.movieVote.setText(selected_movie.getVoteAverage() + " /10");
        binding.movieReleaseDate.setText(dateFormat(dateString));

        Picasso.with(getContext())
            .load("http://image.tmdb.org/t/p/w500" + selected_movie.getBackDropPath())
            .placeholder(R.color.colorPrimary)
            .error(R.color.colorPrimary)
            .into(binding.movieBackdrop);

        Picasso.with(getContext())
            .load("http://image.tmdb.org/t/p/w185" + selected_movie.getPosterPath())
            .placeholder(R.color.colorPrimary)
            .error(R.color.colorPrimary)
            .into(binding.moviePoster);

        loadMovieVideos();
        loadMovieReviews();

        // Bind adapters
        binding.movieTrailers.setAdapter(mTrailerAdapter);
        binding.movieReviews.setAdapter(mReviewAdapter);

        return binding.getRoot();
    }

    public void loadMovieVideos() {
        MovieService movieService = ServiceGenerator.createService(MovieService.class);
        Call<MovieTrailerList> call = movieService.getMovieTrailers(selected_movie.getId());

        call.enqueue(new Callback<MovieTrailerList>() {
            // Success
            @Override
            public void onResponse(Call<MovieTrailerList> call, Response<MovieTrailerList> response) {
                if (response.isSuccessful()) {
                    MovieTrailerList trailer_list = response.body();
                    trailerList = trailer_list.getVideos();
                    mTrailerAdapter.addAll(trailerList);
                }
            }
            // Error
            @Override
            public void onFailure(Call<MovieTrailerList> call, Throwable t) {
                // Handle error
            }
        });
    }

    public void loadMovieReviews() {
        MovieService movieService = ServiceGenerator.createService(MovieService.class);
        Call<MovieReviewList> call = movieService.getMovieReviews(selected_movie.getId());

        call.enqueue(new Callback<MovieReviewList>() {
            @Override
            public void onResponse(Call<MovieReviewList> call, Response<MovieReviewList> response) {
                if (response.isSuccessful()) {
                    MovieReviewList review_list = response.body();
                    reviewList = review_list.getReviews();
                    mReviewAdapter.addAll(reviewList);
                }
            }

            @Override
            public void onFailure(Call<MovieReviewList> call, Throwable t) {

            }
        });
    }

    // Date format
    public String dateFormat(String dateString) {
        try {
            Date movie_date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            return new SimpleDateFormat("dd MMM yyyy").format(movie_date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                // Favorite icon handle
                if (is_favorite) {
                    item.setIcon(R.drawable.ic_favorite);
                    is_favorite = false;
                    // Favorite delete
                    deleteFavorite();
                } else {
                    item.setIcon(R.drawable.ic_favorite_full);
                    is_favorite = true;
                    // Add to favorites
                    addFavorite();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu (Menu menu) {

        // Initialize favorite icon
        is_favorite = isFavorite(selected_movie.getId());
        if (is_favorite) {
            menu.getItem(0).setIcon(R.drawable.ic_favorite_full);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_favorite);
        }
    }

    private boolean isFavorite(Integer movie_id) {
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieDBHelper.CONTENT_URI,
                new String[]{MovieDBHelper.COLUMN_MOVIE_ID},
                MovieDBHelper.COLUMN_MOVIE_ID + " = " + movie_id,
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }

    private void deleteFavorite() {
        getContext().getContentResolver().delete(MovieDBHelper.CONTENT_URI,
                MovieDBHelper.COLUMN_MOVIE_ID + " = " + selected_movie.getId(), null);
    }

    private void addFavorite() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieDBHelper.COLUMN_MOVIE_ID,
                selected_movie.getId());
        movieValues.put(MovieDBHelper.COLUMN_MOVIE_TITLE,
                selected_movie.getTitle());
        movieValues.put(MovieDBHelper.COLUMN_MOVIE_POSTER_PATH,
                selected_movie.getPosterPath());
        movieValues.put(MovieDBHelper.COLUMN_MOVIE_OVERVIEW,
                selected_movie.getOverview());
        movieValues.put(MovieDBHelper.COLUMN_MOVIE_VOTE_AVERAGE,
                selected_movie.getVoteAverage());
        movieValues.put(MovieDBHelper.COLUMN_MOVIE_RELEASE_DATE,
                selected_movie.getReleaseDate());
        movieValues.put(MovieDBHelper.COLUMN_MOVIE_BACKDROP_PATH,
                selected_movie.getBackDropPath());
        getContext().getContentResolver().insert(
                MovieDBHelper.CONTENT_URI,
                movieValues
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
