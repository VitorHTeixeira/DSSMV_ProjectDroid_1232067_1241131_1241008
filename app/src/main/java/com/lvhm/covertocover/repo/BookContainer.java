package com.lvhm.covertocover.repo;

import android.content.Context;
import android.util.Log;

import com.lvhm.covertocover.NotificationCentral;
import com.lvhm.covertocover.exceptions.BookNotFoundException;
import com.lvhm.covertocover.exceptions.DuplicateBookException;
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

    private Book findBook(String isbn) {
        boolean has_valid_isbn = isbn != null && !isbn.trim().isEmpty();
        for (Book existing_book : this.books) {
            if (has_valid_isbn) {
                boolean isbn_matches = existing_book.getISBN() != null && existing_book.getISBN().equals(isbn);
                if (isbn_matches) {
                    return existing_book;
                }
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


    public void addBook(Context context, Book book) {
        try {
            if(findBook(book.getISBN()) != null) {
                throw new DuplicateBookException(book.getISBN());
            }
            Log.i("BookContainer_DEBUG", "Adicionando livro: " + book.getName()); // Use Log.i para Info
            books.add(book);
        } catch(DuplicateBookException e) {
            NotificationCentral.showNotification(context, e.getMessage());
        }
    }
    public void deleteBook(Context context, Book book) {
        try {

            if(findBook(book.getISBN()) == null) {
                throw new BookNotFoundException(book.getISBN());
            }
            books.remove(book);
        } catch(BookNotFoundException e) {
            NotificationCentral.showNotification(context, e.getMessage());
        }
    }
    public void updateBook(Context context, Book book) {
        try {
            if(findBook(book.getISBN()) == null) {
                throw new BookNotFoundException(book.getISBN());
            }
            deleteBook(context, book);
            addBook(context, book);
        } catch(BookNotFoundException e) {
            NotificationCentral.showNotification(context, e.getMessage());
        }
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
