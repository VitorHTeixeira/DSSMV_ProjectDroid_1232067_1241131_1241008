package com.lvhm.covertocover;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.lvhm.covertocover.datamodels.BookResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public static void getBookFromAPI(Context context, String barcode_value, BookAPICallback callback) {
        String query = "isbn:" + barcode_value.trim();
        BookAPIService bookAPIService = getBookAPIService();
        Call<BookResponse> call = bookAPIService.getBookByISBN(query);

        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(@NonNull Call<BookResponse> call, @NonNull Response<BookResponse> response) {
                final Bundle book_details_bundle = new Bundle();
                if (response.isSuccessful() && response.body() != null && response.body().getItems() != null && !response.body().getItems().isEmpty()) {
                    Gson gson = new Gson();
                    book_details_bundle.putString("book_details", gson.toJson(response.body()));
                    BookResponse.Item item = response.body().getItems().get(0);
                    if (item != null && item.getVolumeInfo() != null) {
                        NotificationCentral.showNotification(context, "Book found: " + barcode_value);
                        callback.onBookFound(item, barcode_value, book_details_bundle);
                    } else {
                        NotificationCentral.showNotification(context, "Book found, but volume info is missing.");
                        callback.onNoBookFound(barcode_value);
                    }
                } else {
                    NotificationCentral.showNotification(context, "API call successful, but no book found.");
                    callback.onNoBookFound(barcode_value);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookResponse> call, @NonNull Throwable t) {
                NotificationCentral.showNotification(context, "API call failed: " + t.getMessage());
                callback.onAPIFailure(t.getMessage());
            }
        });
    }
}
