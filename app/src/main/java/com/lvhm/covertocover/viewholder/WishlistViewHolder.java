package com.lvhm.covertocover.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;

public class WishlistViewHolder extends RecyclerView.ViewHolder {
    public ImageView book_cover;
    public RelativeLayout bookcover_placeholder;
    public WishlistViewHolder(View item_view) {
        super(item_view);
        book_cover = item_view.findViewById(R.id.bookcover_image);
        bookcover_placeholder = item_view.findViewById(R.id.bookcover_placeholder);
    }
}
