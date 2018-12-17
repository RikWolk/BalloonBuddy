package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PrestatiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestaties);

        ImageButton settingsButton;
        ImageButton homeButton;

        settingsButton = (ImageButton) findViewById(R.id.pauseButton);
        homeButton = (ImageButton) findViewById(R.id.pauseButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrestatiesActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Dit haalt het huidige scherm van de stack af in plaats van een nieuwe bovenop toevoegen.
            }
        });


    }


}