package com.lvhm.covertocover.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    public ImageView book_cover;
    public ReviewViewHolder(View item_view) {
        super(item_view);
        book_cover = item_view.findViewById(R.id.bookcover_image);
    }
}
