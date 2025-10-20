package com.lvhm.covertocover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BookScreen extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book, container, false);

        RelativeLayout add_review = view.findViewById(R.id.review_button);
        add_review.setOnClickListener(v -> {
            Fragment fragment = new BookReviewOverlay();
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        Button tempButton = view.findViewById(R.id.temp_test_button);
        tempButton.setOnClickListener(v -> {
            Fragment fragment = new BookHistoryReviewOverlay();
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
