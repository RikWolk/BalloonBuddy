package com.example.echo.balloonbuddy;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.UUID;

public class GameActivity extends AppCompatActivity {

    // Achtergrond en ballon
    ImageView background1;
    ImageView background2;
    ImageView balloon;

    // Interface
    ImageButton restartButton;
    ImageButton pauseButton;
    TextView scoreDisplay;

    // Integers
    int score = 0;
    int mistakes = 0;
    int balloonState;
    int timeRemaining;
    final int handlerState = 0;

    // Dit is nodig om de background te animeren
    ValueAnimator bga;

    // ProgressBar aanmaken
    ProgressBar mProgressBar;

    // Alles wat nodig is voor de bluetooth connectie
    Handler bluetoothIn;
    BluetoothSocket btSocket = null;
    BluetoothAdapter btAdapter = null;
    ConnectedThread mConnectedThread;
    StringBuilder recDataString = new StringBuilder();

    // Houdt de status van de microfoon bij
    String micState;

    // Dit is de timer die na tien minuten de sessie laat eindigen
    GameTimer gameTimer = new GameTimer(600000, 1000);

    // SPP UUID Service - dit moet voor de meeste apparaten werken
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String voor MAC address van bluetooth apparaat
    private static String address;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Haal de bluetooth module op van de mobiel
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        // Laat de score zien in het scherm
        scoreDisplay = (TextView) findViewById(R.id.liveScore);
        scoreDisplay.setText(String.valueOf(score));

        // Koppel variabelen aan button van de frontend
        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        restartButton = (ImageButton) findViewById(R.id.restartButton);

        // Koppel variabelen aan de achtergrond en de ballon
        background1 = (ImageView) findViewById(R.id.backgroundImage1);
        background2 = (ImageView) findViewById(R.id.backgroundImage2);
        balloon = (ImageView) findViewById(R.id.ballonImage);

        // Init de ProgressBar en zet deze op 50% gevuld
        mProgressBar = findViewById(R.id.progressbar);
        mProgressBar.setProgress(50);

        // Start de achtergrond infinite loop
        startBackgroundAnimation();

        // Start de gameTimer
        gameTimer.start();

        // Luister naar het moment dat de gameTimer is afgelopen en voer dan een methode uit
        gameTimer.setListener(new GameTimer.ChangeListener() {
            @Override
            public void onChange() {
                onTimerFinish();
            }
        });

        // De handler handelt alle berichten afkomstig van de bluetooth af
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    // Lees de message die binnenkomt
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);

                    // De status van de mic
                    int endOfLineIndex = recDataString.indexOf("*");

                    if (endOfLineIndex >= 0) {
                        String mic = recDataString.substring(0, endOfLineIndex);

                        mic = mic.replace("*", "");
                        mic = mic.replaceAll("\\r|\\n", "");

                        micState = mic;

                        // Als de bluetooth module een 1 doorgeeft, gaat het goed
                        if(micState.contains("1")) {
                            micStateGood();
                        }

                        // Als de bluetooth module een 2 doorgeeft, gaat het slecht
                        if(micState.contains("2")) {
                            micStateWrong();
                        }

                        recDataString.delete(0, recDataString.length());
                    }
                }
            }
        };

        // Pauze menu klik
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Neutrliseer de mic door het op nul te zetten
                micState = "0";

                // De tijd die nog over is ophalen uit de GameTimer en opslaan in de GameActivity
                timeRemaining = gameTimer.returnTimeRmaining();

                // Gooi de pauze activity
                Intent intent = new Intent(GameActivity.this, PauseActivity.class);
                startActivity(intent);
            }
        });

        // Herstart de activity
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel de huidige timer
                gameTimer.cancel();

                // Zet de progressbar terug naar 50%
                mProgressBar.setProgress(50);

                // Herstart de activity
                recreate();
            }
        });
    }

    // Wordt afgevuurd als de activity weer door gaat
    @Override
    public void onResume() {
        super.onResume();

        // Ga door met de timer
        resumeTimer();

        // Maak opnieuw cconnnectie met het bluetooth apparaat
        createConnection();
    }

    // Wordt afgevuurd als de activity gepauzeeerd wordt
    @Override
    public void onPause()
    {
        super.onPause();

        // Pauzeer het bewegen van de background
        bga.pause();

        // De GameTimer die gaande is op stop zetten
        gameTimer.cancel();

        try {
            // Laat de bluetooth socket niet open staan als er uit de activity gegaan wordt
            btSocket.close();
        } catch (IOException e) {
            Log.d("GAMEACTIVITY", "Er gaat wat mis met het afsluiten van de socket: " + e);
        }
    }

    // Wordt aaangeroepen als het goed gaat
    private void micStateGood() {
        // Gooi de score omhoog
        score++;

        // Update de score in de UI
        scoreDisplay.setText(String.valueOf(score));

        // Progressbar ophogen met een punt
        ProgressbarChanger.up(mProgressBar);

        // Als de ballon status niet twee is, dan staat de ballon nog niet op het hoogste punt
        if(balloonState != 2) {
            // De ballon moet 1 plek omhoog
            BalloonMover.up(balloon, GameActivity.this, balloonState);

            // Verhoog de ballon status
            balloonState++;
        }
    }

    // Wordt aaangeroepen als het fout gaat
    private void micStateWrong() {
        // Gooi het aantal fouten omhoog
        mistakes++;

        // Progressbar naar beneden halen met een punt
        ProgressbarChanger.down(mProgressBar);

        // Als de ballon status niet min twee is, dan staat de ballon nog niet op het laagste punt
        if(balloonState != -2) {
            // De ballon moet 1 plek omlaag
            BalloonMover.down(balloon, GameActivity.this, balloonState);

            // Verlaag de ballon status
            balloonState--;
        }
    }

    // 
    private void resumeTimer() {
        // Als de GameActivity weer door gaat, moet de GameTimer weer gestart worden.
        // De if-statement staat hier om het bij de eerste keer opstarten goed te laten gaan
        if(timeRemaining != 0) {
            gameTimer = new GameTimer(timeRemaining, 1000);
            gameTimer.setListener(new GameTimer.ChangeListener() {
                @Override
                public void onChange() {
                    Log.d("GAME ACTIVITY", "SESSIE IS KLAAR");
                    onTimerFinish();
                }
            });
            gameTimer.start();
            bga.resume();
        }
    }

    private void onTimerFinish() {
        Intent intent = new Intent(GameActivity.this, EndSessionActivity.class);
        Bundle score_data = new Bundle();
        score_data.putString("score", scoreDisplay.getText().toString());
        score_data.putString("mistakes", Integer.toString(mistakes));
        intent.putExtras(score_data);
        finish();
        startActivity(intent);
    }

    private void createConnection() {
        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }

        // Establish the Bluetooth socket connection.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                //insert code to deal with this
            }
        }

        mConnectedThread = new ConnectedThread(btSocket, bluetoothIn);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x", this, gameTimer);
    }

    private void startBackgroundAnimation() {
        bga = ValueAnimator.ofFloat(1.0f, 0.0f);
        bga.setRepeatCount(ValueAnimator.INFINITE);
        bga.setInterpolator(new LinearInterpolator());
        bga.setDuration(20000L);
        bga.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                float width = background2.getWidth();
                float translationX = width * progress;
                background2.setTranslationX(translationX);
                background1.setTranslationX(translationX - width);
            }
        });

        bga.start();
    }
}