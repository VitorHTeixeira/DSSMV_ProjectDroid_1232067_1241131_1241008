package com.lvhm.covertocover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.OnGoingBook;
import com.lvhm.covertocover.models.ReadBook;

import java.util.ArrayList;
import java.util.Arrays;

public class ManualBookScreen extends Fragment {

    private EditText input_book_name;
    private EditText input_author;
    private EditText input_categories;
    private EditText input_release_year;
    private EditText input_page_number;
    private EditText input_isbn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manual_book, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String isbn = bundle.getString("isbn");
            EditText input_isbn = view.findViewById(R.id.input_isbn);
            input_isbn.setText(isbn);
        }

        input_book_name = view.findViewById(R.id.input_book_name);
        input_author = view.findViewById(R.id.input_author);
        input_categories = view.findViewById(R.id.input_categories);
        input_release_year = view.findViewById(R.id.input_release_year);
        input_page_number = view.findViewById(R.id.input_page_number);
        input_isbn = view.findViewById(R.id.input_isbn);
        Spinner status_spinner = view.findViewById(R.id.status_spinner);

        RelativeLayout cancel_button = view.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        RelativeLayout save_button = view.findViewById(R.id.save_button);
        save_button.setOnClickListener(v -> {
            String status = status_spinner.getSelectedItem().toString();
            switch (status) {
                case "Read":
                    ReadBook read_book = new ReadBook();
                    read_book.setName(input_book_name.getText().toString());
                    read_book.setAuthor((ArrayList<String>) Arrays.asList(input_author.getText().toString().split(", ")));
                    read_book.setGenre((ArrayList<String>) Arrays.asList(input_categories.getText().toString().split(", ")));
                    //read_book.setCoverImage(book_cover_bitmap );
                    read_book.setYear(Integer.parseInt(input_release_year.getText().toString().split("-")[0]));
                    read_book.setPageCount(Integer.parseInt(input_page_number.getText().toString()));
                    read_book.setISBN(input_isbn.getText().toString());

                    break;
                case "Reading":
                    OnGoingBook on_going_book = new OnGoingBook();
                    on_going_book.setName(input_book_name.getText().toString());
                    on_going_book.setAuthor((ArrayList<String>) Arrays.asList(input_author.getText().toString().split(", ")));
                    on_going_book.setGenre((ArrayList<String>) Arrays.asList(input_categories.getText().toString().split(", ")));
                    //on_going_book.setCoverImage(book_cover_bitmap );
                    on_going_book.setYear(Integer.parseInt(input_release_year.getText().toString().split("-")[0]));
                    on_going_book.setPageCount(Integer.parseInt(input_page_number.getText().toString()));
                    on_going_book.setISBN(input_isbn.getText().toString());

                    break;
                case "Want To Read":
                    Book wishlisted_book = new Book();
                    wishlisted_book.setName(input_book_name.getText().toString());
                    wishlisted_book.setAuthor((ArrayList<String>) Arrays.asList(input_author.getText().toString().split(", ")));
                    wishlisted_book.setGenre((ArrayList<String>) Arrays.asList(input_categories.getText().toString().split(", ")));
                    //wishlisted_book.setCoverImage(book_cover_bitmap );
                    wishlisted_book.setYear(Integer.parseInt(input_release_year.getText().toString().split("-")[0]));
                    wishlisted_book.setPageCount(Integer.parseInt(input_page_number.getText().toString()));
                    wishlisted_book.setISBN(input_isbn.getText().toString());

                    break;
            }
        });
            
        return view;
    }
}
