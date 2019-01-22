package com.example.echo.balloonbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    // Maak een databasehelper aan
    DataBaseHelper mDatabaseHelper;

    // Variabelen voor UI componenten
    ImageButton homeButton;
    ImageButton prestatiesButton;
    Switch reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Variabelen koppelen aan UI componenten
        homeButton = (ImageButton) findViewById(R.id.homeButton);
        prestatiesButton = (ImageButton) findViewById(R.id.prestatiesButton);
        reminder = (Switch) findViewById(R.id.reminder);

        // Geef de context mee aan de databasehelper
        mDatabaseHelper = new DataBaseHelper(this);

        // Haal de huidge setting van de reminder op
        int reminderState = mDatabaseHelper.getReminderSetting();

        // Als de setting uit de database 1 is, zet dan de toggle switch op AAN
        if(reminderState == 1) {
            reminder.setChecked(true);
        } else {
            // Zet de switch anders op UIT
            reminder.setChecked(false);
        }

        // De home button eindigt de huidige activity
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Deze knop laat het prestaties schermm verschijnen
        prestatiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish de huidige activity
                finish();

                // Maak prestaties activiity aan en start hem
                Intent intent = new Intent(SettingsActivity.this, PrestatiesActivity.class);
                startActivity(intent);
            }
        });

        // Init alarm service
        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Maak de notification intent
        Intent notificationIntent = new Intent(this, com.example.echo.balloonbuddy.AlarmReceiver.class);

        // Gooi de intent in een broadcast
        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Dit luisterd naar een verandering in de Switch
        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Als de switch aan staat, voer het volgende uit
                if(isChecked) {
                    // Update het record in de tabel
                    mDatabaseHelper.updateSettings(1, 1);

                    // Maak een kalender voor de tijd
                    Calendar cal = Calendar.getInstance();

                    // Voeg er 24 uur aan toe
                    cal.add(Calendar.SECOND, 86400);

                    // Gooi het alarm zodra de tijd van de kalender + 24u bereikt is
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                } else {
                    // Als de Switch uit staat, update dan het record in de tabel
                    mDatabaseHelper.updateSettings(1, 0);

                    // Cancel dan ook het alarm
                    alarmManager.cancel(broadcast);
                }
            }
        });
    }
}
