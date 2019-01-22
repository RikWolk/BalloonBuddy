package com.example.echo.balloonbuddy;

import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceListActivity extends Activity {
    // Debugging voor LOGCAT
    private static final String TAG = "DeviceListActivity";

    // Aanmaken van variabelen
    private static String address;

    TextView stap1;
    TextView stap2;
    TextView stap3;
    TextView stap4;
    TextView stap5;
    TextView connectieText;

    Button connectButton;

    // String die meegegeven dient te worden naar de GameActivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    // Maak de activity aan
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Text en button voor verbinding
        connectButton = (Button) findViewById(R.id.connectButton);
        connectieText = (TextView) findViewById(R.id.connectieText);

        // Text field met uitleg
        stap1 = (TextView) findViewById(R.id.stap1);
        stap2 = (TextView) findViewById(R.id.stap2);
        stap3 = (TextView) findViewById(R.id.stap3);
        stap4 = (TextView) findViewById(R.id.stap4);
        stap5 = (TextView) findViewById(R.id.stap5);

        // Zet tekst in de fields
        stap1.setText("1) Zet BlueTooth op de mobiel aan.");
        stap2.setText("2) Zorg dat BalloonBuddy in de BlueTooth lijst staat.");
        stap3.setText("3) Zet het BalloonBuddy apparaat aan.");
        stap4.setText("4) Wacht tot het groene lampje aanstaat.");
        stap5.setText("5) Start het spel!");
    }


    // Deze methode gaat van kracht zodra een activity vervolgd wordt
    @Override
    public void onResume()
    {
        super.onResume();

        // Voer een check uit of het mobieltje bluetooth heeft, en of het aanstaat
        checkBTState();

        // Maak een ArrayAdapter waarin alle gepairde apparaten zichtbaar zijn
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Haal de lokale bluetooth adapter op
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Haal de huidige lijst gepaarde apparaten op en voeg dat toe aan pairedDevices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // Check of de lijst gepaarde apparaten heeft
        if (pairedDevices.size() > 0) {
            // Loop door de lijst met apparaten heen
            for (BluetoothDevice device : pairedDevices) {
                // Zoek naar de naam BalloonBuddy
                if(device.getName().toString().contains("BalloonBuddy")) {
                    // Haal het adres op van dat apparaat
                    address = device.getAddress();

                    // Vervang een stuk tekst in het adres
                    address = address.replaceAll("\\r|\\n", "");

                    // Zorg dat de button aangeklikt kan worden
                    connectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Verander de tekst
                            connectieText.setText("Maakt verbinding...");
                            // Verberg de button
                            connectButton.setEnabled(false);
                            connectButton.setVisibility(TextView.INVISIBLE);
                            // Laat de tekst zien
                            connectieText.setVisibility(TextView.VISIBLE);

                            // Rik?
                            String test = address;

                            // Maak een nieuwe activity aan, geef address mee en sluit huidige activity
                            Intent i = new Intent(DeviceListActivity.this, GameActivity.class);
                            i.putExtra(EXTRA_DEVICE_ADDRESS, test);
                            startActivity(i);
                            finish();
                        }
                    });
                }

                // Voeg apparaat toe aan array
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    // Methode om checkts uit te voeren op de bluetooth verbinding
    private void checkBTState() {
        // Check of het apparaat bluetooth heeft
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBtAdapter == null) {
            // Als de mobiel geen bluetooth ondersteunt, laat een toast zien
            Toast.makeText(getBaseContext(), "Uw telefoon ondersteunt geen bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                // Als de bluetooth op de mobiel aan staat, gooi een log
                Log.d(TAG, "Bluetooth AAN");
            } else {
                // Als de bluetooth uit staat, zorg dat de BlueTooth wordt aangezet middels een pop-up
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
}