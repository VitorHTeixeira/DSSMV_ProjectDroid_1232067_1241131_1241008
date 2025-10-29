package com.lvhm.covertocover.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.viewholder.WishlistViewHolder;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistViewHolder> {
    private ArrayList<Book> data;

    public WishlistAdapter(ArrayList<Book> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bookcover, parent, false);
        return new WishlistViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Book item = data.get(position);
        if(item.getCoverImage() == null) {
            int color = ContextCompat.getColor(holder.book_cover.getContext(), R.color.black);
            holder.book_cover.setImageDrawable(new ColorDrawable(color));
        }
        else {
            Bitmap book_cover_image = item.getCoverImage();
            holder.book_cover.setImageBitmap(book_cover_image);
        }
    }
    public void updateData(ArrayList<Book> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
