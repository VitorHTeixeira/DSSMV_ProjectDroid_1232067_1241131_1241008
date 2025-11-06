package com.lvhm.covertocover.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.models.PlaceResult;
import com.lvhm.covertocover.R;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private final List<PlaceResult> mPlacesList;
    private final Context mContext;

    // Construtor
    public PlacesAdapter(List<PlaceResult> placesList, Context context) {
        mPlacesList = placesList;
        mContext = context;
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView vicinityTextView;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.local_morada);
            vicinityTextView = itemView.findViewById(R.id.local_distancia);
        }
    }
    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.locs_maps, parent, false);
        return new PlaceViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        PlaceResult currentPlace = mPlacesList.get(position);

        holder.nameTextView.setText(currentPlace.getName());

        String vicinity = currentPlace.getVicinity();
        if (vicinity != null && !vicinity.isEmpty()) {
            holder.vicinityTextView.setText(vicinity);
        } else {
            holder.vicinityTextView.setText("Endereço indisponível");
        }
    }

    @Override
    public int getItemCount() {
        return mPlacesList.size();
    }
    public void updateList(List<PlaceResult> newPlaces) {
        mPlacesList.clear();
        mPlacesList.addAll(newPlaces);
        notifyDataSetChanged();
    }
}