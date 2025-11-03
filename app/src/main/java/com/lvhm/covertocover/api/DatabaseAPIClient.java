package com.lvhm.covertocover.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lvhm.covertocover.NotificationCentral;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ReviewContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            Call<List<Book>> book_call = book_service.getBooks();
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
        try {
            DatabaseAPIService book_service = getDatabaseAPIService();
            Log.d("UploadBooks", "Número de livros a enviar: " + book_container.getBooks().size());
            Response<List<Book>> get_response = book_service
                    .getUniqueBook("{\"state_key\":\"all_books\"}")
                    .execute();
            Log.d("UploadBooks", "GET successful: " + get_response.isSuccessful());
            Log.d("UploadBooks", "GET body is null: " + (get_response.body() == null));
            Log.d("UploadBooks", "GET body isEmpty: " + (get_response.body() != null && get_response.body().isEmpty()));
            if (get_response.isSuccessful() && get_response.body() != null && !get_response.body().isEmpty()) {
                String unique_id = get_response.body().get(0).get_id();
                Log.d("UploadBooks", "PATCH ID: " + unique_id);
                Response<Book> patch_response = book_service
                        .patchBooks(unique_id, book_container.getBooks())
                        .execute();
                Log.d("UploadBooks", "PATCH successful: " + patch_response.isSuccessful());
                Log.d("UploadBooks", "PATCH code: " + patch_response.code());
                Log.d("UploadBooks", "PATCH body: " + patch_response.body());
                if (!patch_response.isSuccessful()) {
                    Log.e("UploadBooks", "PATCH error: " + patch_response.errorBody().string());
                }
                return patch_response.isSuccessful();
            } else {
                Log.d("UploadBooks", "Criando novo registo POST");
                Call<List<Book>> post_call = book_service.addBooks(book_container.getBooks());
                Response<List<Book>> post_response = post_call.execute();
                Log.d("UploadBooks", "POST successful: " + post_response.isSuccessful());
                Log.d("UploadBooks", "POST code: " + post_response.code());
                if (post_response.body() != null) {
                    Log.d("UploadBooks", "POST retornou " + post_response.body().size() + " livros");
                    for (Book b : post_response.body()) {
                        Log.d("UploadBooks", "Livro criado - ID: " + b.get_id() + ", Nome: " + b.getName());
                    }
                }
                return post_response.isSuccessful();
            }
        } catch (IOException e) {
            Log.e("UploadBooks", "Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static boolean patchSingleBook(Book book) {
        if (book.get_id() == null || book.get_id().trim().isEmpty()) {
            Log.e("PatchSingleBook", "No _id. PATCH is not possible.");
            return false;
        }
        try {
            DatabaseAPIService book_service = getDatabaseAPIService();
            String book_id = book.get_id();
            Call<Book> patch_call = book_service.patchSingleBook(book_id, book);
            Response<Book> patch_response = patch_call.execute();
            Log.d("PatchSingleBook", "PATCH individual successful: " + patch_response.isSuccessful());
            Log.d("PatchSingleBook", "PATCH code: " + patch_response.code());
            if (!patch_response.isSuccessful()) {
                String error_body = patch_response.errorBody() != null ? patch_response.errorBody().string() : "N/A";
                Log.e("PatchSingleBook", "PATCH individual error: " + error_body);
            }
            return patch_response.isSuccessful();
        } catch (IOException e) {
            Log.e("PatchSingleBook", "Exception during PATCH: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Reviews
    public static boolean getReviewsFromDB() {
        try {
            DatabaseAPIService review_service = getDatabaseAPIService();
            Call<List<Review>> review_call = review_service.getReviews();
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
        try {
            DatabaseAPIService review_service = getDatabaseAPIService();
            Response<List<Review>> get_response = review_service
                    .getUniqueReview("{\"state_key\":\"all_reviews\"}")
                    .execute();
            if (get_response.isSuccessful() && get_response.body() != null && !get_response.body().isEmpty()) {
                String unique_id = get_response.body().get(0).get_id();
                Response<Review> patch_response = review_service
                        .patchReviews(unique_id, review_container.getReviews())
                        .execute();
                return patch_response.isSuccessful();
            } else {
                Call<List<Review>> post_call = review_service.addReviews(review_container.getReviews());
                Response<List<Review>> post_response = post_call.execute();
                return post_response.isSuccessful();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
