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
import java.util.Timer;
import java.util.UUID;

public class GameActivity extends AppCompatActivity {

    ImageButton restartButton;
    ImageButton pauseButton;
    TextView scoreDisplay;
    int score = 0;
    int mistakes = 0;

    ImageView background1;
    ImageView background2;
    ImageView balloon;

    int balloonState;

    Handler bluetoothIn;

    ValueAnimator bga;

    public ProgressBar mProgressBar;

    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private String micState;

    public GameTimer gameTimer = new GameTimer(600000, 1000);
    int timeRemaining;

    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        scoreDisplay = (TextView) findViewById(R.id.liveScore);
        scoreDisplay.setText(String.valueOf(score));

        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        restartButton = (ImageButton) findViewById(R.id.restartButton);

        background1 = (ImageView) findViewById(R.id.backgroundImage1);
        background2 = (ImageView) findViewById(R.id.backgroundImage2);
        balloon = (ImageView) findViewById(R.id.ballonImage);

        mProgressBar = findViewById(R.id.progressbar);
        mProgressBar.setProgress(50);

        startBackgroundAnimation();

        gameTimer.start();
        gameTimer.setListener(new GameTimer.ChangeListener() {
            @Override
            public void onChange() {
                Log.d("GAME ACTIVITY", "SESSIE IS KLAAR");
                onTimerFinish();
            }
        });

        //Link the buttons and textViews to respective views
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);

                    int endOfLineIndex = recDataString.indexOf("*");

                    if (endOfLineIndex >= 0) {
                        String mic = recDataString.substring(0, endOfLineIndex);

                        mic = mic.replace("*", "");
                        mic = mic.replaceAll("\\r|\\n", "");

                        micState = mic;

                        if(micState.contains("1")) {
                            micStateGood();
                        }

                        if(micState.contains("2")) {
                            micStateWrong();
                        }

                        recDataString.delete(0, recDataString.length());
                    }
                }
            }
        };

        // Pauze menu
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                gameTimer.cancel();
                mProgressBar.setProgress(50);
                recreate();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        resumeTimer();

        createConnection();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        bga.pause();

        // De GameTimer die gaande is op stop zetten
        gameTimer.cancel();

        try {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    private void micStateGood() {
        score++;
        scoreDisplay.setText(String.valueOf(score));
        Log.d("GAMEACTIVITY", "DIT IS DE EEN");
        ProgressbarChanger.up(mProgressBar);
        if(balloonState != 2) {
            BalloonMover.up(balloon, GameActivity.this, balloonState);
            balloonState++;
        }
    }

    private void micStateWrong() {
        Log.d("GAMEACTIVITY", "DIT IS DE TWEE");
        mistakes++;
        ProgressbarChanger.down(mProgressBar);
        if(balloonState != -2) {
            BalloonMover.down(balloon, GameActivity.this, balloonState);
            balloonState--;
        }
    }

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