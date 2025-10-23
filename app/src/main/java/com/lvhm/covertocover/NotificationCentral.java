package com.lvhm.covertocover;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationCentral {
    private static final int NOTIFICATION_ID = 1;

    public static void createNotificationChannel(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notification_channel = new NotificationChannel("notification_service", "Notification Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notification_manager = context.getSystemService(NotificationManager.class);
            notification_manager.createNotificationChannel(notification_channel);
        }
    }
    public static void showNotification(Context context, final String message) {
        createNotificationChannel(context);
        NotificationCompat.Builder notification_builder = new NotificationCompat.Builder(context, "notification_service");
        notification_builder.setSmallIcon(R.drawable.pencil_icon_ui)
                .setContentTitle("CoverToCover")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroupSummary(false);
        NotificationManagerCompat notification_manager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notification_manager.notify(NOTIFICATION_ID, notification_builder.build());
    }
}
