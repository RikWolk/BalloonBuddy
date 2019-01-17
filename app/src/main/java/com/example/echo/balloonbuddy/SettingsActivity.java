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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.echo.balloonbuddy.AlarmService;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    DataBaseHelper mDatabaseHelper;
    public static Boolean reminderOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton homeButton;
        ImageButton prestatiesButton;
        Switch reminder;

        homeButton = (ImageButton) findViewById(R.id.homeButton);
        prestatiesButton = (ImageButton) findViewById(R.id.prestatiesButton);
        reminder = (Switch) findViewById(R.id.reminder);
//        reminderOn = false;

        mDatabaseHelper = new DataBaseHelper(this);

        int reminderState = mDatabaseHelper.getReminderSetting();

        if(reminderState == 1) {
            reminder.setChecked(true);
        } else {
            reminder.setChecked(false);
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prestatiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SettingsActivity.this, PrestatiesActivity.class);
                startActivity(intent);
            }
        });

        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // Init alarm service
        Intent notificationIntent = new Intent(this, com.example.echo.balloonbuddy.AlarmReceiver.class); // Maak de notification intent
        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT); // Gooi de intent in een broadcast

        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mDatabaseHelper.updateSettings(1, 1);
                    Calendar cal = Calendar.getInstance(); // Maak een kalender
                    cal.add(Calendar.SECOND, 10);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                } else {
                    mDatabaseHelper.updateSettings(1, 0);
                    alarmManager.cancel(broadcast);
                }
            }
        });
    }
}
