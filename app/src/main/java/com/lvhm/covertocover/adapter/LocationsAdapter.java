package com.lvhm.covertocover.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.lvhm.covertocover.R;
import com.lvhm.covertocover.datamodels.PlaceResult;
import com.lvhm.covertocover.viewholder.LocationsViewHolder;

import java.util.ArrayList;
import java.util.Locale;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsViewHolder> {
    private ArrayList<PlaceResult> data;
    private LatLng user_location;

    public LocationsAdapter(ArrayList<PlaceResult> data, LatLng user_location) {
        this.data = data;
        this.user_location = user_location;
    }
    @NonNull
    @Override
    public LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.locs_maps, parent, false);
        return new LocationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsViewHolder holder, int position) {
        PlaceResult place = data.get(position);
        holder.location_address.setText(place.getName());
        if (ContextCompat.checkSelfPermission(holder.itemView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        double distance = getDistance(
                user_location.latitude,
                user_location.longitude,
                place.getGeometry().getLocation().getLat(),
                place.getGeometry().getLocation().getLng()
        );
        String distance_text;
        if (distance < 1) {
            distance_text = String.format(Locale.getDefault(), "%.0f m", distance * 1000);
        } else {
            distance_text = String.format(Locale.getDefault(), "%.1f km", distance);
        }
        holder.location_distance.setText(distance_text);
        holder.itemView.setOnClickListener(l -> {
            double lat = place.getGeometry().getLocation().getLat();
            double lng = place.getGeometry().getLocation().getLng();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("geo:%f,%f?q=%f,%f(%s)",
                                                                        lat, lng, lat, lng, Uri.encode(place.getName()))));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        final int earth_radius = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earth_radius * c;
    }
}
