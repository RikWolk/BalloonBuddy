package com.example.echo.balloonbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PauseActivity extends Activity {

    // Maak variabelen aan voor de buttons
    ImageButton resumeButton;
    ImageButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Koppel UI buttons aan de variabelen
        resumeButton = findViewById(R.id.resumeButton);
        homeButton = findViewById(R.id.homeButton);

        // Onclick listener voor de resume button
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop de pauze activity
                finish();
            }
        });

        // Onclick listener voor de home button
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Maak een nieuwe intent aan
                Intent intent = new Intent(PauseActivity.this, MainActivity.class);

                // Zorg er voor dat dit de enige activity is die bekend is
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Start het
                startActivity(intent);
            }
        });
    }
}
