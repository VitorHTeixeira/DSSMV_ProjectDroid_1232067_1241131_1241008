package com.lvhm.covertocover.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Book implements Parcelable {
    @SerializedName("_id")
    private String _id;

    @Expose
    private String userToken;
    @Expose
    private String uuid;
    @Expose
    private String isbn;
    @Expose
    private String name;
    @Expose
    private ArrayList<String> author;
    @Expose
    private int year;
    @Expose
    private ArrayList<String> genre;
    @Expose
    private boolean read;
    @Expose
    private int page_count;
    @Expose
    private String cover_image64;
    @Expose(serialize = false, deserialize = false)
    private Bitmap cover_image;
    @Expose
    private boolean isWishlisted;
    @Expose
    private boolean onGoing;

    public Book() {
        this._id = "";
        this.uuid = UUID.randomUUID().toString();
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

    public String getUserToken() { return userToken; }
    public void setUserToken(String userToken) { this.userToken = userToken; }
    public String get_id() {
        return _id;
    }
    public void set_id(String id) {
        this._id = id;
    }
    public String getUUID() {
        return this.uuid;
    }
    public void setUUID(String uuid) {
        this.uuid = uuid;
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
        if(cover_image != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            cover_image.compress(Bitmap.CompressFormat.WEBP, 80, baos);
            byte[] byteArray = baos.toByteArray();
            this.cover_image64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        else {
            this.cover_image64 = null;
        }
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

    private Book(Parcel in) {
        _id = in.readString();
        uuid = in.readString();
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
        parcel.writeString(_id);
        parcel.writeString(uuid);
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
