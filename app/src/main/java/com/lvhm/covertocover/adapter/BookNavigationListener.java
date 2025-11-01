package com.lvhm.covertocover.adapter;

import android.os.Bundle;

import com.lvhm.covertocover.models.Book;

public interface BookNavigationListener {
    void navigateToBookScreenFromBook(Book book);
    void navigateToBookScreenFromAPI(Bundle api_bundle);
}
