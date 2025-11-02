package com.lvhm.covertocover.api;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lvhm.covertocover.NotificationCentral;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ReviewContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseAPIClient {
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
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHTTPClient())
                    .build();
        }
        return retrofit;
    }
    public static DatabaseAPIService getDatabaseAPIService() {
        return getClient().create(DatabaseAPIService.class);
    }
    public static BookContainer getBooksFromDB(Context context) {
        final BookContainer[] book_container = {BookContainer.getInstance()};
        DatabaseAPIService database_service = getDatabaseAPIService();
        Call<BookContainer> call_books = database_service.getBooks();
        call_books.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BookContainer> call, @NonNull Response<BookContainer> response) {
                if(response.isSuccessful()) {
                    book_container[0] = new BookContainer(response.body().getBooks());
                }
                else {
                    onFailure(call, new Throwable());
                }
            }
            @Override
            public void onFailure(@NonNull Call<BookContainer> call, @NonNull Throwable t) {
                NotificationCentral.showNotification(context, "Could not retrieve books from database");
            }
        });
        return book_container[0];
    }
    public static void addBookToDB(Context context, BookContainer book_container) {
        DatabaseAPIService database_service = getDatabaseAPIService();
        Call<BookContainer> call_book = database_service.addBooks(book_container);
        call_book.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BookContainer> call, @NonNull Response<BookContainer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookContainer book_list = response.body();
                    Toast.makeText(context, "Added " + book_list.getBooks().size() + " books to the database", Toast.LENGTH_SHORT).show();
                } else {
                    onFailure(call, new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookContainer> call, @NonNull Throwable t) {
                NotificationCentral.showNotification(context, "Could not save books to the database");
            }
        });
    }
    public static boolean addBookToDB(BookContainer book_container) {
        try {
            DatabaseAPIService book_service = getDatabaseAPIService();
            Call<BookContainer> book_call = book_service.addBooks(book_container);
            Response<BookContainer> book_response = book_call.execute();
            return book_response.isSuccessful() && book_response.body() != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void getReviewsFromDB(Context context, ReviewContainer review_container) {
        DatabaseAPIService database_service = getDatabaseAPIService();
        Call<ReviewContainer> call_reviews = database_service.getReviews();
        call_reviews.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ReviewContainer> call, @NonNull Response<ReviewContainer> response) {
                if(response.isSuccessful()) {
                    ReviewContainer reviews = response.body();
//                    review_container.setReviews(reviews);
                } else {
                    onFailure(call, new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewContainer> call, @NonNull Throwable t) {
                NotificationCentral.showNotification(context, "Could not retrieve reviews from database");
            }
        });
    }

    public static boolean addReviewToDB(ReviewContainer review_container) {
        try {
            DatabaseAPIService review_service = getDatabaseAPIService();
            Call<ReviewContainer> review_call = review_service.addReviews(review_container);
            Response<ReviewContainer> review_response = review_call.execute();
            return review_response.isSuccessful() && review_response.body() != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

     public static void addReviewToDB(Context context, ReviewContainer review_container) {
         DatabaseAPIService database_service = getDatabaseAPIService();
         Call<ReviewContainer> call_book = database_service.addReviews(review_container);
         call_book.enqueue(new Callback<>() {
             @Override
             public void onResponse(@NonNull Call<ReviewContainer> call, @NonNull Response<ReviewContainer> response) {
                 if (response.isSuccessful() && response.body() != null) {
                     ReviewContainer review_list = response.body();
                     Toast.makeText(context, "Added " + review_list.getReviews().size() + " reviews to the database", Toast.LENGTH_SHORT).show();
                 } else {
                     onFailure(call, new Throwable());
                 }
             }

             @Override
             public void onFailure(@NonNull Call<ReviewContainer> call, @NonNull Throwable t) {
                 NotificationCentral.showNotification(context, "Could not save reviews to the database");
             }
         });
     }
}
