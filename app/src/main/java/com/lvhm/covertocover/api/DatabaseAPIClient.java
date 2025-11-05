package com.lvhm.covertocover.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lvhm.covertocover.PrintToast;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ReviewContainer;
import com.lvhm.covertocover.repo.UserTokenContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseAPIClient {
    private static PrintToast toast_printer;
    private static Retrofit retrofit;
    private static OkHttpClient http_client = null;
    private static Context app_context = null;
    private static final String BASE_URL = "https://covertocover-a55f.restdb.io/";
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
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getHTTPClient())
                    .build();
        }
        return retrofit;
    }
    public static DatabaseAPIService getDatabaseAPIService() {
        return getClient().create(DatabaseAPIService.class);
    }

    // Books
    public static boolean getBooksFromDB() {
        try {
            DatabaseAPIService book_service = getDatabaseAPIService();
            String query = String.format("{\"user_uuid\":\"%s\"}", UserTokenContainer.getInstance(app_context).getToken());
            Call<List<Book>> book_call = book_service.getBooks(query);
            Response<List<Book>> book_response = book_call.execute();
            if (book_response.isSuccessful() && book_response.body() != null) {
                List<Book> books = book_response.body();
                BookContainer.getInstance().setBooks((ArrayList<Book>) books);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean uploadBooksToDB(BookContainer book_container) {
        DatabaseAPIService book_service = getDatabaseAPIService();
        boolean all_uploaded = true;

        for(Book book : book_container.getBooks()) {
            try {
                String user_uuid_query = String.format("{\"user_uuid\":\"%s\"}", book.getUserUUID());
                String query = String.format("{" +
                        "\"user_uuid\":\"%s\"," +
                        "\"uuid\":\"%s\"" +
                        "}", book.getUserUUID(), book.getUUID());
                Response<Void> delete_response = book_service.deleteBooksByQuery(query).execute();
                Response<Book> post_response = book_service.addSingleBook(user_uuid_query, book).execute();
                if (!post_response.isSuccessful()) {
                    all_uploaded = false;
                }
            } catch (IOException e) {
                all_uploaded = false;
            }
        }
        return all_uploaded;
    }

    // Reviews
    public static boolean getReviewsFromDB() {
        try {
            DatabaseAPIService review_service = getDatabaseAPIService();
            String query = String.format("{\"user_uuid\":\"%s\"}", UserTokenContainer.getInstance(app_context).getToken());
            Call<List<Review>> review_call = review_service.getReviews(query);
            Response<List<Review>> review_response = review_call.execute();
            if (review_response.isSuccessful() && review_response.body() != null) {
                List<Review> reviews = review_response.body();
                ReviewContainer.getInstance().setReviews((ArrayList<Review>) reviews);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean uploadReviewsToDB(ReviewContainer review_container) {
        DatabaseAPIService review_service = getDatabaseAPIService();
        boolean all_uploaded = true;

        for(Review review : review_container.getReviews()) {
            try {
                String user_uuid_query = String.format("{\"user_uuid\":\"%s\"}", review.getUserUUID());
                String query = String.format("{" +
                        "\"user_uuid\":\"%s\"," +
                        "\"uuid\":\"%s\"" +
                        "}", review.getUserUUID(), review.getUUID());
                review_service.deleteReviewsByQuery(query).execute();
                Response<Review> post_response = review_service.addSingleReview(user_uuid_query, review).execute();
                if (!post_response.isSuccessful()) {
                    all_uploaded = false;
                }
            } catch (IOException e) {
                all_uploaded = false;
            }
        }
        return all_uploaded;
    }
}
