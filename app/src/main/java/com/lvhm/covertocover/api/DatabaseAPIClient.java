package com.lvhm.covertocover.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.models.UserToken;
import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ReviewContainer;

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
    private static Retrofit retrofit = null;
    private static OkHttpClient httpClient = null;
    private static Context appContext = null;
    private static final String BASE_URL = "https://covertocover-a55f.restdb.io/";

    public static void initialize(Context context) {
        appContext = context.getApplicationContext();
    }

    private static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            String apiKey = DatabaseAPIService.loadAPIKey(appContext);
            if (apiKey == null || apiKey.trim().isEmpty()) {
                Log.e("API_CLIENT", "FATAL: API Key could not be loaded from secrets.properties.");
                return new OkHttpClient.Builder().build();
            }
            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("x-apikey", apiKey)
                                .header("cache-control", "no-cache")
                                .build();
                        return chain.proceed(request);
                    })
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();
        }
        return httpClient;
    }

    private static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getHttpClient())
                    .build();
        }
        return retrofit;
    }

    private static DatabaseAPIService getService() {
        return getClient().create(DatabaseAPIService.class);
    }

    // Books
    public static boolean getBooksFromDBByToken(String token) {
        try {
            DatabaseAPIService service = getService();
            String query = String.format("{\"userToken\":\"%s\"}", token);
            Call<List<Book>> call = service.getBooksByToken(query);
            Response<List<Book>> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                BookContainer.getInstance().setBooks((ArrayList<Book>) response.body());
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean uploadBooksToDB(BookContainer book_container) {
        DatabaseAPIService service = getService();
        boolean all_uploaded = true;
        for(Book book : book_container.getBooks()) {
            try {
                String query = String.format("{\"uuid\":\"%s\"}", book.getUUID());
                service.deleteBooksByQuery(query).execute();
                Response<Book> post_response = service.addSingleBook(book).execute();
                if (!post_response.isSuccessful()) all_uploaded = false;
            } catch (IOException e) { all_uploaded = false; }
        }
        return all_uploaded;
    }

    // Reviews
    public static boolean getReviewsFromDBByToken(String token) {
        try {
            DatabaseAPIService service = getService();
            String query = String.format("{\"userToken\":\"%s\"}", token);
            Call<List<Review>> call = service.getReviewsByToken(query);
            Response<List<Review>> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                ReviewContainer.getInstance().setReviews((ArrayList<Review>) response.body());
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean uploadReviewsToDB(ReviewContainer review_container) {
        DatabaseAPIService service = getService();
        boolean all_uploaded = true;
        for(Review review : review_container.getReviews()) {
            try {
                String query = String.format("{\"uuid\":\"%s\"}", review.getUUID());
                service.deleteReviewsByQuery(query).execute();
                Response<Review> post_response = service.addSingleReview(review).execute();
                if (!post_response.isSuccessful()) all_uploaded = false;
            } catch (IOException e) { all_uploaded = false; }
        }
        return all_uploaded;
    }

    // User Tokens
    public static boolean validateTokenOnServer(String token) {
        try {
            DatabaseAPIService service = getService();
            String query = String.format("{\"token\":\"%s\"}", token);
            Call<List<UserToken>> call = service.validateToken(query);
            Response<List<UserToken>> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                return !response.body().isEmpty();
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createUserOnServer(String token) {
        try {
            DatabaseAPIService service = getService();
            UserToken user_token = new UserToken(token);
            Call<UserToken> call = service.createUser(user_token);
            Response<UserToken> response = call.execute();

            if (!response.isSuccessful()) {
                String errorBody = "Empty error body";
                if(response.errorBody() != null) {
                    errorBody = response.errorBody().string();
                }
                Log.e("API_ERROR_CREATE_USER", "Failed with code: " + response.code() + " | Message: " + errorBody);
            }
            return response.isSuccessful();
        } catch (IOException e) {
            Log.e("API_ERROR_CREATE_USER", "IOException during user creation", e);
            return false;
        }
    }

    public static void updateLastSync(String token) {
        try {
            DatabaseAPIService service = getService();
            String query = String.format("{\"token\":\"%s\"}", token);
            Call<List<UserToken>> get_call = service.validateToken(query);
            Response<List<UserToken>> get_response = get_call.execute();

            if (get_response.isSuccessful() && get_response.body() != null && !get_response.body().isEmpty()) {
                UserToken user = get_response.body().get(0);
                user.setLastSync(System.currentTimeMillis());
                Call<UserToken> updateCall = service.updateUserSync(user.getId(), user);
                updateCall.execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean syncUserData(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        boolean tokenExists = validateTokenOnServer(token);
        if (!tokenExists) {
            createUserOnServer(token);
        }

        boolean booksLoaded = getBooksFromDBByToken(token);
        boolean reviewsLoaded = getReviewsFromDBByToken(token);

        if (booksLoaded || reviewsLoaded) {
            updateLastSync(token);
        }
        return booksLoaded && reviewsLoaded;
    }
}
