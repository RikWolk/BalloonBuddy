package com.example.echo.balloonbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PauseActivity extends Activity {

    ImageButton resumeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        resumeButton = findViewById(R.id.resumeButton);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
