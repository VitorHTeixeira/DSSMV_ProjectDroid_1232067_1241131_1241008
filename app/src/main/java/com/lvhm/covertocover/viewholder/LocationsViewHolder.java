package com.lvhm.covertocover.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;

public class LocationsViewHolder extends RecyclerView.ViewHolder {
    public TextView location_address;
    public TextView location_distance;
    public LocationsViewHolder(@NonNull View item_view) {
        super(item_view);
        location_address = item_view.findViewById(R.id.location_address);
        location_distance = item_view.findViewById(R.id.location_distance);
    }
}
