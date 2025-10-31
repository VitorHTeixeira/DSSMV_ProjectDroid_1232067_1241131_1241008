package com.lvhm.covertocover.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;

public class SeeAllViewHolder extends RecyclerView.ViewHolder {
    public ImageView book_cover;
    public SeeAllViewHolder(View item_view) {
        super(item_view);
        book_cover = item_view.findViewById(R.id.bookcover_image);
    }
}
