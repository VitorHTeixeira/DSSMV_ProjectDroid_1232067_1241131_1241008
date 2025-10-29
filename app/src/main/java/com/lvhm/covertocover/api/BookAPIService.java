package com.lvhm.covertocover.api;

import com.lvhm.covertocover.datamodels.BookResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookAPIService {
    @GET("volumes")
    Call<BookResponse> getBookByISBN(@Query("q") String query);
    // getBookByISBN("isbn:9781501110368")
}
