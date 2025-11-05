package com.lvhm.covertocover.repo;

import android.content.Context;
import android.widget.Toast;

import com.lvhm.covertocover.PrintToast;
import com.lvhm.covertocover.exceptions.BookNotFoundException;
import com.lvhm.covertocover.exceptions.DuplicateBookException;
import com.lvhm.covertocover.models.Book;

import java.util.ArrayList;

public class BookContainer implements PrintToast {
    private PrintToast toast_printer;
    private static BookContainer instance;
    private ArrayList<Book> books;

    public BookContainer() {
        books = new ArrayList<>();
        this.toast_printer = this;
    }
    public BookContainer(PrintToast toastPrinter) {
        this.books = new ArrayList<>();
        this.toast_printer = toastPrinter;
    }
    public BookContainer(ArrayList<Book> books) {
        this.books = books;
        this.toast_printer = this;
    }
    public BookContainer(BookContainer book_container) {
        this.books = book_container.getBooks();
        this.toast_printer = book_container.getToastPrinter();
    }

    public static synchronized BookContainer getInstance() {
        if(instance == null) {
            instance = new BookContainer();
        }
        return instance;
    }

    public PrintToast getToastPrinter() {
        return toast_printer;
    }
    public void setToastPrinter() {
        this.toast_printer = this;
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
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public Book getBook(String isbn) {
        return findBook(isbn);
    }

    public void addBook(Context context, Book book) {
        try {
            if(findBook(book.getISBN()) != null) {
                throw new DuplicateBookException(book.getISBN());
            }
            books.add(book);
            toast_printer.printToast(context, "✅ Book added successfully");
        } catch(DuplicateBookException e) {
            toast_printer.printToast(context, "❌ " + e.getMessage());
        }
    }
    public void deleteBook(Context context, Book book) {
        try {
            if(findBook(book.getISBN()) == null) {
                throw new BookNotFoundException(book.getISBN());
            }
            books.remove(book);
        } catch(BookNotFoundException e) {
            toast_printer.printToast(context, "❌ " + e.getMessage());
        }
    }
    public void updateBook(Context context, Book book) {
        try {
            Book existing_book = findBook(book.getISBN());
            if (existing_book == null) {
                throw new BookNotFoundException(book.getISBN());
            }
            int index = books.indexOf(existing_book);
            books.set(index, book);
            toast_printer.printToast(context, "✅ Book updated successfully");
        } catch(BookNotFoundException e) {
            toast_printer.printToast(context, "❌ " + e.getMessage());
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

    public ArrayList<Book> getLatestWishlistedBooks(int number) {
        ArrayList<Book> wishlisted = getListWishlistedBooks();
        if(wishlisted.size() < number) {
            return wishlisted;
        } else {
            return new ArrayList<>(wishlisted.subList(wishlisted.size() - number, wishlisted.size()));
        }
    }

    @Override
    public void printToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
