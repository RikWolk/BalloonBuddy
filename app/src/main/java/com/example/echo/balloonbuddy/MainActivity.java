package com.example.echo.balloonbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DataBaseHelper(this);

//        mDatabaseHelper.dropTable("settings");
//        mDatabaseHelper.dropTable("achievements");

        ImageButton settingsButton;
        ImageButton prestatiesButton;
        Button startButton;

        settingsButton = (ImageButton) findViewById(R.id.pauseButton);
        prestatiesButton = (ImageButton) findViewById(R.id.prestatiesButton);
        startButton = (Button) findViewById(R.id.startButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        prestatiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PrestatiesActivity.class);
                startActivity(intent);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
                startActivity(intent);
            }
        });

        int reminderState = mDatabaseHelper.getReminderSetting();
        Log.d("ReminderState", "De state is: " + reminderState);

        // LOCAL NOTIFICATIONS
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // Init alarm service

        Intent notificationIntent = new Intent(this, com.example.echo.balloonbuddy.AlarmReceiver.class); // Maak de notification intent
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT); // Gooi de intent in een broadcast

        Calendar cal = Calendar.getInstance(); // Maak een kalender
//        cal.add(Calendar.HOUR, 24); // Voeg aan de kalender 24u toe
        cal.add(Calendar.SECOND, 10);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast); // Gooi het alarm zodra de tijd de kalender + 24u bereikt
    }
}
