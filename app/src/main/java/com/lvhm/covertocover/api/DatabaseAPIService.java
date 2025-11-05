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
import retrofit2.http.PUT;
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
    Call<List<Book>> getBooks(@Query("q") String query);
    @POST("rest/books")
    @Headers("content-type: application/json")
    Call<Book> addSingleBook(@Query("q") String query, @Body Book book);
    @DELETE("rest/books/*")
    Call<Void> deleteBooksByQuery(@Query("q") String query);

    @GET("rest/reviews")
    Call<List<Review>> getReviews(@Query("q") String query);
    @POST("rest/reviews")
    @Headers("content-type: application/json")
    Call<Review> addSingleReview(@Query("q") String query, @Body Review review);
    @DELETE("rest/reviews/*")
    Call<Void> deleteReviewsByQuery(@Query("q") String query);
}
