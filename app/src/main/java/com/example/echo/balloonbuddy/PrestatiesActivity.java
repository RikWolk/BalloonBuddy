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
    boolean vlucht250unlocked = false;
    boolean login7unlocked = true;
    boolean login14unlocked = false;
    boolean login28unlocked = false;
    boolean ranglijst100unlocked = true;
    boolean ranglijst15unlocked = false;
    boolean ranglijst5unlocked = false;
    boolean xp1000unlocked = true;
    boolean xp2500unlocked = false;
    boolean xp5000unlocked = false;

    ImageView vlucht1image;
    ImageView vlucht100image;
    ImageView vlucht250image;
    ImageView login7image;
    ImageView login14image;
    ImageView login28image;
    ImageView ranglijst100image;
    ImageView ranglijst15image;
    ImageView ranglijst5image;
    ImageView xp1000image;
    ImageView xp2500image;
    ImageView xp5000image;

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

        // Koppel variable aan xml element
        vlucht1image = (ImageView) findViewById(R.id.vlucht1image);
        vlucht100image = (ImageView) findViewById(R.id.vlucht100image);
        vlucht250image = (ImageView) findViewById(R.id.vlucht250image);
        login7image  = (ImageView) findViewById(R.id.login7image);
        login14image = (ImageView) findViewById(R.id.login14image);
        login28image = (ImageView) findViewById(R.id.login28image);
        ranglijst100image = (ImageView) findViewById(R.id.ranglijst100image);
        ranglijst15image = (ImageView) findViewById(R.id.ranglijst15image);
        ranglijst5image = (ImageView) findViewById(R.id.ranglijst5image);
        xp1000image = (ImageView) findViewById(R.id.xp1000image);
        xp2500image = (ImageView) findViewById(R.id.xp2500image);
        xp5000image = (ImageView) findViewById(R.id.xp5000image);

        // Vervang image als aan unlock criterium is voldaan.
        if(vlucht1unlocked == true){
            vlucht1image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
        }

        if(vlucht100unlocked == true){
            vlucht100image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
        }

        if(vlucht250unlocked == true){
            vlucht250image.setImageResource(R.mipmap.icons_beker_unlocked_v01);
        }

        if(login7unlocked == true){
            login7image.setImageResource(R.mipmap.icons_award_unlocked_v01);
        }

        if(login14unlocked == true){
            login14image.setImageResource(R.mipmap.icons_award_unlocked_v01);
        }

        if(login28unlocked == true){
            login28image.setImageResource(R.mipmap.icons_award_unlocked_v01);
        }

        if(ranglijst100unlocked == true){
            ranglijst100image.setImageResource(R.mipmap.icons_leaderboard_unlocked_v01);
        }

        if(ranglijst15unlocked == true){
            ranglijst15image.setImageResource(R.mipmap.icons_leaderboard_unlocked_v01);
        }

        if(ranglijst5unlocked == true){
            ranglijst5image.setImageResource(R.mipmap.icons_leaderboard_unlocked_v01);
        }

        if(xp1000unlocked == true){
            xp1000image.setImageResource(R.mipmap.icons_lintje_unlocked_v01);
        }

        if(xp2500unlocked == true){
            xp2500image.setImageResource(R.mipmap.icons_lintje_unlocked_v01);
        }

        if(xp5000unlocked == true){
            xp5000image.setImageResource(R.mipmap.icons_lintje_unlocked_v01);
        }

    }

}