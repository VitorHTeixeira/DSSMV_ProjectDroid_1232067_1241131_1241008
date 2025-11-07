package com.lvhm.covertocover.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GooglePlacesAPIClient {
    private static Retrofit retrofit;
    private static OkHttpClient http_client = null;
    private static Context app_context = null;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    public static void initialize(Context context) {
        app_context = context.getApplicationContext();
    }
    private static OkHttpClient getHTTPClient() {
        if(http_client == null) {
            String api_key = DatabaseAPIService.loadAPIKey(app_context);
            http_client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("x-apikey", api_key)
                                .header("cache-control", "no-cache")
                                .build();
                        return chain.proceed(request);
                    })
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();
        }
        return http_client;
    }
    public static Retrofit getClient() {
        if(retrofit == null) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(http_client)
                    .build();
        }
        return retrofit;
    }
    public static GooglePlacesAPIService getGooglePlacesAPIService() {
        return getClient().create(GooglePlacesAPIService.class);
    }
}
