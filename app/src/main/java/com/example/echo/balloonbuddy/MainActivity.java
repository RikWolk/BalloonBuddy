package com.example.echo.balloonbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.pm.ActivityInfo;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper mDatabaseHelper;
    AlarmService mAlarmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mDatabaseHelper = new DataBaseHelper(this);

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

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // Init alarm service
        Intent notificationIntent = new Intent(this, com.example.echo.balloonbuddy.AlarmReceiver.class); // Maak de notification intent
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT); // Gooi de intent in een broadcast

        // LOCAL NOTIFICATIONS
        if (reminderState == 1) {
            Calendar cal = Calendar.getInstance(); // Maak een kalender
            cal.add(Calendar.SECOND, 86400);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast); // Gooi het alarm zodra de tijd de kalender + 24u bereikt
        } else {
            alarmManager.cancel(broadcast);
        }
    }
}
