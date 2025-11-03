package com.lvhm.covertocover.service;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.lvhm.covertocover.R;
import com.lvhm.covertocover.repo.UserTokenContainer;

public class LoginTokenScreen extends Fragment {

    private UserTokenContainer token_container;
    private EditText input_token;
    private TextInputLayout input_token_layout;
    private CardView button_generate_token;
    private CardView button_use_token;
    private TextView text_generated_token;
    private TextView text_description;
    private ProgressBar progress_bar;
    private String generated_token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        token_container = UserTokenContainer.getInstance(requireContext());

        if (token_container.hasToken()) {
            proceedToMainScreen();
            return new FrameLayout(requireContext());
        }

        View view = inflater.inflate(R.layout.fragment_logintoken, container, false);

        input_token_layout = view.findViewById(R.id.input_token_layout);
        input_token = view.findViewById(R.id.input_token);
        button_generate_token = view.findViewById(R.id.button_generate_token);
        button_use_token = view.findViewById(R.id.button_use_token);
        text_generated_token = view.findViewById(R.id.text_generated_token);
        text_description = view.findViewById(R.id.text_descrition);
        progress_bar = view.findViewById(R.id.progress_bar);

        text_generated_token.setVisibility(View.GONE);

        button_generate_token.setOnClickListener(v -> generateToken());
        button_use_token.setOnClickListener(v -> useExistingToken());

        input_token.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate_token_input(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        TypedValue typed_value = new TypedValue();
        requireContext().getTheme().resolveAttribute(android.R.attr.textColorPrimary, typed_value, true);
        //text_description.setTextColor(Color.);

        TextView generate_button_text = findTextViewInChildren(button_generate_token);
        if (generate_button_text != null) {
            generate_button_text.setTextColor(Color.WHITE);
        }

        TextView use_button_text = findTextViewInChildren(button_use_token);
        if (use_button_text != null) {
            use_button_text.setTextColor(Color.WHITE);
        }

        return view;
    }


    private void generateToken() {
        setLoadingState(true);

        generated_token = token_container.generateToken();

        text_generated_token.setText("Your token is:\n\n" + generated_token +
                "\n\nSave your token!\n" +
                "It will be necessary to access your data in other mobile devices.");
        text_generated_token.setVisibility(View.VISIBLE);

        setLoadingState(false);

        TextView generate_button_text = findTextViewInChildren(button_generate_token);
        if (generate_button_text != null) {
            generate_button_text.setText("Token Generated");
            generate_button_text.setTextColor(Color.WHITE);

            text_description.setText("Touch 'Continue' to start using the app.");
        }

        TextView use_button_text = findTextViewInChildren(button_use_token);
        if (use_button_text != null) {
            use_button_text.setText("Continue");
            use_button_text.setTextColor(Color.WHITE);
        }

        button_use_token.setEnabled(true);

        Toast.makeText(requireContext(), "Token generated successfully!", Toast.LENGTH_SHORT).show();
    }

    private void useExistingToken() {
        String token = input_token.getText().toString().trim();

        if (generated_token != null && !generated_token.isEmpty()) {
            proceedToMainScreen();
            return;
        }
        if (!token_container.isValidToken(token)) {
            input_token_layout.setError("Invalid Token Format");
            return;
        }

        setLoadingState(true);

        if (token_container.saveToken(token)) {
            setLoadingState(false);
            Toast.makeText(requireContext(), "Token saved successfully!", Toast.LENGTH_SHORT).show();
            proceedToMainScreen();
        } else {
            setLoadingState(false);
            Toast.makeText(requireContext(), "Error saving token", Toast.LENGTH_SHORT).show();
        }
    }

    private void validate_token_input(String token) {
        if (token.isEmpty()) {
            input_token_layout.setError(null);
            button_use_token.setEnabled(false);
            return;
        }

        if (token_container.isValidToken(token)) {
            input_token_layout.setError(null);
            button_use_token.setEnabled(true);

            TextView useButtonText = findTextViewInChildren(button_use_token);
            if (useButtonText != null) {
                useButtonText.setTextColor(Color.WHITE);
            }
        } else {
            input_token_layout.setError("Invalid UUID format (ex: 550e8400-e29b-41d4-a716-446655440000)");
            button_use_token.setEnabled(false);
        }
    }

    private void setLoadingState(boolean isLoading) {
        progress_bar.setVisibility(isLoading ? View.VISIBLE : View.GONE);

        if (isLoading) {
            button_generate_token.setEnabled(false);
            button_use_token.setEnabled(false);
            input_token.setEnabled(false);
            input_token_layout.setEnabled(false);
        } else {
            if (generated_token != null) {
                button_generate_token.setEnabled(false);
                input_token.setEnabled(false);
                input_token_layout.setEnabled(false);
                button_use_token.setEnabled(true);

                TextView useButtonText = findTextViewInChildren(button_use_token);
                if (useButtonText != null) {
                    useButtonText.setTextColor(Color.WHITE);
                }
            } else {
                button_generate_token.setEnabled(true);
                input_token.setEnabled(true);
                input_token_layout.setEnabled(true);
                validate_token_input(input_token.getText().toString());
            }
        }
    }

    private void proceedToMainScreen() {
        token_container.setFirstLaunch();

        if (getActivity() instanceof MainActivity) {

            ((MainActivity) getActivity()).loadFragment(new MainScreen());
        }
    }

    private TextView findTextViewInChildren(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof TextView) {
                return (TextView) child;
            } else if (child instanceof ViewGroup) {
                TextView found = findTextViewInChildren((ViewGroup) child);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
