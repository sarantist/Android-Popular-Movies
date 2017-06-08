package com.example.akis.popularmoviez.adapters;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.akis.popularmoviez.R;
import com.example.akis.popularmoviez.classes.MovieTrailer;

import java.util.ArrayList;
import java.util.List;


public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.ViewHolder> {
    private List<MovieTrailer> mValues = new ArrayList<>();

    public TrailersRecyclerViewAdapter(List<MovieTrailer> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mVideoName.setText(holder.mItem.getName());
        holder.mVideoType.setText(holder.mItem.getSize() + "p");

        final float scale = holder.mView.getContext().getResources().getDisplayMetrics().density;
        int padding_5dp = (int) (10 * scale + 0.5f);
        holder.mView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.mView.setPadding(padding_5dp, padding_5dp,padding_5dp,padding_5dp);

        holder.mVideoName.setTextColor(ContextCompat.getColor(holder.mView.getContext(), R.color.colorAccent));
        holder.mVideoType.setTextColor(ContextCompat.getColor(holder.mView.getContext(), R.color.colorAccent));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(v, holder.mItem.getKey());
            }
        });
    }

    public void playVideo(View v, String key) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
            v.getContext().startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + key));
            v.getContext().startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public MovieTrailer mItem;
        TextView mVideoName;
        TextView mVideoType;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mVideoName = (TextView) view.findViewById(android.R.id.text1);
            mVideoType = (TextView) view.findViewById(android.R.id.text2);
        }
    }

    public void addAll(List list) {
        mValues.clear();
        mValues.addAll(list);
        notifyDataSetChanged();
    }
}