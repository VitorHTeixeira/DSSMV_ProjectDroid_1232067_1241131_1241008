package com.lvhm.covertocover.adapter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.viewholder.ReadingActivityViewHolder;

import java.util.ArrayList;

public class ReadingActivityAdapter extends RecyclerView.Adapter<ReadingActivityViewHolder> {
    private ArrayList<Book> data;

    public ReadingActivityAdapter(ArrayList<Book> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ReadingActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bookcover, parent, false);
        return new ReadingActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingActivityViewHolder holder, int position) {
        Book item = data.get(position);

        if (item.getCoverImage() == null) {
            Bitmap default_book_cover = Bitmap.createBitmap(
                    100,
                    152,
                    Bitmap.Config.ARGB_8888);

            int color = ContextCompat.getColor(holder.book_cover.getContext(), R.color.black);
            Canvas canvas = new Canvas(default_book_cover);
            canvas.drawColor(color);
            holder.book_cover.setImageBitmap(default_book_cover);
        } else {
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
