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
}
