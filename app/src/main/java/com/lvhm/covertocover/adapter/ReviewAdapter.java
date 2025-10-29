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

    public ReviewAdapter(ArrayList<Review> data) {
        this.data = data;
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
