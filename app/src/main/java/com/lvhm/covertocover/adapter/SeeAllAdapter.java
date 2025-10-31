package com.lvhm.covertocover.adapter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.service.BookScreen;
import com.lvhm.covertocover.viewholder.SeeAllViewHolder;

import java.util.ArrayList;

public class SeeAllAdapter extends RecyclerView.Adapter<SeeAllViewHolder> {
    private ImageView book_cover;
    private ArrayList<Book> data;
    private OnBookClickListener listener;
    public SeeAllAdapter(ArrayList<Book> data, OnBookClickListener listener) {
        this.data = data;
        this.listener = listener;
    }
    @NonNull
    @Override
    public SeeAllViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bookcover, parent, false);
        return new SeeAllViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SeeAllViewHolder holder, int position) {
        Book item = data.get(position);
        if(item.getCoverImage() == null) {
            Bitmap default_book_cover = Bitmap.createBitmap(
                    100,
                    152,
                    Bitmap.Config.ARGB_8888);

            int color = ContextCompat.getColor(holder.book_cover.getContext(), R.color.black);
            Canvas canvas = new Canvas(default_book_cover);
            canvas.drawColor(color);
            holder.book_cover.setImageBitmap(default_book_cover);
        }
        else {
            Bitmap book_cover_image = item.getCoverImage();
            holder.book_cover.setImageBitmap(book_cover_image);
        }
        holder.book_cover.setOnClickListener(v -> {
            System.out.println("Click registado na posição: " + position);
            if (listener != null) {
                System.out.println("A chamar listener.onBookClick");
                listener.onBookClick(item);
            } else {
                System.out.println("ERRO: listener é null!");
            }
        });
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
