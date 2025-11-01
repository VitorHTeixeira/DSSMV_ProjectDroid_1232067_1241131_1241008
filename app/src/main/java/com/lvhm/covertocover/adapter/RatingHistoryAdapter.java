package com.lvhm.covertocover.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.viewholder.RatingHistoryViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RatingHistoryAdapter extends RecyclerView.Adapter<RatingHistoryViewHolder> {
    private Context context;
    private ArrayList<Review> data;
    private OnReviewClickListener listener;
    String review_date = "";

    public RatingHistoryAdapter(Context context, ArrayList<Review> data, OnReviewClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }
    @Override
    public RatingHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating_history, parent, false);
        return new RatingHistoryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RatingHistoryViewHolder holder, int position) {
        Review item = data.get(position);
        SharedPreferences shared_preferences = context.getSharedPreferences("CTCPreferences", Context.MODE_PRIVATE);
        int date_format = shared_preferences.getInt("profile_date_format", 0);
        switch (date_format) {
            case 0:
                review_date = new SimpleDateFormat("dd/MM/yyyy").format(item.getDate());
                break;
            case 1:
                review_date = new SimpleDateFormat("MM/dd/yyyy").format(item.getDate());
                break;
            case 2:
                review_date = new SimpleDateFormat("yyyy/MM/dd").format(item.getDate());
                break;
            default:
                break;
        }
        holder.history_rating_bar.setRating((float) item.getRating());
        holder.history_date.setText(review_date);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int adapter_position = holder.getAdapterPosition();
                if (adapter_position != RecyclerView.NO_POSITION) {
                    listener.onReviewClick(data.get(adapter_position));
                }
            }
        });
    }

    public void updateData(ArrayList<Review> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
