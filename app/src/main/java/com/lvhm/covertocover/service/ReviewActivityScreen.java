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
import com.lvhm.covertocover.adapter.BookNavigationListener;
import com.lvhm.covertocover.adapter.OnBookClickListener;
import com.lvhm.covertocover.adapter.ReviewAdapter;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.repo.ReviewContainer;

import java.util.ArrayList;


public class ReviewActivityScreen extends Fragment implements OnBookClickListener {

    private ReviewAdapter review_adapter;
    private ReviewContainer review_container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review_activity, container, false);

        review_container = ReviewContainer.getInstance();

        RecyclerView recycler_view = view.findViewById(R.id.reading_activity_recycler);

        ArrayList<Review> all_reviews = review_container.getReviews();

        review_adapter = new ReviewAdapter(all_reviews, this);
        recycler_view.setAdapter(review_adapter);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Review> all_reviews = review_container.getReviews();
        review_adapter.updateData(all_reviews);
    }
    @Override
    public void onBookClick(Book book) {
        if (getActivity() instanceof BookNavigationListener) {
            ((BookNavigationListener) getActivity()).navigateToBookScreenFromBook(book);
        }
    }
}
