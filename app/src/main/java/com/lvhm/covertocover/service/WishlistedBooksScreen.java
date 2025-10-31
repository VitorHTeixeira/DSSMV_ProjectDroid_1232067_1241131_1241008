package com.lvhm.covertocover.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;
import com.lvhm.covertocover.adapter.WishlistAdapter;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.repo.BookContainer;

import java.util.ArrayList;

public class WishlistedBooksScreen extends Fragment {
    private WishlistAdapter adapter;
    private BookContainer book_container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wishlisted_books, container, false);


        book_container = BookContainer.getInstance();

        RecyclerView recycler_view = view.findViewById(R.id.wishlisted_books_recycler);

        ArrayList<Book> wishlisted_books = book_container.getListWishlistedBooks();

        adapter = new WishlistAdapter(wishlisted_books);
        recycler_view.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Book> wishlisted_books = book_container.getListWishlistedBooks();
        adapter.updateData(wishlisted_books);
    }
}