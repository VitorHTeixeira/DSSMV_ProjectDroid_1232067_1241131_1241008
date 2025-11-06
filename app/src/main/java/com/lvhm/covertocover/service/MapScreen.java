package com.lvhm.covertocover.service;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.lvhm.covertocover.R;
import com.lvhm.covertocover.api.GooglePlacesAPI;
import com.lvhm.covertocover.models.PlaceResponse;
import com.lvhm.covertocover.models.PlaceResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Implementa OnMapReadyCallback para receber o objeto GoogleMap
public class MapScreen extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GooglePlacesAPI placesApi;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private RecyclerView placesRecyclerView;
    private PlacesAdapter placesAdapter;
    private List<PlaceResult> placeResultsList = new ArrayList<>(); // Lista para armazenar os locais
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        placesRecyclerView = view.findViewById(R.id.locais);
        placesAdapter = new PlacesAdapter(placeResultsList, requireContext());
        placesRecyclerView.setAdapter(placesAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        placesApi = retrofit.create(GooglePlacesAPI.class);


        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapa);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            map.setMyLocationEnabled(true);
            getDeviceLocation();

        } else {
            // verifica a permissão de acesso a localização
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        checkLocationPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission(); // Se concedida, tenta novamente
            } else {
                Toast.makeText(requireContext(), "Permissão de localização negada.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

                    findNearbyPlaces(currentLatLng);
                } else {
                    Toast.makeText(requireContext(), "GPS indisponível. Verifique as configurações.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void findNearbyPlaces(LatLng location) {
        String formattedLocation = location.latitude + "," + location.longitude;
        int radius = 10000; // 2,5km o raio esta para ser em metros
        String type = "library"; // não pode ser "biblioteca"
        String apiKey = null;

        try {
            apiKey = requireActivity().getPackageManager().getApplicationInfo(
                    requireActivity().getPackageName(),
                    PackageManager.GET_META_DATA
            ).metaData.getString("com.google.android.geo.API_KEY");

            if (apiKey != null && apiKey.startsWith("$")) {
                apiKey = apiKey.substring(2, apiKey.length() - 1);
            }

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(requireContext(), "Erro: Chave da API não encontrada no Manifest.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        if (apiKey == null) return; // Estava com stress a ler por causa dos secrets... Entender

        Call<PlaceResponse> call = placesApi.getNearbyPlaces(
                formattedLocation,
                radius,
                type,
                apiKey // Usa a chave lida do Manifest
        );

        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceResponse> call, @NonNull Response<PlaceResponse> response) {

                if (response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                    if ("OK".equals(response.body().getStatus())) {

                        addMarkersToMap(response.body().getResults());
                        placeResultsList.clear(); // Limpa a lista antiga
                        List<PlaceResult> newPlaces = response.body().getResults();
                        placeResultsList.addAll(newPlaces); // Adiciona os novos resultadose
                        placesAdapter.notifyDataSetChanged(); // Notifica o Adapter para desenhar os itens

                    } else {
                        Toast.makeText(requireContext(), "Erro da API: " + response.body().getStatus(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Erro na resposta da API: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Erro de rede: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private void addMarkersToMap(List<PlaceResult> places) {
        if (map == null) return;
        map.clear(); //se houver marcadores no mapa faz a limpeza e deixa so o ponto azul do GPS

        if (places.isEmpty()) {
            Toast.makeText(requireContext(), "Nenhuma biblioteca encontrada.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (PlaceResult place : places) {
            double lat = place.getGeometry().getLocation().getLat();
            double lng = place.getGeometry().getLocation().getLng();
            LatLng latLng = new LatLng(lat, lng);

            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(place.getName())
                    .snippet(place.getVicinity()));
        }

        Toast.makeText(requireContext(), places.size() + " bibliotecas encontradas.", Toast.LENGTH_SHORT).show();
    }
}