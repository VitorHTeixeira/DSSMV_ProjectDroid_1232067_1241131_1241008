package com.lvhm.covertocover.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.R;
import com.lvhm.covertocover.adapter.BookNavigationListener;
import com.lvhm.covertocover.adapter.SeeAllAdapter;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.SharedBookViewModel;
import com.lvhm.covertocover.repo.BookContainer;

import java.util.ArrayList;

public class SeeAllTabsScreen extends Fragment {
    private static final String ARG_LIST_TYPE = "list_type";
    public static final String TYPE_WANT_TO_READ = "WANT_TO_READ";
    public static final String TYPE_READING = "READING";
    public static final String TYPE_READ = "READ";

    private RecyclerView book_view;
    private BookContainer book_container;
    private String list_type;

    public static SeeAllTabsScreen newInstance(String listType) {
        SeeAllTabsScreen fragment = new SeeAllTabsScreen();
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
        book_view = view.findViewById(R.id.book_list_recycler_view);
        load_book_data();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        load_book_data();
    }
    private void load_book_data() {
        ArrayList<Book> book_list;
        switch (list_type) {
            case TYPE_WANT_TO_READ:
                book_list = book_container.getListWishlistedBooks();
                SeeAllAdapter wishlist_tab_adapter = new SeeAllAdapter(book_list, this::openBookScreen);
                book_view.setAdapter(wishlist_tab_adapter);
                break;
            case TYPE_READING:
                book_list = book_container.getListOnGoingBooks();
                SeeAllAdapter on_going_tab_adapter = new SeeAllAdapter(book_list, this::openBookScreen);
                book_view.setAdapter(on_going_tab_adapter);
                break;
            case TYPE_READ:
                book_list = book_container.getListReadBooks();
                SeeAllAdapter read_tab_adapter = new SeeAllAdapter(book_list, this::openBookScreen);
                book_view.setAdapter(read_tab_adapter);
                break;
        }
    }

    private void openBookScreen(Book book) {
        if(getActivity() instanceof BookNavigationListener) {
            ((BookNavigationListener) getActivity()).navigateToBookScreenFromBook(book);
        }
    }
}
