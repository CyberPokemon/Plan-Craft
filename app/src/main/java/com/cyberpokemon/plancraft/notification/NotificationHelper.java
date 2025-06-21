package com.cyberpokemon.plancraft.notification;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.cyberpokemon.plancraft.R;

public class NotificationHelper {


    public static void showNotification(Context applicationContext, String title, String message) {
        // ✅ Check for notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // 🚫 You should NOT request permission here unless context is an Activity
                if (applicationContext instanceof Activity) {
                    ActivityCompat.requestPermissions((Activity) applicationContext,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            1001);
                }
                return; // Don't show notification without permission
            }
        }


        NotificationManager manager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "task_reminder_channel";


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(channelId,"Task Reminder", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(applicationContext,channelId)
                .setSmallIcon(R.drawable.plancraft_image1)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        manager.notify((int)System.currentTimeMillis(),builder.build());
    }
}
