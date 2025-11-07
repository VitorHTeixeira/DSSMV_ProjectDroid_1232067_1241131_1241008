package com.lvhm.covertocover.service;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lvhm.covertocover.R;
import com.lvhm.covertocover.adapter.LocationsAdapter;
import com.lvhm.covertocover.api.GooglePlacesAPIService;
import com.lvhm.covertocover.datamodels.PlaceResponse;
import com.lvhm.covertocover.datamodels.PlaceResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapScreen extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private FusedLocationProviderClient fused_location_provider_client;
    private GooglePlacesAPIService places_api;
    private LocationsAdapter location_adapter;
    private static RecyclerView locations_recycler;
    private static LatLng current_lat_lng;
    private final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        fused_location_provider_client = LocationServices.getFusedLocationProviderClient(requireActivity());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        places_api = retrofit.create(GooglePlacesAPIService.class);

        SupportMapFragment map_fragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapa);
        if (map_fragment != null) {
            map_fragment.getMapAsync(this);
        }

        locations_recycler = view.findViewById(R.id.recycler_locations);

        return view;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        getDeviceLocation();
    }
    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fused_location_provider_client.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (location != null) {
                current_lat_lng = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(current_lat_lng, 15));
                findNearbyPlaces(current_lat_lng);
            }
        });
    }
    private void findNearbyPlaces(LatLng location) {
        String formatted_location = location.latitude + "," + location.longitude;
        int radius = 5000;
        String type = "library";
        String keyword = "public library, university library";
        String api_key = GooglePlacesAPIService.loadAPIKey(requireContext());

        Call<PlaceResponse> call = places_api.getNearbyPlaces(
                formatted_location,
                radius,
                type,
                keyword,
                api_key
        );
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PlaceResponse> call, @NonNull Response<PlaceResponse> response) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                    if ("OK".equals(response.body().getStatus())) {
                        addMarkersToMap(response.body().getResults());
                        ArrayList<PlaceResult> location_data = new ArrayList<>(response.body().getResults());
                        location_adapter = new LocationsAdapter(location_data, current_lat_lng);
                        locations_recycler.setAdapter(location_adapter);
                    }
                } else {
                    Toast.makeText(requireContext(), "Error on API response: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<PlaceResponse> call, @NonNull Throwable t) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void addMarkersToMap(List<PlaceResult> places) {
        if (!isAdded() || getContext() == null || map == null) {
            return;
        }
        map.clear();
        if (places.isEmpty()) {
            Toast.makeText(requireContext(), "⚠ No libraries found", Toast.LENGTH_SHORT).show();
            return;
        }
        for (PlaceResult place : places) {
            double lat = place.getGeometry().getLocation().getLat();
            double lng = place.getGeometry().getLocation().getLng();
            LatLng lat_lng = new LatLng(lat, lng);
            map.addMarker(new MarkerOptions()
                    .position(lat_lng)
                    .title(place.getName())
                    .snippet(place.getVicinity()));
        }
        Toast.makeText(requireContext(), "✅ " + places.size() + " libraries found", Toast.LENGTH_SHORT).show();
    }
}