package com.lvhm.covertocover.exceptions;

public class DuplicateBookException extends Exception {
    public DuplicateBookException(String isbn) {
        super("Error: Book with ISBN " + isbn + " is already in your library");
    }
}
