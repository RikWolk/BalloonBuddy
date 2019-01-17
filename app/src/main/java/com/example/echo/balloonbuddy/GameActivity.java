package com.example.echo.balloonbuddy;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
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

    boolean recreatePushed = false;

    DataBaseHelper mDatabaseHelper;

    ImageButton restartButton;
    ImageButton pauseButton;
    TextView scoreDisplay;
    int score = 0;

    TextView textView1;

    ImageView balonImage;
    ImageView background1;
    ImageView background2;

    Handler bluetoothIn;

    Handler BallonHandler;


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

    public ProgressBar mProgressBar;
    public int gameState = 0;
    private int mProgressBarStatus = 50;
    private Handler mHandler = new Handler();

    int balonCounter = 1;

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
                Log.d("GAME ACTIVITY", "BAKFIETS");
                onTimerFinish();
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (1 == 1) {
                    if (gameState == 0) {
                        if (endOfLineIndex > 0) {
                            String mic = recDataString.substring(0, endOfLineIndex);

                            //mic1 geeft een 0,1 of 2 terug in string vorm.
                            mic = mic.replace("*", "");
                            mic = mic.replaceAll("\\r|\\n", "");

                            //textView1.setText(mic1);
                            micState = mic;

                            if (micState.contains("0")) {
                                Log.d("GAMEACTIVITY", "DIT IS DE NUL");
                            }

                            if (micState.contains("1")) {
//                            score += 1;
//                            scoreDisplay.setText(String.valueOf(score));
                                Log.d("GAMEACTIVITY", "DIT IS DE EEN");
                            }
                        }

                        if (gameState == 1 && mProgressBarStatus < 100) {
                            if (mProgressBarStatus < 0) {
                                mProgressBarStatus = 0;
                                mProgressBarStatus++;
                            } else {
                                mProgressBarStatus++;
                            }
                        }

                        if (gameState == 2 && mProgressBarStatus > 0) {
                            if (mProgressBarStatus > 100) {
                                mProgressBarStatus = 100;
                                mProgressBarStatus--;
                            } else {
                                mProgressBarStatus--;
                            }
                            //mProgressBarBalance = mProgressBarBalance + score;
                            //scoreBalance = scoreOld - score;
                            SystemClock.sleep(1000);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(mProgressBarStatus);
                                }
                            });
                        }
                    }
                }
            }

            if(micState.contains("2")) {
                Log.d("GAMEACTIVITY", "DIT IS DE TWEE");
//                            if(score == 0) {
//
//                            } else {
//                                score -= 1;
//                                scoreDisplay.setText(String.valueOf(score));
//                            }
            }

            Toast.makeText(getBaseContext(), micState, Toast.LENGTH_SHORT).show();

            recDataString.delete(0, recDataString.length());

        }).start();

        // Herstart de activity
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTimer.cancel();
                recreate();
            }
        });

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
                Log.d("GAME ACTIVITY", "DIT IS DE TIME REMAINING VARIABELE: " + timeRemaining);

                // De GameTimer die gaande is op stop zetten
                gameTimer.cancel();

                // Gooi de pauze activity
                Intent intent = new Intent(GameActivity.this, PauseActivity.class);
                startActivity(intent);
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
=======
                //Link the buttons and textViews to respective views
                bluetoothIn = new Handler() {
                    public void handleMessage(android.os.Message msg) {
                        if (msg.what == handlerState) {

                            String readMessage = (String) msg.obj;
                            recDataString.append(readMessage);

                            int endOfLineIndex = recDataString.indexOf("*");

                            if (endOfLineIndex > 0) {

                                String mic1 = recDataString.substring(0, endOfLineIndex);

                                //mic1 geeft een 0,1 of 2 terug in string vorm.
                                mic1 = mic1.replace("*", "");

                                //textView1.setText(mic1);
                                micState = mic1;

                                if (micState.contains("0")) {
                                    gameState = 0;
                                }

                                if (micState.contains("1")) {
                                    gameState = 1;
                                    score += 1;
                                    scoreDisplay.setText(String.valueOf(score));
                                }

                                if (micState.contains("2")) {
                                    score -= 1;
                                    scoreDisplay.setText(String.valueOf(score));
                                    gameState = 2;
                                }


                                //Toast.makeText(getBaseContext(), micState, Toast.LENGTH_SHORT).show();

                                recDataString.delete(0, recDataString.length());

                            }

                        }

                    }

                };

                btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
                checkBTState();

                scoreDisplay = (TextView) findViewById(R.id.liveScore);
                scoreDisplay.setText(String.valueOf(score));

        BallonHandler = new Handler();
        BallonHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(recreatePushed == true){
                    BallonHandler.removeCallbacksAndMessages(null);
                    Log.d("Test", "recreate activated");
                    recreatePushed = false;
                }

                else if (gameState == 0 && balonCounter == 0) {
                    Toast.makeText(getBaseContext(), "Test state 0 - 0", Toast.LENGTH_SHORT).show();
                    Log.d("Test", "0 - 0");
                    // Geen beweging
                    BallonHandler.postDelayed(this, 6000);
                }

                else if (gameState == 0 && balonCounter == 1) {
                    Toast.makeText(getBaseContext(), "Test state 0 - 1", Toast.LENGTH_SHORT).show();
                    Log.d("Test", "0 - 1");
                    // Geen beweging
                    BallonHandler.postDelayed(this, 6000);
                }

                else if (gameState == 0 && balonCounter == 2) {
                    Toast.makeText(getBaseContext(), "Test state 0 - 2", Toast.LENGTH_SHORT).show();
                    Log.d("Test", "0 - 2");
                    // Geen beweging
                    BallonHandler.postDelayed(this, 6000);
                }

                else if (gameState == 1 && balonCounter == 0) {
                    Toast.makeText(getBaseContext(), "Test state 1 - 0", Toast.LENGTH_SHORT).show();
                    Log.d("Test", "1 - 0");
                    // Van bottom naar midden

>>>>>>> Marius
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
                    BallonHandler.postDelayed(this, 6000);
                }

                else if (gameState == 1 && balonCounter == 1) {
                    Toast.makeText(getBaseContext(), "Test state 1 - 1", Toast.LENGTH_SHORT).show();
                    Log.d("Test", "1 - 1");
                    // Van midden naar top

                    final ImageView balon = (ImageView) findViewById(R.id.balonImage);
                    final ValueAnimator animatorBalon = ValueAnimator.ofFloat(1.00f, 0.0f);
                    animatorBalon.setInterpolator(new LinearInterpolator());
                    animatorBalon.setDuration(5000L);
                    animatorBalon.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            final float progress = (float) animation.getAnimatedValue();
                            final float height = balon.getHeight();
                            final float translationY = height * progress;
                            balon.setTranslationY(translationY );
                            balon.setTranslationY(translationY - height);
                        }
                    });
                    animatorBalon.start();
                    balonCounter += 1;
                    BallonHandler.postDelayed(this, 6000);
                }

                else if (gameState == 1 && balonCounter == 2) {
                    Toast.makeText(getBaseContext(), "Test state 1 - 2", Toast.LENGTH_SHORT).show();
                    Log.d("Test", "1 - 2");
                    // Geen beweging (is al aan top)
                    BallonHandler.postDelayed(this, 6000);
                }

                else if (gameState == 2 && balonCounter == 0) {
                    Toast.makeText(getBaseContext(), "Test state 2 - 0", Toast.LENGTH_SHORT).show();
                    Log.d("Test", "2 - 0");
                    // Geen beweging (is al aan bottom)
                    BallonHandler.postDelayed(this, 6000);
                }

                else if (gameState == 2 && balonCounter == 1) {
                    Toast.makeText(getBaseContext(), "Test state 2 - 1", Toast.LENGTH_SHORT).show();
                    Log.d("Test", "2 - 1");
                    // Van midden naar bottom

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
                    BallonHandler.postDelayed(this, 6000);
                }

                else if (gameState == 2 && balonCounter == 2) {
                    Toast.makeText(getBaseContext(), "Test state 2 - 2", Toast.LENGTH_SHORT).show();
                    Log.d("Test", "2 - 2");
                    // Van top naar midden
                    final ImageView balon = (ImageView) findViewById(R.id.balonImage);
                    final ValueAnimator animatorBalon = ValueAnimator.ofFloat(0.0f, 1.00f);
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
                    BallonHandler.postDelayed(this, 6000);
                }
            }
<<<<<<< HEAD
        });
        */
=======
        }, 1000);

        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        restartButton = (ImageButton) findViewById(R.id.restartButton);

        // Herstart de activity
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreatePushed = true;
                recreate();
                }
        });

        // Repeating background
        final ImageView background1 = (ImageView) findViewById(R.id.backgroundImage1);
        final ImageView background2 = (ImageView) findViewById(R.id.backgroundImage2);

        final ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(20000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = background2.getWidth();
                final float translationX = width * progress;
                background2.setTranslationX(translationX);
                background1.setTranslationX(translationX - width);
            }
        });

        animator.start();

        // Sessie timer
        final int sessieTijd = 120000;

        final CountDownTimer sessieTimer = new CountDownTimer(sessieTijd, 1000) {
            public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish()

                    {
                        Intent intent = new Intent(GameActivity.this, EndSessionActivity.class);
                        recreatePushed = true;
                        Bundle score_data = new Bundle();
                        score_data.putString("score", scoreDisplay.getText().toString());
                        intent.putExtras(score_data);
                        finish();
                        startActivity(intent);
                    }

                }.start();


        // Pauze menu
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    micState = "0";
                    Intent intent = new Intent(GameActivity.this, PauseActivity.class);
                    startActivity(intent);
                    }

                });

            }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();

        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }

    }

}
>>>>>>> Marius
