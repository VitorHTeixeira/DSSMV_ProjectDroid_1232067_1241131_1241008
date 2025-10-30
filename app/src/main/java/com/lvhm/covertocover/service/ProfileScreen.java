package com.lvhm.covertocover.service;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.Chart;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.repo.ReviewContainer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        review_container = ReviewContainer.getInstance();
        review_barplot = view.findViewById(R.id.review_barplot);

        setup_review_barchart();

        return view;
    }

    private void setup_review_barchart() {

        if (getContext() == null) {
            return;
        }

        int textColor = getTextColor();
        int accentColor = getAccentColor();

        review_barplot.getDescription().setEnabled(false);
        review_barplot.getLegend().setEnabled(false);
        review_barplot.setTouchEnabled(false);
        review_barplot.setDrawGridBackground(false);
        review_barplot.setDrawBorders(false);

        review_barplot.setNoDataText("No review yet. Add a new one!");
        review_barplot.setNoDataTextColor(textColor);
        review_barplot.getPaint(Chart.PAINT_INFO).setTextSize(18f);
        review_barplot.getPaint(Chart.PAINT_INFO).setColor(textColor);
        
        Map<Float, Integer> ratingCounts = getRatingCounts(); // Snake Case
        ArrayList<Review> all_reviews = review_container.getReviews();
        
        if (all_reviews == null || all_reviews.isEmpty()) {
            review_barplot.clear();
            review_barplot.invalidate();
            return;
        }
        
        ArrayList<BarEntry> entries = new ArrayList<>();
        final String[] labels = new String[10];
        int maxCount = 0;

        for (int i = 0; i < 10; i++) {
            float ratingValue = 0.5f + (i * 0.5f); // 0.5, 1.0, 1.5, ..., 5.0
            labels[i] = String.valueOf(ratingValue);
            int count = ratingCounts.getOrDefault(ratingValue, 0);
            entries.add(new BarEntry(i, (float) count));

            if (count > maxCount) {
                maxCount = count;
            }
        }

        if (maxCount == 0) {
            maxCount = 1;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Review Counts");
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(textColor);
        dataSet.setColor(accentColor);
        dataSet.setValueTextSize(14f);

        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value > 0) {
                    return String.valueOf((int) value);
                }
                return "";
            }
        });

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.8f);
        
        review_barplot.setData(barData);
        review_barplot.setFitBars(true);

        review_barplot.setExtraOffsets(5f, 30f, 5f, 10f);

        review_barplot.getAxisRight().setEnabled(false);

        review_barplot.getAxisLeft().setEnabled(true);
        review_barplot.getAxisLeft().setDrawLabels(false);
        review_barplot.getAxisLeft().setDrawGridLines(false);
        review_barplot.getAxisLeft().setDrawAxisLine(false);
        review_barplot.getAxisLeft().setAxisMinimum(0f);

        float yMax = Math.max((float) maxCount * 1.5f, 5f);
        review_barplot.getAxisLeft().setAxisMaximum(yMax);
        review_barplot.getAxisLeft().setGranularity(1f);
        
        XAxis xAxis = review_barplot.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(textColor);
        xAxis.setTextColor(textColor);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(10, false);
        xAxis.setValueFormatter(new ValueFormatter() {
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
            TypedValue typedValue = new TypedValue();
            if (getContext().getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)) {
                int color = typedValue.data;

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
            TypedValue typedValue = new TypedValue();
            if (getContext().getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true)) {
                int color = typedValue.data;
                if (Color.alpha(color) > 0) {
                    return color;
                }
            }

            if (getContext().getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true)) {
                int color = typedValue.data;
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
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
            int backgroundColor = typedValue.data;
            int r = Color.red(backgroundColor);
            int g = Color.green(backgroundColor);
            int b = Color.blue(backgroundColor);
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

        ArrayList<Review> allReviews = review_container.getReviews();
        if (allReviews == null || allReviews.isEmpty()) {
            return counts;
        }

        for (Review review : allReviews) {
            float rating = (float) review.getRating();
            int currentCount = counts.getOrDefault(rating, 0);
            counts.put(rating, currentCount + 1);
        }

        return counts;
    }
}
