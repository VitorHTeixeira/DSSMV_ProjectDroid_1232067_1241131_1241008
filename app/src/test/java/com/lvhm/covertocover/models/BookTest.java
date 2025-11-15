package com.lvhm.covertocover.models;

import android.graphics.Bitmap;
import android.util.Base64;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

@RunWith(RobolectricTestRunner.class)
public class BookTest {
    @Mock
    private Book book_get;
    @Mock
    private Book book_set;
    @Mock
    private Bitmap mock_bitmap;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        book_get = new Book("1a2b3c4d-5e6f-7000-0000-000000000000",
                "9781408855652",
                "Harry Potter and the Philosopher's Stone",
                new ArrayList<>(Arrays.asList("J.K. Rowling")),
                1997,
                new ArrayList<>(Arrays.asList("Fantasy", "Young Adult")),
                false,
                223,
                "image_image_image",
                null,
                true,
                false
        );
        book_set = new Book();
    }

    @Test
    public void testGetUserUUID() {
        String expected_uuid = "1a2b3c4d-5e6f-7000-0000-000000000000";
        Assert.assertEquals(expected_uuid, book_get.getUserUUID());
    }
    @Test
    public void testSetUserUUID() {
        String expected_uuid = "1a2b3c4d-5e6f-7000-0000-000000000001";
        book_set.setUserUUID(expected_uuid);
        Assert.assertEquals(expected_uuid, book_set.getUserUUID());
    }
    @Test
    public void testGetISBN() {
        String expected_isbn = "9781408855652";
        Assert.assertEquals(expected_isbn, book_get.getISBN());
    }
    @Test
    public void testSetISBN() {
        String expected_isbn = "9781408855651";
        book_set.setISBN(expected_isbn);
        Assert.assertEquals(expected_isbn, book_set.getISBN());
    }
    @Test
    public void testGetTitle() {
        String expected_title = "Harry Potter and the Philosopher's Stone";
        Assert.assertEquals(expected_title, book_get.getName());
    }
    @Test
    public void testSetTitle() {
        String expected_title = "Harry Potter and the Chamber of Secrets";
        book_set.setName(expected_title);
        Assert.assertEquals(expected_title, book_set.getName());
    }
    @Test
    public void testGetAuthors() {
        ArrayList<String> expected_authors = new ArrayList<>(Arrays.asList("J.K. Rowling"));
        Assert.assertEquals(expected_authors, book_get.getAuthor());
    }
    @Test
    public void testSetAuthors() {
        ArrayList<String> expected_authors = new ArrayList<>(Arrays.asList("J.K. Rowling", "J.R.R. Tolkien"));
        book_set.setAuthor(expected_authors);
        Assert.assertEquals(expected_authors, book_set.getAuthor());
    }
    @Test
    public void testGetYear() {
        int expected_year = 1997;
        Assert.assertEquals(expected_year, book_get.getYear());
    }
    @Test
    public void testSetYear() {
        int expected_year = 1998;
        book_set.setYear(expected_year);
        Assert.assertEquals(expected_year, book_set.getYear());
    }
    @Test
    public void testGetGenres() {
        ArrayList<String> expected_genres = new ArrayList<>(Arrays.asList("Fantasy", "Young Adult"));
        Assert.assertEquals(expected_genres, book_get.getGenre());
    }
    @Test
    public void testSetGenres() {
        ArrayList<String> expected_genres = new ArrayList<>(Arrays.asList("Fantasy", "Young Adult", "Science Fiction"));
        book_set.setGenre(expected_genres);
        Assert.assertEquals(expected_genres, book_set.getGenre());
    }
    @Test
    public void testGetRead() {
        Assert.assertFalse(book_get.getRead());
    }
    @Test
    public void testSetRead() {
        book_set.setRead(true);
        Assert.assertTrue(book_set.getRead());
    }
    @Test
    public void testGetPages() {
        int expected_pages = 223;
        Assert.assertEquals(expected_pages, book_get.getPageCount());
    }
    @Test
    public void testSetPages() {
        int expected_pages = 224;
        book_set.setPageCount(expected_pages);
        Assert.assertEquals(expected_pages, book_set.getPageCount());
    }
    @Test
    public void testGetImage64() {
        String expected_image64 = "image_image_image";
        Assert.assertEquals(expected_image64, book_get.getCoverImageBase64());
    }
    @Test
    public void testSetImage64() {
        Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 80, baos);
        String expected_image64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        book_set.setCoverImageBase64(bitmap);
        Assert.assertEquals(expected_image64, book_set.getCoverImageBase64());
    }
    @Test
    public void testGetCoverImage() {
        Assert.assertNull(book_get.getCoverImage());
    }
    @Test
    public void testGetWishlisted() {
        Assert.assertTrue(book_get.getIsWishlisted());
    }
    @Test
    public void testSetWishlisted() {
        book_set.setIsWishlisted(false);
        Assert.assertFalse(book_set.getIsWishlisted());
    }
    @Test
    public void testGetOnGoing() {
        Assert.assertFalse(book_get.getOnGoing());
    }
    @Test
    public void testSetOnGoing() {
        book_set.setOnGoing(false);
        Assert.assertFalse(book_set.getOnGoing());
    }
}
