package com.lvhm.covertocover.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Review implements Parcelable {
    private Book book;
    private double rating;
    private String review_text;
    private Date date;

    public Review(Book book, double rating, String reviewText, Date date) {
        this.book = book;
        this.rating = rating;
        this.review_text = reviewText;
        this.date = date;
    }

    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public String getReviewText() {
        return review_text;
    }
    public void setReviewText(String review_text) {
        this.review_text = review_text;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    // Parcelable

    protected Review(Parcel in) {
        book = in.readParcelable(book.getClass().getClassLoader());
        rating = in.readDouble();
        review_text = in.readString();
        date = (Date) in.readSerializable();
    }
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(book, flags);
        parcel.writeDouble(rating);
        parcel.writeString(review_text);
        parcel.writeSerializable(date);
    }
}
