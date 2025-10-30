package com.lvhm.covertocover.viewholder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;

public class RatingHistoryViewHolder extends RecyclerView.ViewHolder {
    public RatingBar history_rating_bar;
    public TextView history_date;
    public RatingHistoryViewHolder(View item_view) {
        super(item_view);
        history_rating_bar = item_view.findViewById(R.id.history_rating_bar);
        history_date = item_view.findViewById(R.id.history_date);
    }
}
