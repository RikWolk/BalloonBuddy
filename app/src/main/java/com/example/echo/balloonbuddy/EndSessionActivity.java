package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class EndSessionActivity extends AppCompatActivity {

    // Maak variabelen aan
    DataBaseHelper mDataBaseHelper;
    String scoreDisplay;
    String mistakesDisplay;
    int scoreInsert;
    int mistakesInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_session);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Maak een instantie van de DataBaseHelper aan
        mDataBaseHelper = new DataBaseHelper(this);

        // Haal de home button op
        ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);

        // Haal twee tekst velden op
        TextView score = (TextView) findViewById(R.id.score);
        TextView mistakes = (TextView) findViewById(R.id.mistakes);

        // Haal de getExtra's op meegekregen vanuit de vorige intent
        Bundle score_data = getIntent().getExtras();

        // Haal data uit de getExtra
        scoreDisplay = score_data.getString("score");
        mistakesDisplay = score_data.getString("mistakes");

        // Zorg dat de data naar Integers wordt omgezet
        scoreInsert = Integer.parseInt(scoreDisplay);
        mistakesInsert = Integer.parseInt(mistakesDisplay) / 2;

        // Laat tekst zien in de frontend
        score.setText(String.valueOf("Score : " + scoreDisplay));
        mistakes.setText(String.valueOf(mistakesInsert + " seconden door elkaar heen gesproken"));

        // Maak de home button klikbaar
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish de huidige activvity
                finish();
                // Voeg een record toe aan de SCORES tabel in de database
                mDataBaseHelper.insertScore(scoreInsert, mistakesInsert);
                // Start de main activity
                Intent intent = new Intent(EndSessionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
