package com.lvhm.covertocover.service;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lvhm.covertocover.R;
import com.lvhm.covertocover.repo.UserTokenContainer;

public class ProfileSettingsScreen extends Fragment {

    private static final String PREFERENCES_FILE = "CTCPreferences";
    private static final String KEY_USERNAME = "profile_username";
    private static final String KEY_PASSWORD = "profile_password";
    private static final String KEY_EMAIL = "profile_email";
    private static final String KEY_PHONE = "profile_phone";
    private static final String KEY_ADDRESS = "profile_address";
    private static final String KEY_DATE_FORMAT = "profile_date_format";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        SharedPreferences shared_preferences = requireContext().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        UserTokenContainer token_container = UserTokenContainer.getInstance(requireContext());

        //EditText edit_text_username = view.findViewById(R.id.value_your_username);
        EditText edit_text_email = view.findViewById(R.id.value_user_email);
        EditText edit_text_phone_number = view.findViewById(R.id.value_user_phone);
        EditText edit_text_address = view.findViewById(R.id.value_user_address);
        EditText edit_text_token = view.findViewById(R.id.value_user_token);
        Spinner spinner_date_format = view.findViewById(R.id.spinner_date_format);
        //ImageView save_username_button = view.findViewById(R.id.edit_username_image_icon);
        ImageView save_email_button = view.findViewById(R.id.edit_email_image_icon);
        ImageView save_phone_button = view.findViewById(R.id.edit_phone_image_icon);
        ImageView save_address_button = view.findViewById(R.id.edit_address_image_icon);
        ImageView copy_token_button = view.findViewById(R.id.copy_token_image_icon); // Assumed ID

        // Spinner
        setup_spinner(spinner_date_format, shared_preferences);

        // Load saved data
        //edit_text_username.setText(shared_preferences.getString(KEY_USERNAME, ""));
        edit_text_email.setText(shared_preferences.getString(KEY_EMAIL, ""));
        edit_text_phone_number.setText(shared_preferences.getString(KEY_PHONE, ""));
        edit_text_address.setText(shared_preferences.getString(KEY_ADDRESS, ""));
        edit_text_token.setText(token_container.getToken());

        // Set initial states
        //set_read_only(edit_text_username);
        set_read_only(edit_text_email);
        set_read_only(edit_text_phone_number);
        set_read_only(edit_text_address);
        set_read_only(edit_text_token);

        // Setup edit/save logic
        //setup_click_to_edit(edit_text_username, save_username_button, shared_preferences, KEY_USERNAME, "Username saved");
        setup_click_to_edit(edit_text_email, save_email_button, shared_preferences, KEY_EMAIL, "Email saved");
        setup_click_to_edit(edit_text_phone_number, save_phone_button, shared_preferences, KEY_PHONE, "Phone Number saved");
        setup_click_to_edit(edit_text_address, save_address_button, shared_preferences, KEY_ADDRESS, "Address saved");

        copy_token_button.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("User Token", token_container.getToken());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(requireContext(), "Token copied to clipboard!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void setup_click_to_edit(final EditText editText, ImageView saveButton, final SharedPreferences prefs, final String key, final String toastMessage) {
        editText.setOnClickListener(v -> {
            if (!editText.isFocusable()) {
                set_editable(editText);
            }
        });
        saveButton.setOnClickListener(v -> {
            commit_field(prefs, key, editText, toastMessage);
        });
    }

    private void setup_spinner(Spinner spinner, final SharedPreferences prefs) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.date_formats, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        String saved_date_format_text = prefs.getString(KEY_DATE_FORMAT, null);
        int positionToSet = 0;
        if (saved_date_format_text != null) {
            int foundPosition = spinnerAdapter.getPosition(saved_date_format_text);
            if (foundPosition >= 0) {
                positionToSet = foundPosition;
            }
        }
        spinner.setSelection(positionToSet);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFormat = parent.getItemAtPosition(position).toString();
                String currentlySavedFormat = prefs.getString(KEY_DATE_FORMAT, "");

                if (!selectedFormat.equals(currentlySavedFormat)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(KEY_DATE_FORMAT, selectedFormat);
                    editor.apply();
                    Toast.makeText(requireContext(), "Date format saved!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void set_read_only(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setCursorVisible(false);
    }

    private void set_editable(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setCursorVisible(true);
        editText.requestFocus();
        editText.setSelection(editText.getText().length());

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void commit_field(SharedPreferences prefs, String key, EditText editText, String toastMessage) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, editText.getText().toString());
        editor.apply();
        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show();

        set_read_only(editText);
        editText.clearFocus();
    }
}
