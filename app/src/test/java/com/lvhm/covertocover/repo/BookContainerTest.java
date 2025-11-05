package com.lvhm.covertocover.repo;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;

import com.lvhm.covertocover.PrintToast;
import com.lvhm.covertocover.models.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class BookContainerTest {

    private BookContainer bookContainer;

    @Mock
    private PrintToast toast_printer;

    @Mock
    private Context mockContext;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookContainer = new BookContainer(toast_printer);
    }

    @Test
    public void testAddBook_Success() {
        // Arrange
        Book book = createTestBook("9781234567890", "Test Book");

        // Act
        bookContainer.addBook(mockContext, book);

        // Assert
        assertThat(bookContainer.getBooks()).hasSize(1);
        assertThat(bookContainer.getBook("9781234567890")).isEqualTo(book);
    }

    @Test
    public void testAddBook_DuplicateISBN() {
        // Arrange
        Book book1 = createTestBook("9781234567890", "Test Book 1");
        Book book2 = createTestBook("9781234567890", "Test Book 2");

        bookContainer.addBook(mockContext, book1);

        // Act
        bookContainer.addBook(mockContext, book2);

        // Assert
        assertThat(bookContainer.getBooks()).hasSize(1);
    }

    @Test
    public void testUpdateBook_Success() {
        // Arrange
        Book originalBook = createTestBook("9781234567890", "Original Title");
        bookContainer.addBook(mockContext, originalBook);

        Book updatedBook = createTestBook("9781234567890", "Updated Title");

        // Act
        bookContainer.updateBook(mockContext, updatedBook);

        // Assert
        assertThat(bookContainer.getBooks()).hasSize(1);
        assertThat(bookContainer.getBook("9781234567890").getName())
                .isEqualTo("Updated Title");
    }

    @Test
    public void testGetListReadBooks() {
        // Arrange
        Book readBook = createTestBook("9781111111111", "Read Book");
        readBook.setRead(true);

        Book unreadBook = createTestBook("9782222222222", "Unread Book");
        unreadBook.setRead(false);

        bookContainer.addBook(mockContext, readBook);
        bookContainer.addBook(mockContext, unreadBook);

        // Act
        ArrayList<Book> readBooks = bookContainer.getListReadBooks();

        // Assert
        assertThat(readBooks).hasSize(1);
        assertThat(readBooks.get(0).getName()).isEqualTo("Read Book");
    }

    @Test
    public void testGetListWishlistedBooks() {
        // Arrange
        Book wishlistedBook = createTestBook("9783333333333", "Wishlisted");
        wishlistedBook.setIsWishlisted(true);

        Book normalBook = createTestBook("9784444444444", "Normal");

        bookContainer.addBook(mockContext, wishlistedBook);
        bookContainer.addBook(mockContext, normalBook);

        // Act
        ArrayList<Book> wishlist = bookContainer.getListWishlistedBooks();

        // Assert
        assertThat(wishlist).hasSize(1);
        assertThat(wishlist.get(0).getIsWishlisted()).isTrue();
    }

    @Test
    public void testDeleteBook_Success() {
        // Arrange
        Book book = createTestBook("9785555555555", "To Delete");
        bookContainer.addBook(mockContext, book);

        // Act
        bookContainer.deleteBook(mockContext, book);

        // Assert
        assertThat(bookContainer.getBooks()).isEmpty();
        assertThat(bookContainer.getBook("9785555555555")).isNull();
    }

    // Helper method
    private Book createTestBook(String isbn, String title) {
        Book book = new Book();
        book.setISBN(isbn);
        book.setName(title);
        book.setAuthor(new ArrayList<>());
        book.setGenre(new ArrayList<>());
        return book;
    }
}