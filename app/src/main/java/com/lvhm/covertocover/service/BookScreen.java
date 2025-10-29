package com.lvhm.covertocover.service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lvhm.covertocover.NotificationCentral;
import com.lvhm.covertocover.R;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.OnGoingBook;
import com.lvhm.covertocover.models.ReadBook;
import com.lvhm.covertocover.models.SharedBookViewModel;
import com.lvhm.covertocover.repo.BookContainer;

import org.apache.commons.text.WordUtils;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookScreen extends Fragment {
    private Bundle book_details = new Bundle();
    private Bitmap book_cover_bitmap = null;
    private String book_title;
    private ArrayList<String> book_authors;
    private ArrayList<String> book_categories;
    private int book_year;
    private int book_pages;
    private String book_isbn;
    private TextView label_book_name;
    private TextView label_book_categories;
    private TextView label_book_date;
    private TextView label_book_author;
    private TextView label_book_pages;
    private TextView label_book_isbn;
    private Spinner status_spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book, container, false);
        Bundle book_info = getArguments();

        label_book_name = view.findViewById(R.id.label_book_name);
        label_book_categories = view.findViewById(R.id.label_categories);
        label_book_date = view.findViewById(R.id.label_release_year);
        label_book_author = view.findViewById(R.id.label_author);
        label_book_pages = view.findViewById(R.id.label_page_number);
        label_book_isbn = view.findViewById(R.id.label_isbn_number);

        if(book_info != null) {
            book_details = book_info.getBundle("response");
            fillBookInfo(view, book_info);
        }

        status_spinner = view.findViewById(R.id.status_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.status_options, R.layout.spinner_item_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_spinner.setAdapter(adapter);

        RelativeLayout add_review = view.findViewById(R.id.review_button);
        add_review.setOnClickListener(v -> {
            SharedBookViewModel view_model = new ViewModelProvider(requireActivity()).get(SharedBookViewModel.class);
            view_model.selectBook(getBookForReview());
            Fragment fragment = new BookReviewOverlay();
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        
        RelativeLayout book_details_layout = view.findViewById(R.id.book_details_layout);
        book_details_layout.setOnClickListener(v -> {
            Fragment book_details_fragment = new BookDetails();
            book_details_fragment.setArguments(book_details);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, book_details_fragment)
                    .addToBackStack(null)
                    .commit();
        });

        RelativeLayout share_button_layout = view.findViewById(R.id.share_book_layout);
        share_button_layout.setOnClickListener(v -> {
            Intent share_intent = new Intent();
            share_intent.setAction(Intent.ACTION_SEND);
            share_intent.putExtra(Intent.EXTRA_TEXT, book_details.toString());
            share_intent.setType("text/plain");
            startActivity(Intent.createChooser(share_intent, "Share Book"));
        });


        ImageView book_cover = view.findViewById(R.id.book_cover_image);
        book_cover.setOnClickListener(v -> {
            Bundle book_cover_info = new Bundle();
            if(book_info.getString("thumbnail") != null) {
                book_cover_info.putString("thumbnail", book_info.getString("thumbnail"));
            }
            if (book_cover_bitmap != null) {
                book_cover_info.putParcelable("thumbnail_bitmap", book_cover_bitmap);
            }
            Fragment book_cover_fragment = new BookCoverImageOverlay();
            book_cover_fragment.setArguments(book_cover_info);
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, book_cover_fragment)
                    .addToBackStack(null)
                    .commit();
        });

        RelativeLayout cancel_button = view.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        RelativeLayout save_button = view.findViewById(R.id.save_button);
        save_button.setOnClickListener(v -> {
            saveBook();
            getParentFragmentManager().popBackStack();
        });

        return view;
    }

    public void fillBookInfo(View view, Bundle book_info) {
        if(book_info.getString("title") != null) {
            String title = book_info.getString("title");
            String subtitle = book_info.getString("subtitle");
            String fullTitle = title;
            if (subtitle != null && !subtitle.isEmpty()) {
                fullTitle += ": " + subtitle;
            }
            String book_name = WordUtils.capitalize(fullTitle);
            book_title = book_name;
            label_book_name.append("\"" + book_name + "\"");
        }
        if(book_info.getStringArrayList("categories") != null) {
            StringBuilder categories = new StringBuilder();
            book_categories = book_info.getStringArrayList("categories");
            ArrayList<String> book_categories_array = book_info.getStringArrayList("categories");
            for(String category : book_categories_array) {
                if(book_categories_array.size() > 1) {
                    categories.append(WordUtils.capitalize(category)).append(", ");
                }
                else {
                    categories.append(WordUtils.capitalize(category));
                }
            }
            label_book_categories.append(categories);
        }
        if(book_info.getString("publishedDate") != null) {
            if (book_info.getString("publishedDate").matches("\\d{4}")) {
                book_year = Integer.parseInt(book_info.getString("publishedDate"));
                label_book_date.append(book_info.getString("publishedDate"));
            }
            else {
                int year = LocalDate.parse(book_info.getString("publishedDate")).getYear();
                book_year = year;
                label_book_date.append("" + year);
            }
        }
        if(book_info.getStringArrayList("authors") != null) {
            StringBuilder authors = new StringBuilder();
            book_authors = book_info.getStringArrayList("authors");
            ArrayList<String> book_authors_array = book_info.getStringArrayList("authors");
            for (String author : book_authors_array) {
                if (book_authors_array.size() > 1) {
                    authors.append(WordUtils.capitalize(author)).append(", ");
                } else {
                    authors.append(WordUtils.capitalize(author));
                }
            }
            label_book_author.append(" " + authors);
        }
        if(book_info.getString("pageCount") != null) {
            book_pages = Integer.parseInt(book_info.getString("pageCount"));
            label_book_pages.append(book_info.getString("pageCount"));
        }
        book_isbn = book_info.getString("isbn");
        label_book_isbn.append(book_info.getString("isbn"));
        if(book_info.getString("thumbnail") != null) {
            getBookCoverImage(view, book_info.getString("thumbnail"));
        }
    }

    public void getBookCoverImage(View view, String image_url) {
        image_url = image_url.replace("http://", "https://");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        ImageView book_cover = view.findViewById(R.id.book_cover_image);
        String finalImage_url = image_url;
        executor.execute(() -> {
            try {
                InputStream in = new URL(finalImage_url).openStream();
                book_cover_bitmap = BitmapFactory.decodeStream(in);
                handler.post(() -> {
                    book_cover.setImageBitmap(book_cover_bitmap);
                    FrameLayout book_cover_frame = view.findViewById(R.id.book_cover);
                    book_cover_frame.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
                });

            } catch (Exception e) {
                String message = "Error getting book cover image. Error: " + e.getMessage();
                System.out.println(message);
                handler.post(() -> NotificationCentral.showNotification(requireContext(), message));
            }
        });
    }

    public Book getBookForReview() {
        BookContainer book_container = BookContainer.getInstance();
        return book_container.getBook(book_isbn);
    }

    public void saveBook() {
        BookContainer book_container = BookContainer.getInstance();
        String status = status_spinner.getSelectedItem().toString();
        switch (status) {
            case "Read":
                Book read_book = new Book();
                read_book.setName(book_title);
                read_book.setAuthor(book_authors);
                read_book.setGenre(book_categories);
                read_book.setCoverImage(book_cover_bitmap);
                read_book.setYear(book_year);
                read_book.setPageCount(book_pages);
                read_book.setISBN(book_isbn);
                read_book.setRead(true);
                read_book.setIsWishlisted(false);
                read_book.setOnGoing(false);
                book_container.addBook(requireContext(), read_book);
                break;
            case "Reading":
                Book on_going_book = new Book();
                on_going_book.setName(book_title);
                on_going_book.setAuthor(book_authors);
                on_going_book.setGenre(book_categories);
                on_going_book.setCoverImage(book_cover_bitmap);
                on_going_book.setYear(book_year);
                on_going_book.setPageCount(book_pages);
                on_going_book.setISBN(book_isbn);
                on_going_book.setIsWishlisted(false);
                on_going_book.setOnGoing(true);
                book_container.addBook(requireContext(), on_going_book);
                break;
            case "Want To Read":
                Book wishlisted_book = new Book();
                wishlisted_book.setName(book_title);
                wishlisted_book.setAuthor(book_authors);
                wishlisted_book.setGenre(book_categories);
                wishlisted_book.setCoverImage(book_cover_bitmap);
                wishlisted_book.setYear(book_year);
                wishlisted_book.setPageCount(book_pages);
                wishlisted_book.setISBN(book_isbn);
                wishlisted_book.setRead(false);
                wishlisted_book.setIsWishlisted(true);
                wishlisted_book.setOnGoing(false);
                book_container.addBook(requireContext(), wishlisted_book);
                break;
        }
    }
}
