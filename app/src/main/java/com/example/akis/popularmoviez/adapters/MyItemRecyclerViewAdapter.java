package com.example.akis.popularmoviez.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.akis.popularmoviez.classes.Movie;
import com.example.akis.popularmoviez.fragments.GridFragment.OnListFragmentInteractionListener;
import com.example.akis.popularmoviez.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<Movie> mValues = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<Movie> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Log.d("POSTER", mValues.get(position).getPosterPath());
        Picasso.with(holder.mView.getContext()).load("http://image.tmdb.org/t/p/w185" + mValues.get(position).getPosterPath()).into(holder.movieImage);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView movieImage;
        public Movie mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            movieImage = (ImageView) view.findViewById(R.id.movie_image);
        }

    }

    public void addAll(List list, OnListFragmentInteractionListener listener){
        mValues.addAll(list);
        mListener = listener;
        notifyDataSetChanged();
    }

    public void clearAll() {
        mValues.clear();
        mListener = null;
        notifyDataSetChanged();
    }
}
