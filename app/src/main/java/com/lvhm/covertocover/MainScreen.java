package com.lvhm.covertocover;

import android.graphics.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lvhm.covertocover.datamodels.BookResponse;
import com.lvhm.covertocover.models.Book;

public class MainScreen extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FrameLayout add_book = view.findViewById(R.id.add_book_button);
        add_book.setOnClickListener(v -> {
            Fragment fragment = new ManualBookScreen();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        SearchView search_bar = view.findViewById(R.id.search_bar);
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                BookAPIClient.getBookFromAPI(requireContext(), query, new BookAPICallback() {
                    @Override
                    public void onBookFound(BookResponse.Item item, String isbn, Bundle response_bundle) {
                        navigateToBookScreen(item, isbn, response_bundle);
                    }

                    @Override
                    public void onNoBookFound(String isbn) {
                        navigateToManualBookScreen(isbn);
                    }

                    @Override
                    public void onAPIFailure(String errorMessage) {}
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }

    private void navigateToBookScreen(BookResponse.Item item, String isbn, Bundle response_bundle) {
        Fragment book_screen_fragment = new BookScreen();
        Bundle book_info = item.getVolumeInfo().getBundle();
        book_info.putString("isbn", isbn);
        book_info.putBundle("response", response_bundle);
        book_screen_fragment.setArguments(book_info);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, book_screen_fragment)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToManualBookScreen(String isbn) {
        Fragment manual_book_screen_fragment = new ManualBookScreen();
        Bundle book_info = new Bundle();
        book_info.putString("isbn", isbn);
        manual_book_screen_fragment.setArguments(book_info);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, manual_book_screen_fragment)
                .addToBackStack(null)
                .commit();
    }
}