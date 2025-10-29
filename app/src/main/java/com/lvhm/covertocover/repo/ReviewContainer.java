package com.lvhm.covertocover.repo;

import com.lvhm.covertocover.models.Review;

import java.util.ArrayList;
import java.util.Calendar;

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

    public double getAverageRating() {
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        double scale = Math.pow(10, reviews.size());
        return Math.round(sum * scale) / scale;
    }
    public int getTotalBooksThisYear() {
        int count = 0;
        for (Review review : reviews) {
            if(review.getDate().getYear() == Calendar.getInstance().get(Calendar.YEAR)) {
                count++;
            }
        }
        return count;
    }
//    public int getMostUsedRating() {
//        int[][] counts = new int[11][];
//        for (Review review : reviews) {
//            counts[review.getRating()]++;
//        }
//        int max = 0;
//        int index = 0;
//        for (int i = 0; i < counts.length; i++) {
//            if(counts[i] > max) {
//                max = counts[i];
//                index = i;
//            }
//        }
//        return index;
//    }
}
