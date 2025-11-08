package com.lvhm.covertocover.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

public class Review implements Parcelable {
    @SerializedName("_id")
    private String _id;

    @Expose
    private String user_uuid;
    @Expose
    private String uuid;
    @Expose
    private Book book;
    @Expose
    private double rating;
    @Expose
    private String review_text;
    @Expose
    private Date date;

    public Review() {
        this.user_uuid = "";
        this.uuid = "";
        this.book = null;
        this.rating = 0.0;
        this.review_text = "";
        this.date = null;
    }

    public Review(Book book, String user_uuid, double rating, String review_text, Date date) {
        this.user_uuid = user_uuid;
        this.uuid = UUID.randomUUID().toString();
        this.book = book;
        this.rating = rating;
        this.review_text = review_text;
        this.date = date;
    }

    public Review(Book book, String user_uuid, String uuid, double rating, String review_text, Date date) {
        this.user_uuid = user_uuid;
        this.uuid = uuid;
        this.book = book;
        this.rating = rating;
        this.review_text = review_text;
        this.date = date;
    }

    public String get_id() {
        return _id;
    }
    public void set_id(String id) {
        this._id = id;
    }
    public String getUserUUID() {
        return user_uuid;
    }
    public void setUserUUID(String user_uuid) {
        this.user_uuid = user_uuid;
    }
    public String getUUID() {
        return this.uuid;
    }
    public void setUUID(String uuid) {
        this.uuid = uuid;
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
        user_uuid = in.readString();
        uuid = in.readString();
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
        parcel.writeString(user_uuid);
        parcel.writeString(uuid);
        parcel.writeDouble(rating);
        parcel.writeString(review_text);
        parcel.writeSerializable(date);
    }
}
