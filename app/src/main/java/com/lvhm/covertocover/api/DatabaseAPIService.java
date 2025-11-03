package com.lvhm.covertocover.api;

import android.content.Context;

import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ExportToJSON;
import com.lvhm.covertocover.repo.ReviewContainer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DatabaseAPIService {
    static String loadAPIKey(Context context) {
        Properties properties = new Properties();
        try (InputStream input = context.getAssets().open("secrets.properties")) {
            properties.load(input);
            String api_key = properties.getProperty("RESTDB_API_KEY");
            if (api_key == null || api_key.trim().isEmpty()) {
                throw new IllegalArgumentException("API key \"RESTDB_API_KEY\" not found in properties file");
            }
            input.close();
            return api_key;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GET("rest/books")
    Call<List<Book>> getBooks();
    @GET("rest/books")
    Call<List<Book>> getUniqueBook(@Query("q") String query);

    @PATCH("rest/books/{id}")
    @Headers("content-type: application/json")
    Call<Book> patchBooks(@Path("id") String id, @Body List<Book> books);

    @POST("rest/books")
    @Headers("content-type: application/json")
    Call<List<Book>> addBooks(@Body List<Book> books);

    @GET("rest/reviews")
    Call<List<Review>> getReviews();
    @GET("rest/reviews")
    Call<List<Review>> getUniqueReview(@Query("q") String query);

    @PATCH("rest/reviews/{id}")
    @Headers("content-type: application/json")
    Call<Review> patchReviews(@Path("id") String id, @Body List<Review> reviews);

    @POST("rest/reviews")
    @Headers("content-type: application/json")
    Call<List<Review>> addReviews(@Body List<Review> reviews);

    @DELETE("rest/books/*?q={}")
    Call<Void> deleteBooks();

    @DELETE("rest/reviews/*?q={}")
    Call<Void> deleteReviews();
}
