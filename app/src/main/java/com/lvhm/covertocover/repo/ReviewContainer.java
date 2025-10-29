package com.lvhm.covertocover.repo;

import com.lvhm.covertocover.models.Review;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReviewContainer {
    private static ReviewContainer instance = new ReviewContainer();
    private ArrayList<Review> reviews;

    private ReviewContainer() {
        reviews = new ArrayList<>();
    }
    private ReviewContainer(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
    public static ReviewContainer getInstance() {
        if(instance == null) {
            instance = new ReviewContainer();
        }
        return instance;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public ArrayList<Review> getLatestReviews(int number) {
        if(reviews.size() < number) {
            return reviews;
        } else {
            return new ArrayList<>(reviews.subList(reviews.size() - number, reviews.size()));
        }
    }

    public void addReview(Review review) {
        reviews.add(review);
    }
    public void deleteReview(Review review) {
        reviews.remove(review);
    }
    public void updateReview(Review review) {
        deleteReview(review);
        addReview(review);
    }

    public double getAverageRatingThisYear() {
        if(reviews.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        int count = 0;
        for (Review review : reviews) {
            if(review.getDate().getYear() == Calendar.getInstance().get(Calendar.YEAR)) {
                sum += review.getRating();
                count++;
            }
        }
        if(count == 0) {
            return 0.0;
        }
        double average = sum / count;
        double scale = Math.pow(10, 2);
        return Math.round(average * scale) / scale;
    }
    public int getTotalReviewsThisYear() {
        if(reviews.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Review review : reviews) {
            if(review.getDate().getYear() == Calendar.getInstance().get(Calendar.YEAR)) {
                count++;
            }
        }
        return count;
    }
    public double getMostUsedRatingThisYear() {
        if(reviews.isEmpty()) {
            return 0.0;
        }
        ArrayList<Review> this_year_reviews = new ArrayList<>();
        HashMap<Double, Integer> counts = new HashMap<>();
        for (Review review : reviews) {
            if(review.getDate().getYear() == Calendar.getInstance().get(Calendar.YEAR)) {
                this_year_reviews.add(review);
            }
        }
        for (Review review : this_year_reviews) {
            double rating = review.getRating();
            counts.merge(rating, 1, Integer::sum);
        }
        if(counts.isEmpty()) {
            return 0.0;
        }
        int max_value = (Collections.max(counts.values()));
        double max_key = 0.0;
        for (Map.Entry<Double, Integer> entry :
                counts.entrySet()) {
            if (entry.getValue() == max_value) {
                max_key = entry.getKey();
            }
        }
        return max_key;
    }

    public String getBestMonthThisYear() {
        if(reviews.isEmpty()) {
            return "?";
        }
        ArrayList<Review> this_year_reviews = new ArrayList<>();
        HashMap<Integer, Integer> counts = new HashMap<>();
        for (Review review : reviews) {
            if(review.getDate().getYear() == Calendar.getInstance().get(Calendar.YEAR)) {
                this_year_reviews.add(review);
            }
        }
        for (Review review : this_year_reviews) {
            int month = review.getDate().getMonth();
            counts.merge(month, 1, Integer::sum);
        }
        if(counts.isEmpty()) {
            return "?";
        }
        int max_value = (Collections.max(counts.values()));
        int max_key = 0;
        for (Map.Entry<Integer, Integer> entry :
                counts.entrySet()) {
            if (entry.getValue() == max_value) {
                max_key = entry.getKey();
            }
        }
        switch (max_key) {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "?";
        }
    }
}
