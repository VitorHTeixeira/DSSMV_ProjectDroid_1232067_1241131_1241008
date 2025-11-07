package com.lvhm.covertocover.api;

import android.content.Context;

import com.lvhm.covertocover.datamodels.PlaceResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesAPIService {
    static String loadAPIKey(Context context) {
        Properties properties = new Properties();
        try (InputStream input = context.getAssets().open("secrets.properties")) {
            properties.load(input);
            String api_key = properties.getProperty("MAPS_API_KEY");
            if (api_key == null || api_key.trim().isEmpty()) {
                throw new IllegalArgumentException("API key \"MAPS_API_KEY\" not found in properties file");
            }
            input.close();
            return api_key;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @GET("place/nearbysearch/json")
    Call<PlaceResponse> getNearbyPlaces(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("keyword") String keyword,
            @Query("key") String api_key
    );
}