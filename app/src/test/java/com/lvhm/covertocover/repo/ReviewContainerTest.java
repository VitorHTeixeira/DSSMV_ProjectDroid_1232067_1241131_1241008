package com.lvhm.covertocover.repo;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;

import com.lvhm.covertocover.PrintToast;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


@RunWith(MockitoJUnitRunner.class)
public class ReviewContainerTest {

    private ReviewContainer review_container;

    @Mock
    private PrintToast toast_printer;

    @Mock
    private Context mockContext;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        review_container = new ReviewContainer();
    }

    @Test
    public void testAddReviewSuccess() {
        // Arrange
        Book book = createTestBook("9781234567890", "Test Book");
        Review review = createTestReview(book, 4.5, "O professor de DSSMV é o melhor professor do ISEP", new Date());

        // Act
        review_container.addReview(mockContext, review);

        // Assert
        assertThat(review_container.getReviews()).hasSize(1);
        assertThat(review_container.getReviews().get(0)).isEqualTo(review);
    }

    @Test
    public void testAddMultipleReviewsSuccess() {
        // Arrange
        Book book_1 = createTestBook("9781111111111", "Book 1");
        Book book_2 = createTestBook("9782222222222", "Book 2");

        Review review_1 = createTestReview(book_1, 5.0, "DSSMV é uma obra de arte", new Date());
        Review review_2 = createTestReview(book_2, 3.5, "Java é a melhor lingua de programação", new Date());
        Review review_3 = createTestReview(book_1, 4.0, "React Native também", new Date());

        // Act
        review_container.addReview(mockContext, review_1);
        review_container.addReview(mockContext, review_2);
        review_container.addReview(mockContext, review_3);

        // Assert
        assertThat(review_container.getReviews()).hasSize(3);
    }


    @Test
    public void testUpdateReviewSuccess() {
        // Arrange
        Book book = createTestBook("9784444444444", "Update Test");
        Review original_review = createTestReview(book, 3.0, "Texto original", new Date());
        review_container.addReview(mockContext, original_review);

        original_review.setRating(4.5);
        original_review.setReviewText("Texto atualizado após releitura");

        // Act
        review_container.updateReview(original_review);

        // Assert
        assertThat(review_container.getReviews()).hasSize(1);
        assertThat(review_container.getReviews().get(0).getRating()).isEqualTo(4.5);
        assertThat(review_container.getReviews().get(0).getReviewText())
                .isEqualTo("Texto atualizado após releitura");
    }

    @Test
    public void testGetAverageRatingThisYearCorrect() {
        // Arrange
        Book book = createTestBook("9780000000001", "Year Test");
        Date current_year_date = getCurrentYearDate();

        review_container.addReview(mockContext, createTestReview(book, 5.0, "Review 1", current_year_date));
        review_container.addReview(mockContext, createTestReview(book, 4.0, "Review 2", current_year_date));
        review_container.addReview(mockContext, createTestReview(book, 3.0, "Review 3", current_year_date));

        // Act
        double average = review_container.getAverageRatingThisYear();

        // Assert
        // (5.0 + 4.0 + 3.0) / 3 = 4.0
        assertThat(average).isEqualTo(4.0);
    }

    @Test
    public void testGetTotalReviewsThisYearTotal() {
        // Arrange
        Book book = createTestBook("9780000000003", "Count Test");
        Date current_year_date = getCurrentYearDate();
        Date previous_year_date = getPreviousYearDate();

        review_container.addReview(mockContext, createTestReview(book, 4.0, "Review 1", current_year_date));
        review_container.addReview(mockContext, createTestReview(book, 3.5, "Review 2", current_year_date));
        review_container.addReview(mockContext, createTestReview(book, 5.0, "Old review", previous_year_date));

        // Act
        int total = review_container.getTotalReviewsThisYear();

        // Assert
        assertThat(total).isEqualTo(2);
    }

    @Test
    public void testGetMostUsedRatingThisYearRating() {
        // Arrange
        Book book = createTestBook("9780000000004", "Rating Frequency Test");
        Date current_year_date = getCurrentYearDate();

        review_container.addReview(mockContext, createTestReview(book, 4.0, "Review 1", current_year_date));
        review_container.addReview(mockContext, createTestReview(book, 4.0, "Review 2", current_year_date));
        review_container.addReview(mockContext, createTestReview(book, 4.0, "Review 3", current_year_date));
        review_container.addReview(mockContext, createTestReview(book, 5.0, "Review 4", current_year_date));
        review_container.addReview(mockContext, createTestReview(book, 3.0, "Review 5", current_year_date));

        // Act
        double most_used = review_container.getMostUsedRatingThisYear();

        // Assert
        assertThat(most_used).isEqualTo(4.0);
    }

    @Test
    public void testGetBestMonthThisYearMonth() {
        // Arrange
        Book book = createTestBook("9780000000005", "Month Test");

        // Criar reviews para diferentes meses do ano corrente
        Date january_date = getDateForCurrentYearMonth(Calendar.JANUARY);
        Date february_date = getDateForCurrentYearMonth(Calendar.FEBRUARY);

        review_container.addReview(mockContext, createTestReview(book, 4.0, "Jan 1", january_date));
        review_container.addReview(mockContext, createTestReview(book, 4.0, "Jan 2", january_date));
        review_container.addReview(mockContext, createTestReview(book, 4.0, "Jan 3", january_date));
        review_container.addReview(mockContext, createTestReview(book, 5.0, "Feb 1", february_date));

        // Act
        String best_month = review_container.getBestMonthThisYear();

        // Assert
        assertThat(best_month).isEqualTo("January");
    }
    @Test
    public void testGetBestMonthThisYearEmpty() {
        // Act
        String best_month = review_container.getBestMonthThisYear();

        // Assert
        assertThat(best_month).isEqualTo("?");
    }

    // Helper methods
    private Book createTestBook(String isbn, String title) {
        Book book = new Book();
        book.setISBN(isbn);
        book.setName(title);
        book.setAuthor(new ArrayList<>());
        book.setGenre(new ArrayList<>());
        return book;
    }

    private Review createTestReview(Book book, double rating, String review_text, Date date) {
        return new Review(book, "test-user-uuid", rating, review_text, date);
    }

    private Date getCurrentYearDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        return cal.getTime();
    }

    private Date getPreviousYearDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        return cal.getTime();
    }

    private Date getDateForCurrentYearMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
