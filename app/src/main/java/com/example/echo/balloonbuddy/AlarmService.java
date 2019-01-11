package com.example.echo.balloonbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

public class AlarmService extends AppCompatActivity {
    public void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // Init alarm service

        Intent notificationIntent = new Intent(this, com.example.echo.balloonbuddy.AlarmReceiver.class); // Maak de notification intent
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT); // Gooi de intent in een broadcast

        Calendar cal = Calendar.getInstance(); // Maak een kalender

//        cal.add(Calendar.HOUR, 24); // Voeg aan de kalender 24u toe
        cal.add(Calendar.SECOND, 10);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast); // Gooi het alarm zodra de tijd de kalender + 24u bereikt
    }
}
