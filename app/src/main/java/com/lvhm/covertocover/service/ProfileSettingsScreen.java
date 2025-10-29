package com.lvhm.covertocover.service;

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

public class ProfileSettingsScreen extends Fragment {

    private static final String PREFERENCES_FILE = "CTCPreferences";
    private static final String KEY_USERNAME = "profile_username";
    private static final String KEY_PASSWORD = "profile_password";
    private static final String KEY_EMAIL = "profile_email";
    private static final String KEY_PHONE = "profile_phone";
    private static final String KEY_ADDRESS = "profile_address";
    private static final String KEY_DATE_FORMAT = "profile_date_format";

    private void setReadOnly(EditText edit_text) {
        edit_text.setFocusable(false);
        edit_text.setFocusableInTouchMode(false);
        edit_text.setCursorVisible(false);
    }

    private void setEditable(EditText edit_text) {
        edit_text.setFocusable(true);
        edit_text.setFocusableInTouchMode(true);
        edit_text.setCursorVisible(true);
        edit_text.requestFocus();
        edit_text.setSelection(edit_text.getText().length());

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit_text, InputMethodManager.SHOW_IMPLICIT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        SharedPreferences shared_preferences = requireContext().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        EditText edit_text_username = view.findViewById(R.id.value_your_username);
        EditText edit_text_password = view.findViewById(R.id.value_user_password);
        EditText edit_text_email = view.findViewById(R.id.value_user_email);
        EditText edit_text_phone_number = view.findViewById(R.id.value_user_phone);
        EditText edit_text_address = view.findViewById(R.id.value_user_address);
        Spinner spinner_date_format = view.findViewById(R.id.spinner_date_format);
        ImageView save_username_button = view.findViewById(R.id.edit_username_image_icon);
        ImageView save_password_button = view.findViewById(R.id.edit_password_image_icon);
        ImageView save_email_button = view.findViewById(R.id.edit_email_image_icon);
        ImageView save_phone_button = view.findViewById(R.id.edit_phone_image_icon);
        ImageView save_address_button = view.findViewById(R.id.edit_address_image_icon);

        edit_text_username.setText(shared_preferences.getString(KEY_USERNAME, ""));
        setReadOnly(edit_text_username);

        edit_text_password.setText(shared_preferences.getString(KEY_PASSWORD, ""));
        setReadOnly(edit_text_password);

        edit_text_email.setText(shared_preferences.getString(KEY_EMAIL, ""));
        setReadOnly(edit_text_email);

        edit_text_phone_number.setText(shared_preferences.getString(KEY_PHONE, ""));
        setReadOnly(edit_text_phone_number);

        edit_text_address.setText(shared_preferences.getString(KEY_ADDRESS, ""));
        setReadOnly(edit_text_address);

        // Username
        edit_text_username.setOnClickListener(v -> setEditable(edit_text_username));
        save_username_button.setOnClickListener(v -> saveField(shared_preferences, KEY_USERNAME, edit_text_username, "Username saved"));

        // Password
        edit_text_password.setOnClickListener(v -> setEditable(edit_text_password));
        save_password_button.setOnClickListener(v -> saveField(shared_preferences, KEY_PASSWORD, edit_text_password, "Password saved"));

        // Email
        edit_text_email.setOnClickListener(v -> setEditable(edit_text_email));
        save_email_button.setOnClickListener(v -> saveField(shared_preferences, KEY_EMAIL, edit_text_email, "Email saved"));

        // Phone Number
        edit_text_phone_number.setOnClickListener(v -> setEditable(edit_text_phone_number));
        save_phone_button.setOnClickListener(v -> saveField(shared_preferences, KEY_PHONE, edit_text_phone_number, "Phone Number saved"));

        // Address
        edit_text_address.setOnClickListener(v -> setEditable(edit_text_address));
        save_address_button.setOnClickListener(v -> {
            saveField(shared_preferences, KEY_ADDRESS, edit_text_address, "Address saved");
        });

        // Spinner
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.date_formats, R.layout.spinner_item_text);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_date_format.setAdapter(spinner_adapter);

        int saved_date_format = shared_preferences.getInt(KEY_DATE_FORMAT, 0);
        spinner_date_format.setSelection(saved_date_format);
        spinner_date_format.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                int selected_format = parent.getSelectedItemPosition();
                int currently_saved_format = shared_preferences.getInt(KEY_DATE_FORMAT, 0);
                if (currently_saved_format != selected_format) {
                    SharedPreferences.Editor editor = shared_preferences.edit();
                    editor.putInt(KEY_DATE_FORMAT, selected_format);
                    editor.apply();
                    Toast.makeText(requireContext(), "Date format saved!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        return view;
    }


    private void saveField(SharedPreferences preferences, String key, EditText edit_text, String toast_message) {

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_text.getWindowToken(), 0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, edit_text.getText().toString());
        editor.apply();
        Toast.makeText(requireContext(), toast_message, Toast.LENGTH_SHORT).show();

        setReadOnly(edit_text);
        edit_text.clearFocus();
    }
}
