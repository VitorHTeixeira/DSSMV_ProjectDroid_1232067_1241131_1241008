package com.lvhm.covertocover.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.lvhm.covertocover.R;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.models.SharedBookReviewViewModel;
import com.lvhm.covertocover.repo.ReviewContainer;

import java.util.Date;

public class BookReviewOverlay extends Fragment {
    RatingBar review_rating_bar;
    TextInputEditText review_text_input;
    private Review current_review;

    public static BookReviewOverlay newInstance(Review review) {
        BookReviewOverlay fragment = new BookReviewOverlay();
        Bundle bundle = new Bundle();
        bundle.putParcelable("review_recycler", review);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overlay_review, container, false);
        SharedBookReviewViewModel view_model = new ViewModelProvider(requireActivity()).get(SharedBookReviewViewModel.class);

        review_rating_bar = view.findViewById(R.id.review_rating_bar);
        review_text_input = view.findViewById(R.id.review_text_input);

        if(getArguments() != null) {
            current_review = getArguments().getParcelable("review_recycler");
        }

        if (current_review != null) {
            review_rating_bar.setRating((float) current_review.getRating());
            review_text_input.setText(current_review.getReviewText());
        }

        RelativeLayout cancel_button = view.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        RelativeLayout save_button = view.findViewById(R.id.save_button);
        save_button.setOnClickListener(v -> {
            Book book = view_model.getSelectedBook().getValue();
            if (book != null) {
                saveReview(book);
                Toast.makeText(requireContext(), "Review saved successfully", Toast.LENGTH_SHORT).show();
            }
            getParentFragmentManager().popBackStack();
        });

        return view;
    }

    private void saveReview(Book book) {
        ReviewContainer review_container = ReviewContainer.getInstance();

        float review_rating = review_rating_bar.getRating();
        String review_text = "";
        if(review_text_input.getText() != null) {
            review_text = review_text_input.getText().toString();
        }
        Date review_date = new Date();

        if (current_review != null) {
            current_review.setRating(review_rating);
            current_review.setReviewText(review_text);
            current_review.setDate(review_date);
            review_container.updateReview(current_review);
        } else {
            Review review = new Review(book, review_rating, review_text, review_date);
            review_container.addReview(review);
        }

        SharedBookReviewViewModel book_view_model = new ViewModelProvider(requireActivity()).get(SharedBookReviewViewModel.class);
        book_view_model.notifyReviewUpdated();
    }
}
