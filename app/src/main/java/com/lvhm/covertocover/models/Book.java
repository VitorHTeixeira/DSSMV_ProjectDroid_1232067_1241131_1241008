package com.lvhm.covertocover.models;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Book {
    protected String isbn;
    protected String name;
    protected ArrayList<String> author;
    protected int year;
    protected String publisher;
    protected ArrayList<String> genre;
    protected boolean read;
    protected int edition;
    protected String idiom;
    protected int page_count;
    protected Bitmap cover_image;
    protected boolean isWishlisted;
    protected boolean onGoing;

    public Book() {
        this.isbn = "Unknown";
        this.name = "Unknown";
        this.author = (ArrayList<String>) List.of("Unknown");
        this.year = 0;
        this.publisher = "Unknown";
        this.genre = (ArrayList<String>) List.of("Unknown");
        this.read = false;
        this.edition = 0;
        this.idiom = "Unknown";
        this.page_count = 0;
        this.cover_image = null;
        this.isWishlisted = false;
        this.onGoing = false;
    }
    public Book(String name, ArrayList<String> author) {
        this.name = name;
        this.author = author;
        this.isbn = "Unknown";
        this.year = 0;
        this.publisher = "Unknown";
        this.genre = (ArrayList<String>) List.of("Unknown");
        this.read = false;
        this.edition = 0;
        this.idiom = "Unknown";
        this.page_count = 0;
        this.cover_image = null;
        this.isWishlisted = false;
        this.onGoing = false;
    }
    public Book(String name, ArrayList<String> author, Bitmap cover_image) {
        this.name = name;
        this.author = author;
        this.isbn = "Unknown";
        this.year = 0;
        this.publisher = "Unknown";
        this.genre = (ArrayList<String>) List.of("Unknown");
        this.read = false;
        this.edition = 0;
        this.idiom = "Unknown";
        this.page_count = 0;
        this.cover_image = cover_image;
        this.isWishlisted = false;
        this.onGoing = false;
    }
    public Book(String isbn, String name, ArrayList<String> author, int year, int edition, int page_count, Bitmap cover_image) {
        this.name = name;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.publisher = "Unknown";
        this.genre = (ArrayList<String>) List.of("Unknown");
        this.read = false;
        this.edition = edition;
        this.idiom = "Unknown";
        this.page_count = page_count;
        this.cover_image = cover_image;
        this.isWishlisted = false;
        this.onGoing = false;
    }
    public Book(String isbn, String name, ArrayList<String> author, int year, String publisher, ArrayList<String> genre, int edition, String idiom, int page_count, Bitmap cover_image) {
        this.name = name;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.publisher = publisher;
        this.genre = genre;
        this.read = false;
        this.edition = edition;
        this.idiom = idiom;
        this.page_count = page_count;
        this.cover_image = cover_image;
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
    public String getPublisher() {
        return this.publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
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
    public int getEdition() {
        return this.edition;
    }
    public void setEdition(int edition) {
        this.edition = edition;
    }
    public String getIdiom() {
        return this.idiom;
    }
    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }
    public int getPageCount() {
        return this.page_count;
    }
    public void setPageCount(int page_count) {
        this.page_count = page_count;
    }
    public Bitmap getCoverImage() {
        return this.cover_image;
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
}
