package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PrestatiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestaties);

        ImageButton settingsButton;
        ImageButton prestatiesButton;

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        prestatiesButton = (ImageButton) findViewById(R.id.prestatiesButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrestatiesActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        prestatiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrestatiesActivity.this, PrestatiesActivity.class);
                startActivity(intent);
            }
        });

        int[] i = {R.mipmap.icons_beker_locked_v01, R.mipmap.icons_beker_unlocked_v01};

    }


}