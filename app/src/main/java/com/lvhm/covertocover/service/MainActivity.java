package com.lvhm.covertocover.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lvhm.covertocover.PermissionsHandler;
import com.lvhm.covertocover.R;
import com.lvhm.covertocover.adapter.BookNavigationListener;
import com.lvhm.covertocover.api.DatabaseAPIClient;
import com.lvhm.covertocover.api.DownloadDBWorker;
import com.lvhm.covertocover.api.UploadDBWorker;
import com.lvhm.covertocover.models.Book;
import com.lvhm.covertocover.models.Review;
import com.lvhm.covertocover.repo.BookContainer;
import com.lvhm.covertocover.repo.ReviewContainer;
import com.lvhm.covertocover.repo.UserTokenContainer;

public class MainActivity extends AppCompatActivity implements BookNavigationListener {
    private FrameLayout navMain, navProfile, navCamera, navMap, navSettings;
    private PermissionsHandler permissions_handler;
    private BookContainer book_container;
    private ReviewContainer review_container;
    private static final String PREFERENCES_FILE = "CTCPreferences";
    private static final String THEME_KEY = "ThemeMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseAPIClient.initialize(this);
        OneTimeWorkRequest upload_work = new OneTimeWorkRequest.Builder(DownloadDBWorker.class).build();
        WorkManager.getInstance(this).enqueue(upload_work);

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

        if (savedInstanceState == null) {
            UserTokenContainer token_container = UserTokenContainer.getInstance(this);

            if (!token_container.hasToken()) {
                loadFragment(new LoginTokenScreen());
            } else {
                loadFragment(new MainScreen());
            }

            permissions_handler.requestPermissions();
        } else {
            Fragment current_fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (current_fragment instanceof LoginTokenScreen) {
                disableNavigationListeners();
            } else {
                setupNavigationListeners();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        UserTokenContainer token_container = UserTokenContainer.getInstance(this);

        if (token_container.hasToken()) {
            OneTimeWorkRequest upload_work = new OneTimeWorkRequest.Builder(UploadDBWorker.class).build();
            WorkManager.getInstance(this).enqueue(upload_work);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissions_handler.handlePermissionsResult(requestCode, permissions, grantResults);
    }
    public void loadFragment(Fragment fragment) {
        if (fragment instanceof LoginTokenScreen) {
            disableNavigationListeners();
        } else {
            setupNavigationListeners();
        }

        FragmentManager fragment_manager = getSupportFragmentManager();
        FragmentTransaction transaction = fragment_manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void setupNavigationListeners() {
        navMain.setClickable(true);
        navProfile.setClickable(true);
        navCamera.setClickable(true);
        navMap.setClickable(true);
        navSettings.setClickable(true);

        navMain.setOnClickListener(v -> loadFragment(new MainScreen()));
        navProfile.setOnClickListener(v -> loadFragment(new ProfileScreen()));
        navCamera.setOnClickListener(v -> loadFragment(new CameraScreen()));
        navMap.setOnClickListener(v -> loadFragment(new MapScreen()));
        navSettings.setOnClickListener(v -> loadFragment(new SettingsScreen()));
    }

    private void disableNavigationListeners() {
        navMain.setClickable(false);
        navProfile.setClickable(false);
        navCamera.setClickable(false);
        navMap.setClickable(false);
        navSettings.setClickable(false);
    }

    @Override
    public void navigateToBookScreenFromBook(Book book) {
        Fragment book_screen = BookScreen.newInstanceFromBook(book);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, book_screen)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateToBookScreenFromAPI(Bundle api_bundle) {
        Fragment book_screen = BookScreen.newInstanceFromAPI(api_bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, book_screen)
                .addToBackStack(null)
                .commit();
    }
}
