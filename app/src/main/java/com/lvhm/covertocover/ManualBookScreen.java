package com.lvhm.covertocover;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.repo.BookContainer;

import java.util.ArrayList;
import java.util.Arrays;

public class ManualBookScreen extends Fragment {

    private EditText input_book_name;
    private EditText input_author;
    private EditText input_categories;
    private EditText input_release_year;
    private EditText input_page_number;
    private EditText input_isbn;
    private Spinner status_spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manual_book, container, false);

        input_book_name = view.findViewById(R.id.input_book_name);
        input_author = view.findViewById(R.id.input_author);
        input_categories = view.findViewById(R.id.input_categories);
        input_release_year = view.findViewById(R.id.input_release_year);
        input_page_number = view.findViewById(R.id.input_page_number);
        input_isbn = view.findViewById(R.id.input_isbn);
        status_spinner = view.findViewById(R.id.status_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.status_options, R.layout.spinner_item_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_spinner.setAdapter(adapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String isbn = bundle.getString("isbn");
            input_isbn.setText(isbn);
            input_isbn.setEnabled(false);
        }

        RelativeLayout cancel_button = view.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        RelativeLayout save_button = view.findViewById(R.id.save_button);
        save_button.setOnClickListener(v -> {

            hideKeyboard(v);
            BookContainer book_container = BookContainer.getInstance();

            String book_name = input_book_name.getText().toString();
            String author = input_author.getText().toString();
            String categories = input_categories.getText().toString();
            String release_year = input_release_year.getText().toString();
            String page_number = input_page_number.getText().toString();
            String isbn = input_isbn.getText().toString();

            if (book_name.isEmpty()) {
                Toast.makeText(requireContext(), "Book Name is required.", Toast.LENGTH_SHORT).show();
                input_book_name.requestFocus();
                return;
            }
            if (author.isEmpty()) {
                Toast.makeText(requireContext(), "Author is required.", Toast.LENGTH_SHORT).show();
                input_author.requestFocus();
                return;
            }

            ArrayList<String> authorsList;
            ArrayList<String> categoriesList;
            int year = 0;
            int pageCount = 0;

            try {
                authorsList = author.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(author.split(",\\s*")));
                categoriesList = categories.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(categories.split(",\\s*")));

                if (!release_year.isEmpty()) {
                    year = Integer.parseInt(release_year.split("-")[0].trim());
                }
                if (!page_number.isEmpty()) {
                    pageCount = Integer.parseInt(page_number);
                }

            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid format for Year or Page Count.", Toast.LENGTH_SHORT).show();
                return;
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Error processing fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            String status = status_spinner.getSelectedItem().toString();

            Book manual_book = new Book();
            manual_book.setName(book_name);
            manual_book.setAuthor(authorsList);
            manual_book.setGenre(categoriesList);
            manual_book.setYear(year);
            manual_book.setPageCount(pageCount);
            manual_book.setISBN(isbn);

            switch (status) {
                case "Read":
                    manual_book.setRead(true);
                    break;
                case "Reading":
                    manual_book.setOnGoing(true);
                    break;
                case "Want To Read":
                    manual_book.setIsWishlisted(true);
                    break;
            }
            
            book_container.addBook(manual_book); 
            getParentFragmentManager().popBackStack();
            Toast.makeText(requireContext(), "Book added successfully.", Toast.LENGTH_SHORT).show();
        });
            
        return view;
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
