package com.lvhm.covertocover.service;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lvhm.covertocover.adapter.ReviewAdapter;
import com.lvhm.covertocover.adapter.WishlistAdapter;
import com.lvhm.covertocover.api.BookAPICallback;
import com.lvhm.covertocover.api.BookAPIClient;
import com.lvhm.covertocover.R;
import com.lvhm.covertocover.datamodels.BookResponse;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ReviewContainer;

import java.util.ArrayList;

public class MainScreen extends Fragment {
    private ReviewAdapter review_adapter;
    private WishlistAdapter wishlist_adapter;
    private BookContainer book_container;
    private ReviewContainer review_container;
    private TextView average_rating_text, total_books_text, best_month_text, most_used_rating_text;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        book_container = BookContainer.getInstance();
        review_container = ReviewContainer.getInstance();

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

        RecyclerView review_carousel = view.findViewById(R.id.review_carousel);
        ArrayList<Review> review_data = review_container.getLatestReviews(3);
        review_adapter = new ReviewAdapter(review_data);
        review_carousel.setAdapter(review_adapter);

        RecyclerView wishlist_carousel = view.findViewById(R.id.wishlist_carousel);
        ArrayList<Book> wishlist_data = book_container.getLatestWishlistedBooks(5);
        wishlist_adapter = new WishlistAdapter(wishlist_data);
        wishlist_carousel.setAdapter(wishlist_adapter);


        average_rating_text = view.findViewById(R.id.average_rating_text);
        average_rating_text.append(String.valueOf(review_container.getAverageRatingThisYear()));
        total_books_text = view.findViewById(R.id.total_books_text);
        total_books_text.append(String.valueOf(review_container.getTotalReviewsThisYear()));
        best_month_text = view.findViewById(R.id.best_month_text);
        best_month_text.append(review_container.getBestMonthThisYear());
        most_used_rating_text = view.findViewById(R.id.most_used_rating_text);
        most_used_rating_text.append(String.valueOf(review_container.getMostUsedRatingThisYear()));

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        review_adapter.updateData(review_container.getLatestReviews(3));
        wishlist_adapter.updateData(book_container.getLatestWishlistedBooks(5));
        Log.d("MainScreen", "onResume called!");
        Log.d("MainScreen", "Average: " + review_container.getAverageRatingThisYear());
        average_rating_text.setText("Average Rating: " + review_container.getAverageRatingThisYear());
        total_books_text.setText("Total Books: " + review_container.getTotalReviewsThisYear());
        best_month_text.setText("Best Month: " + review_container.getBestMonthThisYear());
        most_used_rating_text.setText("Most Used Rating: " + review_container.getMostUsedRatingThisYear());

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