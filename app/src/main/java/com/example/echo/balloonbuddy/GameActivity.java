package com.example.echo.balloonbuddy;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.UUID;

public class GameActivity extends AppCompatActivity {

    boolean resume = true;

    ImageButton restartButton;
    ImageButton pauseButton;
    TextView scoreDisplay;
    int timer = 0;
    int score = 0;

    // Houdt locatie balon bij
    int balonCounter = 1;

    TextView teller;
    int i = 0;

    TextView textView1;
    TextView textView2;
    TextView textView3;

    Button buttonOmhoog;
    Button buttonOmlaag;
    Button buttonOmhoog2;
    Button buttonOmlaag2;

    ImageView balonImage;

    Handler bluetoothIn;

    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    private int aantal = 0;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        buttonOmhoog = (Button) findViewById(R.id.buttonOmhoog);
        buttonOmlaag = (Button) findViewById(R.id.buttonOmlaag);
        buttonOmhoog2 = (Button) findViewById(R.id.buttonOmhoog2);
        buttonOmlaag2 = (Button) findViewById(R.id.buttonOmlaag2);

        balonImage = (ImageView) findViewById(R.id.balonImage);

=======
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        //Link the buttons and textViews to respective views

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);

                    int midOfLineIndex = recDataString.indexOf("&");
                    int endOfLineIndex = recDataString.indexOf("*");

                    if (endOfLineIndex > 0) {

                        recDataString.delete(0, recDataString.length());

                    }

                }
            }

        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        // Balon omhoog van midden
        buttonOmhoog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balonCounter == 1) {
                    final ImageView balon = (ImageView) findViewById(R.id.balonImage);

                    final ValueAnimator animatorBalon = ValueAnimator.ofFloat(0.75f, 0.0f);
                    //animatorBalon.setRepeatCount(0);
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
                    //animatorBalon.setRepeatCount(0);
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
                    //animatorBalon.setRepeatCount(0);
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
                    //animatorBalon.setRepeatCount(0);
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
                        String mic1 = recDataString.substring(0, endOfLineIndex);


                        //mic1 geeft een 0,1 of 2 terug in string vorm.
                        mic1 = mic1.replace("*", "");

                        textView1.setText(mic1);

                        recDataString.delete(0, recDataString.length());

                    }

                }
            }

        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        restartButton = (ImageButton) findViewById(R.id.restartButton);

        // Herstart de activity
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        scoreDisplay = (TextView) findViewById(R.id.liveScore);
        scoreDisplay.setText(String.valueOf(score));

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                // Als state wijzigt cancel?
            }

            public void onFinish() {
                score += 10;
                scoreDisplay.setText(String.valueOf(score));
                start();
            }
        }.start();

        teller = (TextView) findViewById(R.id.liveScore);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                i++;
                teller.setText(String.valueOf(i));
            }
        },10000);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                Intent intent = new Intent(GameActivity.this, PauseActivity.class);
                startActivity(intent);
            }
        });

        // Repeating background bergen

        final ImageView background1 = (ImageView) findViewById(R.id.backgroundImage1);
        final ImageView background2 = (ImageView) findViewById(R.id.backgroundImage2);

        final ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(40000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) { final float progress = (float) animation.getAnimatedValue();
                final float width = background2.getWidth();
                final float translationX = width * progress;
                background2.setTranslationX(translationX);
                background1.setTranslationX(translationX - width);
                }
        });

        // Gras voorground animatie
        final ImageView background_grass1 = (ImageView) findViewById(R.id.backgroundGrassImage1);
        final ImageView background_grass2 = (ImageView) findViewById(R.id.backgroundGrassImage2);

        final ValueAnimator animatorGrass = ValueAnimator.ofFloat(1.0f, 0.0f);
        animatorGrass.setRepeatCount(ValueAnimator.INFINITE);
        animatorGrass.setInterpolator(new LinearInterpolator());
        animatorGrass.setDuration(15000L);
        animatorGrass.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) { final float progress = (float) animation.getAnimatedValue();
                final float width = background_grass2.getWidth();
                final float translationX = width * progress;
                background_grass2.setTranslationX(translationX);
                background_grass1.setTranslationX(translationX - width);
            }
        });

        // Wolken achtegrond animatie
        final ImageView background_clouds1 = (ImageView) findViewById(R.id.backgroundCloudsImage1);
        final ImageView background_clouds2 = (ImageView) findViewById(R.id.backgroundCloudsImage2);

        final ValueAnimator animatorClouds = ValueAnimator.ofFloat(1.0f, 0.0f);
        animatorClouds.setRepeatCount(ValueAnimator.INFINITE);
        animatorClouds.setInterpolator(new LinearInterpolator());
        animatorClouds.setDuration(30000L);
        animatorClouds.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) { final float progress = (float) animation.getAnimatedValue();
                final float width = background_clouds2.getWidth();
                final float translationX = width * progress;
                background_clouds2.setTranslationX(translationX);
                background_clouds1.setTranslationX(translationX - width);
            }
        });

        animator.start();
        animatorGrass.start();
        animatorClouds.start();
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
<<<<<<< HEAD

    }

=======
    }
>>>>>>> Rik
}
