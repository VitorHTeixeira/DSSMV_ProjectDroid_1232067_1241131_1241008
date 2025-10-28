package com.lvhm.covertocover.repo;

import com.lvhm.covertocover.models.Book;

import java.util.ArrayList;

public class BookContainer {
    private static BookContainer instance;
    private ArrayList<Book> books;

    public BookContainer() {
        books = new ArrayList<>();
    }
    public BookContainer(ArrayList<Book> books) {
        this.books = books;
    }

    public static synchronized BookContainer getInstance() {
        if(instance == null) {
            instance = new BookContainer();
        }
        return instance;
    }

    private Book findBook(String isbn, String name) {
        for(Book book : this.books) {
            if(book.getISBN().equals(isbn) || book.getName().equals(name)) {
                return book;
            }
        }
        return null;
    }
    private ArrayList<Book> findReadBooks(boolean read) {
        ArrayList<Book> read_books = new ArrayList<>();
        for(Book book : this.books) {
            if(book.getRead() == read) {read_books.add(book);}
        }
        return read_books;
    }
    private ArrayList<Book> findOnGoingBooks(boolean onGoing) {
        ArrayList<Book> onGoing_books = new ArrayList<>();
        for(Book book : this.books) {
            if(book.getOnGoing() == onGoing) {onGoing_books.add(book);}
        }
        return onGoing_books;
    }
    private ArrayList<Book> findWishlistedBooks(boolean wishlisted) {
        ArrayList<Book> wishlisted_books = new ArrayList<>();
        for(Book book : this.books) {
            if(book.getIsWishlisted() == wishlisted) {wishlisted_books.add(book);}
        }
        return wishlisted_books;
    }



    public ArrayList<Book> getBooks() {
        return books;
    }


    public void addBook(Book book) {
        if(findBook(book.getISBN(), book.getName()) != null) {
            // throw exception
            return;
        }
        books.add(book);
    }
    public void deleteBook(Book book) {
        if(findBook(book.getISBN(), book.getName()) == null) {
            // throw exception
            return;
        }
        books.remove(book);
    }
    public void updateBook(Book book) {
        if(findBook(book.getISBN(), book.getName()) == null) {
            // throw exception
            return;
        }
        deleteBook(book);
        addBook(book);
    }
    public ArrayList<Book> getListReadBooks() {
        return findReadBooks(true);
    }
    public ArrayList<Book> getListOnGoingBooks() {
        return findOnGoingBooks(true);
    }
    public ArrayList<Book> getListWishlistedBooks() {
        return findWishlistedBooks(true);
    }
}
