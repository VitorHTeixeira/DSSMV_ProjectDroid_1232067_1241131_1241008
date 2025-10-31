package com.lvhm.covertocover.service;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.Chart;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.repo.ReviewContainer;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import com.lvhm.covertocover.R;

public class ProfileScreen extends Fragment {

    private BarChart review_barplot;
    private ReviewContainer review_container;
    private LinearLayout bookshelf_section;
    private LinearLayout reading_activity_section;
    private LinearLayout wishlisted_books_section;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        bookshelf_section = view.findViewById(R.id.bookshelf_section);
        bookshelf_section.setOnClickListener(v -> {
            Fragment see_all_fragment = new SeeAllBooksScreen();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, see_all_fragment)
                    .addToBackStack(null)
                    .commit();
        });

        reading_activity_section = view.findViewById(R.id.reading_activity_section);

        reading_activity_section.setOnClickListener(v -> {
            ReadingActivityScreen reading_activity_fragment = new ReadingActivityScreen();

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, reading_activity_fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        wishlisted_books_section = view.findViewById(R.id.wishlisted_books_section);

        wishlisted_books_section.setOnClickListener(v -> {
            WishlistedBooksScreen wishlisted_books_fragment = new WishlistedBooksScreen();

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, wishlisted_books_fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        review_container = ReviewContainer.getInstance();
        review_barplot = view.findViewById(R.id.review_barplot);

        setup_review_barchart();

        return view;
    }

    private void setup_review_barchart() {

        if (getContext() == null) {
            return;
        }

        int text_color = getTextColor();
        int accent_color = getAccentColor();

        review_barplot.getDescription().setEnabled(false);
        review_barplot.getLegend().setEnabled(false);
        review_barplot.setTouchEnabled(false);
        review_barplot.setDrawGridBackground(false);
        review_barplot.setDrawBorders(false);

        review_barplot.setNoDataText("No review yet. Add a new one!");
        review_barplot.setNoDataTextColor(text_color);
        review_barplot.getPaint(Chart.PAINT_INFO).setTextSize(40f);
        review_barplot.getPaint(Chart.PAINT_INFO).setColor(text_color);
        
        Map<Float, Integer> rating_counts = getRatingCounts();
        ArrayList<Review> all_reviews = review_container.getReviews();
        
        if (all_reviews == null || all_reviews.isEmpty()) {
            review_barplot.clear();
            review_barplot.invalidate();
            return;
        }
        
        ArrayList<BarEntry> entries = new ArrayList<>();
        final String[] labels = new String[10];
        int max_count = 0;

        for (int i = 0; i < 10; i++) {
            float rating_value = 0.5f + (i * 0.5f);
            labels[i] = String.valueOf(rating_value);
            int count = rating_counts.getOrDefault(rating_value, 0);
            entries.add(new BarEntry(i, (float) count));

            if (count > max_count) {
                max_count = count;
            }
        }

        if (max_count == 0) {
            max_count = 1;
        }

        BarDataSet data_set = new BarDataSet(entries, "Review Counts");
        data_set.setDrawValues(true);
        data_set.setValueTextColor(text_color);
        data_set.setColor(accent_color);
        data_set.setValueTextSize(14f);

        data_set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value > 0) {
                    return String.valueOf((int) value);
                }
                return "";
            }
        });

        BarData bar_data = new BarData(data_set);
        bar_data.setBarWidth(0.8f);
        
        review_barplot.setData(bar_data);
        review_barplot.setFitBars(true);

        review_barplot.setExtraOffsets(5f, 30f, 5f, 10f);

        review_barplot.getAxisRight().setEnabled(false);

        review_barplot.getAxisLeft().setEnabled(true);
        review_barplot.getAxisLeft().setDrawLabels(false);
        review_barplot.getAxisLeft().setDrawGridLines(false);
        review_barplot.getAxisLeft().setDrawAxisLine(false);
        review_barplot.getAxisLeft().setAxisMinimum(0f);

        float y_max = Math.max((float) max_count * 1.5f, 5f);
        review_barplot.getAxisLeft().setAxisMaximum(y_max);
        review_barplot.getAxisLeft().setGranularity(1f);
        
        XAxis x_axis = review_barplot.getXAxis();
        x_axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        x_axis.setDrawGridLines(false);
        x_axis.setDrawAxisLine(true);
        x_axis.setAxisLineColor(text_color);
        x_axis.setTextColor(text_color);
        x_axis.setGranularity(1f);
        x_axis.setTextSize(12f);
        x_axis.setLabelCount(10, false);
        x_axis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < labels.length) {
                    String label = labels[index];
                    if (label.endsWith(".5")) {
                        return label + "★";
                    } else {
                        return (int) Float.parseFloat(label) + "★";
                    }
                }
                return "";
            }
        });
        review_barplot.notifyDataSetChanged();
        review_barplot.invalidate();
    }

    private int getTextColor() {
        try {
            TypedValue typed_value = new TypedValue();
            if (getContext().getTheme().resolveAttribute(android.R.attr.textColorPrimary, typed_value, true)) {
                int color = typed_value.data;

                if (Color.alpha(color) > 0) {
                    return color;
                }
            }
        } catch (Exception e) {
        }

        return isDarkTheme() ? Color.WHITE : Color.BLACK;
    }

    private int getAccentColor() {
        try {
            TypedValue typed_value = new TypedValue();
            if (getContext().getTheme().resolveAttribute(android.R.attr.colorAccent, typed_value, true)) {
                int color = typed_value.data;
                if (Color.alpha(color) > 0) {
                    return color;
                }
            }

            if (getContext().getTheme().resolveAttribute(android.R.attr.colorPrimary, typed_value, true)) {
                int color = typed_value.data;
                if (Color.alpha(color) > 0) {
                    return color;
                }
            }
        } catch (Exception e) {
        }

        try {
            return ContextCompat.getColor(requireContext(), R.color.watch_green);
        } catch (Exception e) {
        }
        return Color.parseColor("#4CAF50");
    }

    private boolean isDarkTheme() {
        try {
            TypedValue typed_value = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, typed_value, true);
            int background_color = typed_value.data;
            int r = Color.red(background_color);
            int g = Color.green(background_color);
            int b = Color.blue(background_color);
            return (r + g + b) < 382;
        } catch (Exception e) {
            return false;
        }
    }
    private Map<Float, Integer> getRatingCounts() {
        Map<Float, Integer> counts = new HashMap<>();
        
        for (int i = 0; i < 10; i++) {
            counts.put(0.5f + (i * 0.5f), 0);
        }

        ArrayList<Review> all_reviews = review_container.getReviews();
        if (all_reviews == null || all_reviews.isEmpty()) {
            return counts;
        }

        for (Review review : all_reviews) {
            float rating = (float) review.getRating();
            int current_count = counts.getOrDefault(rating, 0);
            counts.put(rating, current_count + 1);
        }

        return counts;
    }
}
