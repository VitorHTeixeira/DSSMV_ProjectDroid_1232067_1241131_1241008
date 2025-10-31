package com.lvhm.covertocover.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.lvhm.covertocover.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SettingsScreen extends Fragment {
    private RadioGroup app_theme_radio_group, export_app_data_radio_group;
    private ImageView app_theme_arrow, export_app_data_arrow;
    private SharedPreferences shared_preferences = null;
    private static final String PREFERENCES_FILE = "CTCPreferences";
    private static final String THEME_KEY = "ThemeMode";
    private static final String PROFILE_IMAGE_KEY = "ProfileImageBase64";
    private ImageView profile_picture;
    private ActivityResultLauncher<String> gallery_launcher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        shared_preferences = requireActivity().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        // User Overview
        TextView username = view.findViewById(R.id.username);
        username.setText(shared_preferences.getString("profile_username", "Username"));

        // User profile picture
        profile_picture = view.findViewById(R.id.profile_picture);
        profile_picture.setClipToOutline(true);
        profile_picture.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });

        String imageBase64 = shared_preferences.getString(PROFILE_IMAGE_KEY, null);
        if (imageBase64 != null) {
            try {
                byte[] byteArray = Base64.decode(imageBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                if (bitmap != null) {
                    profile_picture.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }

        gallery_launcher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null && getContext() != null) {
                        try {
                            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(
                                    requireContext().getContentResolver(), uri);

                            Bitmap resizedBitmap = getResizedBitmap(originalBitmap, 512);

                            profile_picture.setImageBitmap(resizedBitmap);

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            resizedBitmap.compress(Bitmap.CompressFormat.WEBP, 80, baos);
                            byte[] byteArray = baos.toByteArray();
                            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                            SharedPreferences.Editor editor = shared_preferences.edit();
                            editor.putString(PROFILE_IMAGE_KEY, base64Image);
                            editor.apply();

                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("SettingsScreen", "Nenhuma imagem selecionada.");
                    }
                });

        profile_picture.setOnClickListener(v -> gallery_launcher.launch("image/*"));


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

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    protected void clickAction(View view) {
        int view_id = view.getId();
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
