package com.lvhm.covertocover;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ReviewContainer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FrameLayout navMain, navProfile, navCamera, navMap, navSettings;
    private PermissionsHandler permissions_handler;
    private BookContainer book_container;
    private ReviewContainer review_container;
    private static final String PREFERENCES_FILE = "CTCPreferences";
    private static final String THEME_KEY = "ThemeMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences shared_preferences = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        int theme_mode = shared_preferences.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(theme_mode);

        book_container = BookContainer.getInstance();
        review_container = ReviewContainer.getInstance();

        setContentView(R.layout.activity_main);

        permissions_handler = new PermissionsHandler(this);

        navMain = findViewById(R.id.nav_main);
        navProfile = findViewById(R.id.nav_profile);
        navCamera = findViewById(R.id.nav_camera);
        navMap = findViewById(R.id.nav_map);
        navSettings = findViewById(R.id.nav_settings);

        setupNavigationListeners();
        
        if (savedInstanceState == null) {
            loadFragment(new MainScreen());
            permissions_handler.requestPermissions();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ArrayList<Book> books = book_container.getBooks();
        for(Book book : books) {
            System.out.println(book.getName());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissions_handler.handlePermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupNavigationListeners() {
        navMain.setOnClickListener(v -> loadFragment(new MainScreen()));
        navProfile.setOnClickListener(v -> loadFragment(new ProfileScreen()));
        navCamera.setOnClickListener(v -> loadFragment(new CameraScreen()));
        navMap.setOnClickListener(v -> loadFragment(new MapScreen()));
        navSettings.setOnClickListener(v -> loadFragment(new SettingsScreen()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragment_manager = getSupportFragmentManager();
        FragmentTransaction transaction = fragment_manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
