package com.example.akis.popularmoviez.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.akis.popularmoviez.R;
import com.example.akis.popularmoviez.classes.MovieReview;
import java.util.ArrayList;
import java.util.List;


public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {
    private List<MovieReview> mValues = new ArrayList<>();

    public ReviewsRecyclerViewAdapter(List<MovieReview> items) {
        mValues = items;
    }

    @Override
    public ReviewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.two_line_list_item, parent, false);
        return new ReviewsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        // Set text
        holder.mAuthor.setText(holder.mItem.getAuthor());
        holder.mContent.setText(holder.mItem.getContent());

        // Truncate text
        holder.mContent.setMaxLines(3);
        holder.mContent.setEllipsize(TextUtils.TruncateAt.END);

        // Padding
        final float scale = holder.mView.getContext().getResources().getDisplayMetrics().density;
        int padding_5dp = (int) (10 * scale + 0.5f);
        holder.mView.setPadding(padding_5dp, padding_5dp,padding_5dp,padding_5dp);

        // Text color
        holder.mAuthor.setTextColor(ContextCompat.getColor(holder.mView.getContext(), R.color.colorAccent));
        holder.mContent.setTextColor(ContextCompat.getColor(holder.mView.getContext(), R.color.colorAccent));

        // Full text dialog display
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readReview(v, holder);
            }
        });
    }

    public void readReview(View v, ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle(holder.mItem.getAuthor());
        builder.setMessage(holder.mItem.getContent());
        builder.show();
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public MovieReview mItem;
        TextView mAuthor;
        TextView mContent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAuthor = (TextView) view.findViewById(android.R.id.text1);
            mContent = (TextView) view.findViewById(android.R.id.text2);
        }
    }

    public void addAll(List list) {
        mValues.clear();
        mValues.addAll(list);
        notifyDataSetChanged();
    }
}
