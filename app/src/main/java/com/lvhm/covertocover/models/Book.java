package com.lvhm.covertocover.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Book implements Parcelable {
    protected String isbn;
    protected String name;
    protected ArrayList<String> author;
    protected int year;
    protected ArrayList<String> genre;
    protected boolean read;
    protected int page_count;
    protected String cover_image64;
    protected Bitmap cover_image;
    protected boolean isWishlisted;
    protected boolean onGoing;

    public Book() {
        this.isbn = "Unknown";
        this.name = "Unknown";
        this.author = new ArrayList<>(List.of("Unknown"));
        this.year = 0;
        this.genre = new ArrayList<>(List.of("Unknown"));
        this.read = false;
        this.page_count = 0;
        this.cover_image64 = null;
        this.cover_image = null;
        this.isWishlisted = false;
        this.onGoing = false;
    }

    public String getISBN() {
        return this.isbn;
    }
    public void setISBN(String isbn) {
        this.isbn = isbn;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<String> getAuthor() {
        return this.author;
    }
    public void setAuthor(ArrayList<String> author) {
        this.author = author;
    }
    public void setAuthor(String authors) {
        this.author = (ArrayList<String>) Arrays.asList(authors.split(", "));
    }
    public int getYear() {
        return this.year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public ArrayList<String> getGenre() {
        return this.genre;
    }
    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }
    public boolean getRead() {
        return this.read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }
    public int getPageCount() {
        return this.page_count;
    }
    public void setPageCount(int page_count) {
        this.page_count = page_count;
    }
    public String getCoverImageBase64() {
        return this.cover_image64;
    }
    public Bitmap getCoverImage() {
        String image_base64 = this.cover_image64;
        if (image_base64 != null) {
            try {
                byte[] byteArray = Base64.decode(image_base64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                if (bitmap != null) {
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public void setCoverImageBase64(Bitmap cover_image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cover_image.compress(Bitmap.CompressFormat.WEBP, 80, baos);
        byte[] byteArray = baos.toByteArray();
        this.cover_image64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public void setCoverImage(Bitmap cover_image) {
        this.cover_image = cover_image;
    }
    public boolean getIsWishlisted() {
        return this.isWishlisted;
    }
    public void setIsWishlisted(boolean isWishlisted) {
        this.isWishlisted = isWishlisted;
    }
    public boolean getOnGoing() {
        return this.onGoing;
    }
    public void setOnGoing(boolean onGoing) {
        this.onGoing = onGoing;
    }


    // Parcelable

    protected Book(Parcel in) {
        isbn = in.readString();
        name = in.readString();
        author = in.createStringArrayList();
        year = in.readInt();
        genre = in.createStringArrayList();
        read = in.readBoolean();
        page_count = in.readInt();
        cover_image = in.readParcelable(Bitmap.class.getClassLoader());
        isWishlisted = in.readBoolean();
        onGoing = in.readBoolean();
    }
    public static final Creator<Book> CREATOR = new Creator<>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(isbn);
        parcel.writeString(name);
        parcel.writeStringList(author);
        parcel.writeInt(year);
        parcel.writeStringList(genre);
        parcel.writeBoolean(read);
        parcel.writeInt(page_count);
        parcel.writeString(cover_image64);
        parcel.writeParcelable(cover_image, flags);
        parcel.writeBoolean(isWishlisted);
        parcel.writeBoolean(onGoing);
    }
}
