package com.lvhm.covertocover;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class PermissionsHandler {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private final Activity activity;

    public PermissionsHandler(Activity activity) {
        this.activity = activity;
    }

    public void requestPermissions() {
        String[] permissions_max32 = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        String[] permissions_min33 = {
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
        };
        List<String> permissions_to_request = new ArrayList<>();
        if(android.os.Build.VERSION.SDK_INT >= 33) {
            for (String permission : permissions_min33) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissions_to_request.add(permission);
                }
            }
        }
        else {
            for (String permission : permissions_max32) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissions_to_request.add(permission);
                }
            }
        }
        if(!permissions_to_request.isEmpty()) {
            boolean show_camera_warning = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA);
            boolean show_location_warning = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION);
            boolean show_write_warning = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            boolean show_read_warning = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(show_camera_warning || show_location_warning || show_write_warning || show_read_warning) {
                new AlertDialog.Builder(activity)
                        .setTitle("Permissions Required")
                        .setMessage("This app requires all the permissions to function properly.\nPlease grant them to continue.")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            ActivityCompat.requestPermissions(activity,
                                    permissions_to_request.toArray(new String[0]),
                                    PERMISSION_REQUEST_CODE);
                        })
                        .setNegativeButton("No", (dialog, which) -> activity.finish())
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(activity,
                        permissions_to_request.toArray(new String[0]),
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void handlePermissionsResult(int request_code, @NonNull String[] permissions, @NonNull int[] grant_results) {
        if (request_code == PERMISSION_REQUEST_CODE) {
            List<String> denied_permissions = new ArrayList<>();
            for(int i = 0; i < permissions.length; i++) {
                if(grant_results[i] != PackageManager.PERMISSION_GRANTED) {
                    denied_permissions.add(permissions[i]);
                }
            }
            if(!denied_permissions.isEmpty()) {
                activity.finish();
            }
        }
    }
}
