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
import com.lvhm.covertocover.adapter.ReadingActivityAdapter;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.repo.BookContainer;

import java.util.ArrayList;


public class ReadingActivityScreen extends Fragment {

    private ReadingActivityAdapter adapter;
    private BookContainer book_container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reading_activity, container, false);

        book_container = BookContainer.getInstance();

        RecyclerView recycler_view = view.findViewById(R.id.reading_activity_recycler);

        ArrayList<Book> reading_books = book_container.getListOnGoingBooks();

        adapter = new ReadingActivityAdapter(reading_books);
        recycler_view.setAdapter(adapter);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Book> reading_books = book_container.getListOnGoingBooks();
        adapter.updateData(reading_books);
    }
}
