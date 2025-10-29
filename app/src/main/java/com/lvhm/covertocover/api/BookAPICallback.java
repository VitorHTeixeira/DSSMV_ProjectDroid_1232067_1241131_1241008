package com.lvhm.covertocover.api;

import android.os.Bundle;

import com.lvhm.covertocover.datamodels.BookResponse;

public interface BookAPICallback {
    void onBookFound(BookResponse.Item item, String isbn, Bundle response_bundle);
    void onNoBookFound(String isbn);
    void onAPIFailure(String errorMessage);
}
