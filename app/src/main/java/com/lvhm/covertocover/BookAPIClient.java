package com.lvhm.covertocover;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookAPIClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/";
    private static final OkHttpClient http_client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build();

    public static Retrofit getClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(http_client)
                    .build();
        }
        return retrofit;
    }

    public static BookAPIService getBookAPIService() {
        return getClient().create(BookAPIService.class);
    }
    
}
