package com.example.echo.balloonbuddy;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.Timer;
import java.util.UUID;

public class GameActivity extends AppCompatActivity {

    boolean resume = true;

    DataBaseHelper mDatabaseHelper;

    ImageButton restartButton;
    ImageButton pauseButton;
    TextView scoreDisplay;
    int score = 0;

    TextView textView1;

    // Houdt locatie balon bij
    int balonCounter = 1;

    ImageView balonImage;
    ImageView background1;
    ImageView background2;

    Handler bluetoothIn;
    Handler ScoreCounter;

    ValueAnimator animator;

    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private String micState;
    
    private Timer sessieTimer = new Timer();

    public GameTimer gameTimer = new GameTimer(10000, 1000);
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

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter

        scoreDisplay = (TextView) findViewById(R.id.liveScore);
        scoreDisplay.setText(String.valueOf(score));

        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        restartButton = (ImageButton) findViewById(R.id.restartButton);

        balonImage = (ImageView) findViewById(R.id.balonImage);
        background1 = (ImageView) findViewById(R.id.backgroundImage1);
        background2 = (ImageView) findViewById(R.id.backgroundImage2);

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

                    if (endOfLineIndex > 0) {
                        String mic = recDataString.substring(0, endOfLineIndex);

                        mic = mic.replace("*", "");
                        mic = mic.replaceAll("\\r|\\n", "");

                        micState = mic;

                        if(micState.contains("0")) {
                            Log.d("GAMEACTIVITY", "DIT IS DE NUL");
                        }

                        if(micState.contains("1")) {
                            Log.d("GAMEACTIVITY", "DIT IS DE EEN");
                        }

                        if(micState.contains("2")) {
                            Log.d("GAMEACTIVITY", "DIT IS DE TWEE");
                        }

                        Toast.makeText(getBaseContext(), micState, Toast.LENGTH_SHORT).show();
                        recDataString.delete(0, recDataString.length());
                    }
                }
            }
        };

        animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(20000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                float width = background2.getWidth();
                float translationX = width * progress;
                background2.setTranslationX(translationX);
                background1.setTranslationX(translationX - width);
            }
        });

        animator.start();

        // Pauze menu
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                micState = "0";
                sessieTimer.cancel();

                // De tijd die nog over is ophalen uit de GameTimer en opslaan in de GameActivity
                timeRemaining = gameTimer.returnTimeRmaining();

                // De GameTimer die gaande is op stop zetten
                gameTimer.cancel();

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

        try {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    private void resumeTimer() {
        // Als de GameActivity weer door gaat, moet de GameTimer weer gestart worden.
        // De if-statement staat hier om het bij de eerste keer opstarten goed te laten gaan
        if(timeRemaining != 0) {
            gameTimer = new GameTimer(timeRemaining, 1000);
            gameTimer.start();
        }
    }

    private void onTimerFinish() {
        Intent intent = new Intent(GameActivity.this, EndSessionActivity.class);
        Bundle score_data = new Bundle();
        score_data.putString("score", scoreDisplay.getText().toString());
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
}

/*
        // Balon omhoog van midden
        buttonOmhoog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balonCounter == 1) {
                    final ImageView balon = (ImageView) findViewById(R.id.balonImage);

                    final ValueAnimator animatorBalon = ValueAnimator.ofFloat(0.75f, 0.0f);
                    animatorBalon.setInterpolator(new LinearInterpolator());
                    animatorBalon.setDuration(5000L);
                    animatorBalon.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            final float progress = (float) animation.getAnimatedValue();
                            final float height = balon.getHeight();
                            final float translationY = height * progress;
                            balon.setTranslationY(translationY);
                            balon.setTranslationY(translationY - height);
                        }
                    });

                    animatorBalon.start();
                    balonCounter += 1;
                }

                else{

                }
            }
        });

        // Balon omlaag van top
        buttonOmlaag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balonCounter == 2) {
                    final ImageView balon = (ImageView) findViewById(R.id.balonImage);

                    final ValueAnimator animatorBalon = ValueAnimator.ofFloat(0.0f, 0.75f);
                    animatorBalon.setInterpolator(new LinearInterpolator());
                    animatorBalon.setDuration(5000L);
                    animatorBalon.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            final float progress = (float) animation.getAnimatedValue();
                            final float height = balon.getHeight();
                            final float translationY = height * progress;
                            balon.setTranslationY(translationY);
                            balon.setTranslationY(translationY - height);
                        }
                    });

                    animatorBalon.start();
                    balonCounter -= 1;
                }

                else{

                }
            }
        });

        // Omhoog van bottom
        buttonOmhoog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(balonCounter == 0){

                    final ImageView balon = (ImageView) findViewById(R.id.balonImage);

                    final ValueAnimator animatorBalon = ValueAnimator.ofFloat(0.75f, 0.0f);
                    animatorBalon.setInterpolator(new LinearInterpolator());
                    animatorBalon.setDuration(5000L);
                    animatorBalon.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            final float progress = (float) animation.getAnimatedValue();
                            final float height = balon.getHeight();
                            final float translationY = height * progress;
                            balon.setTranslationY(translationY);
                        }
                    });

                    animatorBalon.start();
                    balonCounter += 1;
                }

                else{

                }
            }
        });

        // Balon omlaag van midden
        buttonOmlaag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balonCounter == 1) {
                    final ImageView balon = (ImageView) findViewById(R.id.balonImage);

                    final ValueAnimator animatorBalon = ValueAnimator.ofFloat(0.0f, 0.75f);
                    animatorBalon.setInterpolator(new LinearInterpolator());
                    animatorBalon.setDuration(5000L);
                    animatorBalon.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            final float progress = (float) animation.getAnimatedValue();
                            final float height = balon.getHeight();
                            final float translationY = height * progress;
                            balon.setTranslationY(translationY);
                        }
                    });

                    animatorBalon.start();
                    balonCounter -= 1;
                }

                else{

                }
            }
        });
        */