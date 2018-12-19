package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

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
        reminderOn = false;

        mDatabaseHelper = new DataBaseHelper(this);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Dit haalt het huidige scherm van de stack af in plaats van een nieuwe toevoegen.
            }
        });

        prestatiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, PrestatiesActivity.class);
                startActivity(intent);
            }
        });

        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Switch", "staat de switch aan: " + isChecked);

                Cursor data = mDatabaseHelper.getAllData("settings");

                while(data.moveToNext()) {
                    Log.d("Table Data", "Col ID: " + data.getString(0));
                    Log.d("Table Data", "Col Reminder: " + data.getString(1));
                }

//                if(isChecked) {
//                    mDatabaseHelper.updateSettings(0, 1);
//                }

            }
        });
    }
}
