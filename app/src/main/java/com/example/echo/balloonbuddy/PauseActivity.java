package com.example.echo.balloonbuddy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PauseActivity extends Activity {

    ImageButton resumeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

//        Bundle extras = getIntent().getExtras();
//        int timeRemaining = extras.getInt("timeRemaining");
//        Log.d("PAUSE", "DIT IS DE PAUSE EN DE MILLIS: " + timeRemaining);
//        final GameTimer gameTimer = new GameTimer(timeRemaining, 1000);

        resumeButton = (ImageButton) findViewById(R.id.resumeButton);

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                gameTimer.start();
                finish();
            }
        });

    }

}
