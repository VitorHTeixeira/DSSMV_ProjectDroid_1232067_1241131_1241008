package com.lvhm.covertocover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.apache.commons.text.WordUtils;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookScreen extends Fragment {
    private Bundle book_details = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book, container, false);
        Bundle book_info = getArguments();
        if(book_info != null) {
            book_details = book_info.getBundle("response");
            fillBookInfo(view, book_info);
        }

        Spinner statusSpinner = view.findViewById(R.id.status_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.status_options, R.layout.spinner_item_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        RelativeLayout add_review = view.findViewById(R.id.review_button);
        add_review.setOnClickListener(v -> {
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

        Button tempButton = view.findViewById(R.id.temp_test_button);
        tempButton.setOnClickListener(v -> {
            Fragment fragment = new BookHistoryReviewOverlay();
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    public void fillBookInfo(View view, Bundle book_info) {
        if(book_info.getString("title") != null) {
            TextView label_book_name = view.findViewById(R.id.label_book_name);
            String title = book_info.getString("title");
            String subtitle = book_info.getString("subtitle");
            String fullTitle = title;
            if (subtitle != null && !subtitle.isEmpty()) {
                fullTitle += ": " + subtitle;
            }
            String book_name = WordUtils.capitalize("\"" + fullTitle + "\"");
            label_book_name.append(book_name);
        }
        if(book_info.getStringArrayList("categories") != null) {
            TextView label_book_categories = view.findViewById(R.id.label_categories);
            StringBuilder categories = new StringBuilder();
            for(String category : book_info.getStringArrayList("categories")) {
                if(categories.length() > 1) {
                    categories.append(WordUtils.capitalize(category)).append(", ");
                }
                else {
                    categories.append(WordUtils.capitalize(category));
                }
            }
            label_book_categories.append(categories);
        }
        if(book_info.getString("publishedDate") != null) {
            TextView label_book_date = view.findViewById(R.id.label_release_year);
            if (book_info.getString("publishedDate").matches("\\d{4}")) {
                label_book_date.append(book_info.getString("publishedDate"));
            }
            else {
                label_book_date.append("" + LocalDate.parse(book_info.getString("publishedDate")).getYear());
            }
        }
        if(book_info.getStringArrayList("authors") != null) {
            TextView label_book_author = view.findViewById(R.id.label_author);
            StringBuilder authors = new StringBuilder();
            for (String author : book_info.getStringArrayList("authors")) {
                if (authors.length() > 1) {
                    authors.append(WordUtils.capitalize(author)).append(", ");
                } else {
                    authors.append(WordUtils.capitalize(author));
                }
            }
            label_book_author.append(" " + authors);
        }
        if(book_info.getString("pageCount") != null) {
            TextView label_book_pages = view.findViewById(R.id.label_page_number);
            label_book_pages.append(book_info.getString("pageCount"));
        }
        TextView label_book_isbn = view.findViewById(R.id.label_isbn_number);
        label_book_isbn.append(book_info.getString("isbn"));
        if(book_info.getString("thumbnail") != null) {
            getBookCoverImage(view, book_info.getString("thumbnail"));
        }
    }

    public void getBookCoverImage(View view, String image_url) {
        image_url = image_url.replace("http://", "https://");
        ImageView book_cover = view.findViewById(R.id.book_cover_image);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        final Bitmap[] image_cover = {null};

        String finalImage_url = image_url;
        executor.execute(() -> {
            try {
                InputStream in = new URL(finalImage_url).openStream();
                image_cover[0] = BitmapFactory.decodeStream(in);
                handler.post(() -> book_cover.setImageBitmap(image_cover[0]));
                FrameLayout book_cover_frame = view.findViewById(R.id.book_cover);
                book_cover_frame.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));

            } catch (Exception e) {
                String message = "Error getting book cover image. Error: " + e.getMessage();
                System.out.println(message);
                NotificationCentral.showNotification(requireContext(), message);
            }
        });
    }
}
