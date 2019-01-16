package com.example.echo.balloonbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class EndSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_session);

        ImageButton homeButton;

        homeButton = (ImageButton) findViewById(R.id.homeButton);

        TextView score = (TextView) findViewById(R.id.score);

        Bundle score_data = getIntent().getExtras();
        if(score_data != null){
            String scoreDisplay = score_data.getString("score");
            score.setText(String.valueOf("Score : " + scoreDisplay));
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(EndSessionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
