package com.lvhm.covertocover.repo;

import com.lvhm.covertocover.models.Review;

import java.util.ArrayList;

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
}
