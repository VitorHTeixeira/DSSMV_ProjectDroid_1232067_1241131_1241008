package com.lvhm.covertocover.api; // ou a sua pasta de interfaces

import com.lvhm.covertocover.models.PlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesAPI {


    @GET("place/nearbysearch/json")
    Call<PlaceResponse> getNearbyPlaces(
            @Query("location") String location, // Lat,Lng (ex: 38.72,-9.13)
            @Query("radius") int radius,       // Raio em metros
            @Query("type") String type,        // Tipo (ex: library)
            @Query("key") String apiKey         // A chave da API
    );
}