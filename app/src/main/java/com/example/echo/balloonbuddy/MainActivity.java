package com.example.echo.balloonbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.pm.ActivityInfo;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Maak een databasehelper aan
    DataBaseHelper mDatabaseHelper;

    // Variabelen voor de buttons
    ImageButton settingsButton;
    ImageButton prestatiesButton;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Geef de context mee aan de databasehelper
        mDatabaseHelper = new DataBaseHelper(this);

        // Koppel de UI buttons aan de variabelen
        settingsButton = (ImageButton) findViewById(R.id.pauseButton);
        prestatiesButton = (ImageButton) findViewById(R.id.prestatiesButton);
        startButton = (Button) findViewById(R.id.startButton);

        // Onclick voor settingsknop
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Maak en start settings activity
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Onclick voor prestatiesknop
        prestatiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Maak en start prestatie activity
                Intent intent = new Intent(MainActivity.this, PrestatiesActivity.class);
                startActivity(intent);
            }
        });

        // Onclick voor startknop
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Maak en start devicelist activity
                Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
                startActivity(intent);
            }
        });

        // Haal de huidge setting van de reminder op
        int reminderState = mDatabaseHelper.getReminderSetting();

        // Init alarm service
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Maak de notification intent
        Intent notificationIntent = new Intent(this, com.example.echo.balloonbuddy.AlarmReceiver.class);

        // Gooi de intent in een broadcast
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        // Als de setting zegt dat er herinnerd mag worden, maak dan een herinnering aan voor over 24 uur
        if (reminderState == 1) {
            // Maak een kalender voor de tijd
            Calendar cal = Calendar.getInstance();

            // Voeg er 24 uur aan toe
            cal.add(Calendar.SECOND, 86400);

            // Gooi het alarm zodra de tijd van de kalender + 24u bereikt is
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        } else {
            // Als de setting aangeeft dat er niet herinnerd hoeft te worden, cancel dan het alarm
            alarmManager.cancel(broadcast);
        }
    }
}
