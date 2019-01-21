package com.example.echo.balloonbuddy;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Handler bluetoothIn;

    // Creatie van de ConnectedThread
    // Heeft bij het aanmaken een socket en handler nodig
    public ConnectedThread(BluetoothSocket socket, Handler bluetoothIn) {
        // Defineer streams
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Zet de bluetooth handler gelijk aan de handler die actief is in de Game Activity
        this.bluetoothIn = bluetoothIn;

        // Maak I/O streams voor connectie
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        // Maak de streams available door heel de classe
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    // Methode om te luisteren naar nieuwe berichten
    public void run() {
        byte[] buffer = new byte[512];
        int bytes;

        // Blijft loopen voor ontvangen berichten
        while (true) {
            try {
                // Lees bytes van de input buffer
                bytes = mmInStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);

                // Verstuur de verkregen bytes naar de Game Activity via een handler
                bluetoothIn.obtainMessage(0, bytes, -1, readMessage).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }

    // Test connectie methode
    // Om te testen heeft het input, de UI activity en de game timer nodig
    public void write(String input, AppCompatActivity gameActivity, GameTimer gameTimer) {
        // Gooit ingevoerde String om naar bytes
        byte[] msgBuffer = input.getBytes();

        // Schrijft bytes over bluetooth connectie via outstream
        try {
            mmOutStream.write(msgBuffer);
        } catch (IOException e) {
            // Als schijven niet lukt is de connectie down. Sluit dan af
            gameTimer.cancel();
            gameActivity.finish();
            Toast.makeText(gameActivity.getBaseContext(), "Verbinding met apparaat verbroken", Toast.LENGTH_SHORT).show();
        }
    }
}
