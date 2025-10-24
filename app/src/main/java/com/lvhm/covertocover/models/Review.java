package com.lvhm.covertocover.models;

import java.util.Date;

public class Review {
    private ReadBook book;
    private double rating;
    private String reviewText;
    private Date date;

    public Review(ReadBook book, double rating, String reviewText, Date date) {
        this.book = book;
        this.rating = rating;
        this.reviewText = reviewText;
        this.date = date;
    }

    public ReadBook getBook() {
        return book;
    }
    public void setBook(ReadBook book) {
        this.book = book;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public String getReviewText() {
        return reviewText;
    }
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
