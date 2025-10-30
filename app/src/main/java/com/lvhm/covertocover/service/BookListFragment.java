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
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.repo.BookContainer;

import java.util.ArrayList;

public class BookListFragment extends Fragment {
    private static final String ARG_LIST_TYPE = "list_type";
    public static final String TYPE_WANT_TO_READ = "WANT_TO_READ";
    public static final String TYPE_READING = "READING";
    public static final String TYPE_READ = "READ";

    // private RecyclerView recycler_view;
    private BookContainer book_container;
    private String list_type;

    public static BookListFragment newInstance(String listType) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LIST_TYPE, listType);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        
        if (getArguments() != null) {
            list_type = getArguments().getString(ARG_LIST_TYPE);
        }
        book_container = BookContainer.getInstance();
        // recycler_view = view.findViewById(R.id.book_list_recycler_view);
        //load_book_data();

       
        return view;
    }

    /*private void load_book_data() {
        if (book_container == null || list_type == null) return;

        ArrayList<Book> book_list = new ArrayList<>();

        switch (list_type) {
            case TYPE_WANT_TO_READ:
                book_list = book_container.getListWishlistedBooks();
                break;
            case TYPE_READING:
                book_list = book_container.getListOnGoingBooks();
                break;
            case TYPE_READ:
                book_list = book_container.getListReadBooks();
                break;
        }
    } */
}
