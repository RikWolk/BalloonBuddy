package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PrestatiesActivity extends AppCompatActivity {

    boolean vlucht1unlocked = true;
    boolean vlucht100unlocked = false;
    boolean vlucht250unlocked = true;

    ImageView vlucht1image;
    ImageView vlucht100image;
    ImageView vlucht250image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestaties);

        ImageButton settingsButton;
        ImageButton homeButton;

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        homeButton = (ImageButton) findViewById(R.id.homeButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PrestatiesActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vlucht1image = (ImageView) findViewById(R.id.vlucht1image);
        vlucht100image = (ImageView) findViewById(R.id.vlucht100image);
        vlucht250image = (ImageView) findViewById(R.id.vlucht250image);


        if(vlucht1unlocked == true){
            vlucht1image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
        }

        if(vlucht100unlocked == true){
            vlucht100image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
        }

        if(vlucht250unlocked == true){
            vlucht250image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
        }

    }

}