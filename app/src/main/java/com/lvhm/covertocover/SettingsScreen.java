package com.lvhm.covertocover;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsScreen extends Fragment {
    private RadioGroup app_theme_radio_group, export_app_data_radio_group;
    private ImageView app_theme_arrow, export_app_data_arrow;
    private static final String PREFERENCES_FILE = "CTCPreferences";
    private static final String THEME_KEY = "ThemeMode";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // App Theme
        RelativeLayout app_theme_toggle = view.findViewById(R.id.app_theme_menu);
        app_theme_toggle.setOnClickListener(this::clickAction);
        app_theme_radio_group = view.findViewById(R.id.app_theme_radio_group);

        RadioButton radio_light_mode = view.findViewById(R.id.radio_light_mode);
        radio_light_mode.setOnClickListener(this::clickAction);

        RadioButton radio_dark_mode = view.findViewById(R.id.radio_dark_mode);
        radio_dark_mode.setOnClickListener(this::clickAction);

        RadioButton radio_system_preference = view.findViewById(R.id.radio_system_preference);
        radio_system_preference.setOnClickListener(this::clickAction);

        switch (requireActivity().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                radio_light_mode.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                radio_system_preference.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                radio_dark_mode.setChecked(true);
                break;
        }

        // Export
        RelativeLayout export_app_data_toggle = view.findViewById(R.id.export_app_data_menu);
        export_app_data_toggle.setOnClickListener(this::clickAction);
        export_app_data_radio_group = view.findViewById(R.id.export_app_data_radio_group);

        // Force Save
        RelativeLayout force_save_data_toggle = view.findViewById(R.id.force_save_data_menu);
        force_save_data_toggle.setOnClickListener(this::clickAction);

        // Profile Settings
        RelativeLayout profile_settings_toggle = view.findViewById(R.id.profile_settings_menu);
        profile_settings_toggle.setOnClickListener(this::clickAction);

        app_theme_arrow = view.findViewById(R.id.app_theme_arrow);
        export_app_data_arrow = view.findViewById(R.id.export_app_data_arrow);

        return view;
    }

    protected void clickAction(View view) {
        int view_id = view.getId();
        SharedPreferences shared_preferences = requireActivity().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_preferences.edit();
        if (view_id == R.id.app_theme_menu) {
            toggleVisibility(app_theme_radio_group, app_theme_arrow);
        }
        else if (view_id == R.id.export_app_data_menu) {
            toggleVisibility(export_app_data_radio_group, export_app_data_arrow);
        }
        else if (view_id == R.id.force_save_data_menu) {}
        else if (view_id == R.id.profile_settings_menu) {
            Fragment fragment = new ProfileSettingsScreen();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        else if (view_id == R.id.radio_light_mode) {
            editor.putInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_NO);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if (view_id == R.id.radio_dark_mode) {
            editor.putInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_YES);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else if (view_id == R.id.radio_system_preference) {
            editor.putInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    private void toggleVisibility(View view, ImageView arrow) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            rotateArrow(arrow, view);
        } else {
            view.setVisibility(View.VISIBLE);
            rotateArrow(arrow, view);
        }
    }

    private void rotateArrow(View arrow, View content) {
        if (content.getVisibility() == View.VISIBLE) {
            arrow.animate().rotation(90).setDuration(200).start();
        } else {
            arrow.animate().rotation(0).setDuration(200).start();
        }
    }
}
