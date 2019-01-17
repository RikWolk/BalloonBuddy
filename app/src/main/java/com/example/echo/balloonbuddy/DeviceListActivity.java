package com.example.echo.balloonbuddy;

import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceListActivity extends Activity {
    // Debugging for LOGCAT
    private static final String TAG = "DeviceListActivity";

    Button connectButton;

    private static String address;

    TextView stap1;
    TextView stap2;
    TextView stap3;
    TextView stap4;
    TextView stap5;
    TextView connectieText;

    // EXTRA string to send on to mainactivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        connectButton = (Button) findViewById(R.id.connectButton);
        connectieText = (TextView) findViewById(R.id.connectieText);

        stap1 = (TextView) findViewById(R.id.stap1);
        stap2 = (TextView) findViewById(R.id.stap2);
        stap3 = (TextView) findViewById(R.id.stap3);
        stap4 = (TextView) findViewById(R.id.stap4);
        stap5 = (TextView) findViewById(R.id.stap5);

        stap1.setText("1) Zet BlueTooth op de mobiel aan.");
        stap2.setText("2) Zorg dat BalloonBuddy in de BlueTooth lijst staat.");
        stap3.setText("3) Zet het BalloonBuddy apparaat aan.");
        stap4.setText("4) Wacht tot het groene lampje aanstaat.");
        stap5.setText("5) Start het spel!");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //***************
        checkBTState();

        // Initialize array adapter for paired devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices and append to 'pairedDevices'
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // Add previosuly paired devices to the array
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                //String name = device.getName().toString();
                if(device.getName().toString().contains("BalloonBuddy")){
                    address = device.getAddress();

                    address = address.replaceAll("\\r|\\n", "");

                    connectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            connectieText.setText("Maakt connectie...");
                            connectButton.setEnabled(false);
                            connectButton.setVisibility(TextView.INVISIBLE);
                            connectieText.setVisibility(TextView.VISIBLE);

                            String test = address;

                            Intent i = new Intent(DeviceListActivity.this, GameActivity.class);
                            i.putExtra(EXTRA_DEVICE_ADDRESS, test);
                            startActivity(i);
                        }
                    });
                }
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter=BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if(mBtAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Zorgt dat de BlueTooth wordt aangezet.
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
}

