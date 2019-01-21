package com.example.echo.balloonbuddy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class AlarmReceiver extends BroadcastReceiver {

    // Channel ID wordt naar 100 gezet
    private static final String CHANNEL_ID = "100";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Intent aanmaken welke geopend wordt als de notificatie aangeklikt wordt
        Intent notificationIntent = new Intent(context, MainActivity.class);

        // Bouw de stack aan schermen, enkel de main activity in dit geval
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        // Maak een pending intent
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Maak een notificatie builder aan. Hiermee kan de notificatie opgebouwd worden
        Notification.Builder builder = new Notification.Builder(context);

        // Geef bepaalde instellingen mee aan een notificatie
        Notification notification = builder.setContentTitle("Oefen herinnering")
                .setContentText("Je hebt al 24 uur niet geoefend met Balloon Buddy")
                .setTicker("Oefen herinnering")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        // Zet het Channel ID in de builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        // Maak een notification manager aan die de notificatie regelt
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Geef Channel informatie mee aan de notificatie en maak het aan
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Laat de notification manager weten welke notification die moet laten zien
        notificationManager.notify(0, notification);
    }
}