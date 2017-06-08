package com.example.akis.popularmoviez.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.akis.popularmoviez.activities.MainActivity;
import com.example.akis.popularmoviez.adapters.MyItemRecyclerViewAdapter;
import com.example.akis.popularmoviez.classes.Movie;
import com.example.akis.popularmoviez.classes.MovieList;
import com.example.akis.popularmoviez.services.MovieService;
import com.example.akis.popularmoviez.R;
import com.example.akis.popularmoviez.services.ServiceGenerator;
import com.example.akis.popularmoviez.utilities.MovieDBHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GridFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private Integer nextPage = 1;
    private List<Movie> myList = new ArrayList<>();
    private MyItemRecyclerViewAdapter mAdapter = new MyItemRecyclerViewAdapter(myList, mListener);
    private String sort_selection = "popular";
    private Integer items_before_load = 8;
    private ProgressBar loader;
    private Boolean isLoading = false;

    public GridFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        View view = inflater.inflate(R.layout.fragment_grid_list, container, false);
        final RecyclerView recyclerView = (RecyclerView) view;
        Context context = view.getContext();
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, calculateNoOfColumns(context));
        loader = (ProgressBar) getActivity().findViewById(R.id.list_loader);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);

        populateList();

        // Infinite scroll listener
        addScrollListener(recyclerView, gridLayoutManager);

        ((MainActivity)getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh(String selection) {
                nextPage = 1;
                mAdapter.clearAll();
                sort_selection = selection;

                // Clear scroll listener
                recyclerView.clearOnScrollListeners();

                if (sort_selection.equals("favorite")) {
                    populateFavoriteList();
                } else {
                    addScrollListener(recyclerView, gridLayoutManager);
                    populateList();
                }
            }
        });

        return view;
    }

    // Scroll listener
    public void addScrollListener(RecyclerView recyclerView, final GridLayoutManager gridLayoutManager) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Boolean loadMore = gridLayoutManager.findLastCompletelyVisibleItemPosition() + items_before_load >= gridLayoutManager.getItemCount();
            // Reached bottom
            if (!isLoading && loadMore) {
                populateList();
            }
            }
        });
    }

    @Override
    public void onResume() {
        // Refresh favorites
        if (sort_selection.equals("favorite")) {
            mAdapter.clearAll();
            populateFavoriteList();
        }
        super.onResume();
    }

    public void populateFavoriteList() {
        Cursor mCursor = getContext().getContentResolver().query(MovieDBHelper.CONTENT_URI, null, null, null, null, null);

        List<Movie> movieList = new ArrayList<>();
        mCursor.moveToFirst();
        while(!mCursor.isAfterLast()) {
            Movie m = new Movie();
            m.setOriginal_title(mCursor.getString(mCursor.getColumnIndex(MovieDBHelper.COLUMN_MOVIE_TITLE)));
            m.setBackdrop_path(mCursor.getString(mCursor.getColumnIndex(MovieDBHelper.COLUMN_MOVIE_BACKDROP_PATH)));
            m.setId(mCursor.getInt(mCursor.getColumnIndex(MovieDBHelper.COLUMN_MOVIE_ID)));
            m.setPosterPath(mCursor.getString(mCursor.getColumnIndex(MovieDBHelper.COLUMN_MOVIE_POSTER_PATH)));
            m.setOverview(mCursor.getString(mCursor.getColumnIndex(MovieDBHelper.COLUMN_MOVIE_OVERVIEW)));
            m.setVote_average(mCursor.getString(mCursor.getColumnIndex(MovieDBHelper.COLUMN_MOVIE_VOTE_AVERAGE)));
            m.setRelease_date(mCursor.getString(mCursor.getColumnIndex(MovieDBHelper.COLUMN_MOVIE_RELEASE_DATE)));

            movieList.add(m);
            mCursor.moveToNext();
        }

        mAdapter.addAll(movieList, mListener);

        // No favorites toast display
        if (mAdapter.getItemCount() == 0) {
            Context context = getActivity();
            CharSequence text = "No favorites found";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    // Screen size will determine the number of columns
    // Calculate columns - http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    // Load list with most popular or top rated movies
    public void populateList() {
        // Show loader for page 1 only
        if (nextPage == 1) {
            loader.setVisibility(View.VISIBLE);
        }

        MovieService movieService = ServiceGenerator.createService(MovieService.class);
        Call<MovieList> call = movieService.getMovies(sort_selection, nextPage);

        isLoading = true;
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    loader.setVisibility(View.GONE);
                    MovieList movieList = response.body();
                    nextPage = movieList.getPage() + 1;
                    myList = movieList.getMovies();
                    mAdapter.addAll(myList, mListener);
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                loader.setVisibility(View.GONE);
                onNetworkError();
            }
        });
    }

    // Handle no connection error
    public void onNetworkError() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Context context = getActivity();
            CharSequence text = getResources().getString(R.string.no_connection);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d("DETACHED", "DETA");
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Movie item);
    }

}
