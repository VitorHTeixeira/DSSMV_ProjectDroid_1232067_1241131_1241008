package com.lvhm.covertocover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SettingsScreen extends Fragment {
    private RadioGroup app_theme_radio_group, export_app_data_radio_group;
    private ImageView app_theme_arrow, export_app_data_arrow;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        RelativeLayout app_theme_toggle = view.findViewById(R.id.app_theme_menu);
        app_theme_toggle.setOnClickListener(this::clickAction);
        app_theme_radio_group = view.findViewById(R.id.app_theme_radio_group);

        RelativeLayout export_app_data_toggle = view.findViewById(R.id.export_app_data_menu);
        export_app_data_toggle.setOnClickListener(this::clickAction);
        export_app_data_radio_group = view.findViewById(R.id.export_app_data_radio_group);

        RelativeLayout force_save_data_toggle = view.findViewById(R.id.force_save_data_menu);
        force_save_data_toggle.setOnClickListener(this::clickAction);

        RelativeLayout profile_settings_toggle = view.findViewById(R.id.profile_settings_menu);
        profile_settings_toggle.setOnClickListener(this::clickAction);


        app_theme_arrow = view.findViewById(R.id.app_theme_arrow);
        export_app_data_arrow = view.findViewById(R.id.export_app_data_arrow);


        return view;
    }
    protected void clickAction(View view) {
        int id = view.getId();
        if (id == R.id.app_theme_menu) {
            toggleVisibility(app_theme_radio_group, app_theme_arrow);
        }
        else if (id == R.id.export_app_data_menu) {
            toggleVisibility(export_app_data_radio_group, export_app_data_arrow);
        }
        else if (id == R.id.force_save_data_menu) {}
        else if (id == R.id.profile_settings_menu) {
            Fragment fragment = new ProfileSettingsScreen();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
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
