package com.lvhm.covertocover;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private FrameLayout navMain, navProfile, navCamera, navMap, navSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navMain = findViewById(R.id.nav_main);
        navProfile = findViewById(R.id.nav_profile);
        navCamera = findViewById(R.id.nav_camera);
        navMap = findViewById(R.id.nav_map);
        navSettings = findViewById(R.id.nav_settings);

        setupNavigationListeners();

        if (savedInstanceState == null) {
            loadFragment(new MainScreen());
        }
    }

    private void setupNavigationListeners() {
        navMain.setOnClickListener(v -> loadFragment(new MainScreen()));
        navProfile.setOnClickListener(v -> loadFragment(new ProfileScreen()));
//        navCamera.setOnClickListener(v -> loadFragment(new CameraScreen()));
//        navMap.setOnClickListener(v -> loadFragment(new MapScreen()));
        navSettings.setOnClickListener(v -> loadFragment(new SettingsScreen()));

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragment_manager = getSupportFragmentManager();
        FragmentTransaction transaction = fragment_manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
