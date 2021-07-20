package com.zebra.jamesswinton.tc2xdropdetectdemo.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;
import com.zebra.jamesswinton.tc2xdropdetectdemo.DropDetectionService;
import com.zebra.jamesswinton.tc2xdropdetectdemo.R;

public class NotificationHelper {

    // Constants
    public static final int NOTIFICATION_ID = 1;

    // Actions
    public static final String ACTION_STOP_SERVICE = "com.zebra.tc2xdropdetectdemo.STOP_SERVICE";

    public static Notification createNotification(Context cx) {
        // Create Variables
        String channelId = "com.zebra.jamesswinton.tc2xdropdetectdemo";
        String channelName = "Custom Background Notification Channel";

        // Create Channel
        NotificationChannel notificationChannel = new NotificationChannel(channelId,
                channelName, android.app.NotificationManager.IMPORTANCE_NONE);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        // Set Channel
        android.app.NotificationManager manager = (android.app.NotificationManager)
                cx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(notificationChannel);
        }

        // Build Notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(cx,
                channelId);

        // Set Notification Options
        notificationBuilder.setContentTitle("Drop Detection Logging Active")
                .setSmallIcon(R.drawable.ic_drop)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true);

        // Add Buttons
        Intent stopIntent = new Intent(cx, DropDetectionService.class);
        stopIntent.setAction(ACTION_STOP_SERVICE);
        PendingIntent stopPendingIntent = PendingIntent.getService(cx,
                0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action stopServiceAction = new NotificationCompat.Action(
                R.drawable.ic_drop,
                "Stop Service",
                stopPendingIntent
        );
        notificationBuilder.addAction(stopServiceAction);

        // Build & Return Notification
        return notificationBuilder.build();
    }
}
