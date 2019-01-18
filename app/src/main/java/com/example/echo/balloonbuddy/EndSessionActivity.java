package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class EndSessionActivity extends AppCompatActivity {

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

        mDataBaseHelper = new DataBaseHelper(this);

        ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);

        TextView score = (TextView) findViewById(R.id.score);
        TextView mistakes = (TextView) findViewById(R.id.mistakes);

        Bundle score_data = getIntent().getExtras();

        scoreDisplay = score_data.getString("score");
        mistakesDisplay = score_data.getString("mistakes");

        scoreInsert = Integer.parseInt(scoreDisplay);
        mistakesInsert = Integer.parseInt(mistakesDisplay);

        score.setText(String.valueOf("Score : " + scoreDisplay));
        mistakes.setText(String.valueOf("Fouten: " + mistakesDisplay));

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mDataBaseHelper.insertScore(Integer.parseInt(scoreDisplay), Integer.parseInt(mistakesDisplay));
                Intent intent = new Intent(EndSessionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
