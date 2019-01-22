package com.example.echo.balloonbuddy;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Handler bluetoothIn;
    public static String readMessage;

    private AppCompatActivity ga;
    private GameTimer gt;

    //creation of the connect thread
    public ConnectedThread(BluetoothSocket socket, Handler bluetoothIn) {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        Log.d("THread", "Ik ben levend");
        this.bluetoothIn = bluetoothIn;

        try {
            //Create I/O streams for connection
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
            //Log.d("CONNECTEDTHREAD", "getOutputstuff");
        } catch (IOException e) {
            //Log.d("CONNECTEDTHREAD", "BB is uit ");
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[512];
        int bytes;

        //Log.d("test", "testje");
        // Keep looping to listen for received messages
        while (true) {
            try {

                bytes = mmInStream.read(buffer);            //read bytes from input buffer
                readMessage = new String(buffer, 0, bytes);
                // Send the obtained bytes to the UI Activity via handler

                //Hier moet een timer komen die kijkt of de laatste 10 seconden er een message is binnen  gekomen
                //Zo niet dan wordt de game gepauzeerd en komt er een message dat het apparaat uitstaat.
                //Nieuwe connect knop ala deviceListActivity
                bluetoothIn.obtainMessage(0, bytes, -1, readMessage).sendToTarget();

                //readMessage = null;
            } catch (IOException e) {
            }
        }
    }

    // Test connection method
    public void write(String input, AppCompatActivity gameActivity, GameTimer gameTimer) {
        gt = gameTimer;
        ga = gameActivity;
        byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
        try {
            mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
        } catch (IOException e) {
            //if you cannot write, close the application
            gameTimer.cancel();
            gameActivity.finish();
            Toast.makeText(gameActivity.getBaseContext(), "Kan geen connectie maken.", Toast.LENGTH_LONG).show();
            Log.d("CONNECTEDTHREAD", "HAHA: " + e);
        }
    }
}
