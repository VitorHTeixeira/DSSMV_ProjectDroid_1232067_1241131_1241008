package com.lvhm.covertocover.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.viewholder.ReviewViewHolder;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private ArrayList<Review> data;
    private OnBookClickListener listener;

    public ReviewAdapter(ArrayList<Review> data, OnBookClickListener listener) {
        this.data = data;
        this.listener = listener;
    }
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bookcover, parent, false);
        return new ReviewViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review item = data.get(position);
        if(item.getBook() == null) {
            holder.book_cover.setImageBitmap(null);
        }
        else {
            Bitmap book_cover_image = item.getBook().getCoverImage();
            holder.book_cover.setImageBitmap(book_cover_image);
        }
        holder.book_cover.setOnClickListener(v -> {
            if (listener != null) {
                int adapter_position = holder.getAdapterPosition();
                if (adapter_position != RecyclerView.NO_POSITION) {
                    listener.onBookClick(data.get(adapter_position).getBook());
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
