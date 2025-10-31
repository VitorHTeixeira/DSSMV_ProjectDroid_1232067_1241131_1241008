package com.lvhm.covertocover.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedBookViewModel extends ViewModel {
    private MutableLiveData<Book> selected_book = new MutableLiveData<>();

    public void selectBook(Book book) {
        selected_book.setValue(book);
    }

    public LiveData<Book> getSelectedBook() {
        return selected_book;
    }
}