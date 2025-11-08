package com.lvhm.covertocover.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class ReviewTest {
    private Review review_get;
    private Review review_set;

    @Before
    public void setUp() {
        review_get = new Review(new Book(),
                                "1a2b3c4d-5e6f-7000-0000-000000000000",
                                "1a2b3c4d-5e6f-7000-0000-000000000011",
                                4.5,
                                "Muito bom!",
                                new Date(2025, 12, 8));
    }

    @Test
    public void testGetBook() {
        Book expected_book = new Book();
        Assert.assertEquals(expected_book.getUserUUID(), review_get.getBook().getUserUUID());
        Assert.assertEquals(expected_book.getISBN(), review_get.getBook().getISBN());
        Assert.assertEquals(expected_book.getName(), review_get.getBook().getName());
        Assert.assertEquals(expected_book.getAuthor(), review_get.getBook().getAuthor());
        Assert.assertEquals(expected_book.getYear(), review_get.getBook().getYear());
        Assert.assertEquals(expected_book.getGenre(), review_get.getBook().getGenre());
        Assert.assertEquals(expected_book.getRead(), review_get.getBook().getRead());
        Assert.assertEquals(expected_book.getPageCount(), review_get.getBook().getPageCount());
        Assert.assertEquals(expected_book.getCoverImageBase64(), review_get.getBook().getCoverImageBase64());
        Assert.assertEquals(expected_book.getCoverImage(), review_get.getBook().getCoverImage());
        Assert.assertEquals(expected_book.getIsWishlisted(), review_get.getBook().getIsWishlisted());
        Assert.assertEquals(expected_book.getOnGoing(), review_get.getBook().getOnGoing());
    }
    @Test
    public void testSetBook() {
        review_set = new Review();
        Book expected_book = new Book();
        review_set.setBook(expected_book);
        Assert.assertEquals(expected_book, review_set.getBook());
    }
    @Test
    public void testGetUserUUID() {
        String expected_user_uuid = "1a2b3c4d-5e6f-7000-0000-000000000000";
        Assert.assertEquals(expected_user_uuid, review_get.getUserUUID());
    }
    @Test
    public void testSetUserUUID() {
        review_set = new Review();
        String expected_user_uuid = "1a2b3c4d-5e6f-7000-0000-000000000000";
        review_set.setUserUUID(expected_user_uuid);
        Assert.assertEquals(expected_user_uuid, review_set.getUserUUID());
    }
    @Test
    public void testGetUUID() {
        String expected_uuid = "1a2b3c4d-5e6f-7000-0000-000000000011";
        Assert.assertEquals(expected_uuid, review_get.getUUID());
    }
    @Test
    public void testSetUUID() {
        review_set = new Review();
        String expected_uuid = "1a2b3c4d-5e6f-7000-0000-000000000011";
        review_set.setUUID(expected_uuid);
        Assert.assertEquals(expected_uuid, review_set.getUUID());
    }
    @Test
    public void testGetRating() {
        double expected_rating = 4.5;
        Assert.assertEquals(expected_rating, review_get.getRating(), 0.001);
    }
    @Test
    public void testSetRating() {
        review_set = new Review();
        double expected_rating = 4.5;
        review_set.setRating(expected_rating);
        Assert.assertEquals(expected_rating, review_set.getRating(), 0.001);
    }
    @Test
    public void testGetReviewText() {
        String expected_text = "Muito bom!";
        Assert.assertEquals(expected_text, review_get.getReviewText());
    }
    @Test
    public void testSetReviewText() {
        review_set = new Review();
        String expected_text = "Muito bom!";
        review_set.setReviewText(expected_text);
        Assert.assertEquals(expected_text, review_set.getReviewText());
    }
    @Test
    public void testGetDate() {
        Date expected_date = new Date(2025, 12, 8);
        Assert.assertEquals(expected_date, review_get.getDate());
    }
    @Test
    public void testSetDate() {
        review_set = new Review();
        Date expected_date = new Date(2025, 12, 8);
        review_set.setDate(expected_date);
        Assert.assertEquals(expected_date, review_set.getDate());
    }
}
