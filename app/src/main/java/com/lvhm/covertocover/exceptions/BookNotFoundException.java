package com.lvhm.covertocover.exceptions;

public class BookNotFoundException extends Exception {
    public BookNotFoundException(String isbn) {
        super("Error: Unable to find book with ISBN " + isbn);
    }
}
