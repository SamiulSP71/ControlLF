package com.example.controllf;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
public class Setup extends AppCompatActivity {

    String[] permissions = {"android.permission.BLUETOOTH","android.permission.BLUETOOTH_ADMIN",
            "android.permission.BLUETOOTH_CONNECT","android.permission.BLUETOOTH_SCAN"};
    BottomNavigationView bottomNavigationView;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.setup);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.setup:
                                return true;
                            case R.id.control:
                                startActivity(new Intent(getApplicationContext(), Control.class));
                                finish();
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.more:
                                startActivity(new Intent(getApplicationContext(), More.class));
                                finish();
                                overridePendingTransition(0, 0);
                                return true;
                        }
                        return false;
                    }
                });

        RadioGroup LtGroup = findViewById(R.id.LtGroup);
        RadioGroup FanGroup = findViewById(R.id.FanGroup);

        ListView listView = findViewById(R.id.blueList);
        TextView connectbtn = findViewById(R.id.connectButton);
        // Add a listener to the radio group
        LtGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                String selectedText = radioButton.getText().toString();

                // Save the selected value to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("LtBtnGroup", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                RadioButton selectedRadioButton = findViewById(checkedId);
                editor.putString("selected_value", selectedRadioButton.getText().toString());
                editor.apply();

            }
        });
        FanGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                String selectedText = radioButton.getText().toString();

                // Save the selected value to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("FanBtnGroup", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                RadioButton selectedRadioButton = findViewById(checkedId);
                editor.putString("selected_value", selectedRadioButton.getText().toString());
                editor.apply();
            }
        });
        connectbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                // TODO: Add code to get the list of available Bluetooth devices

                if (Build.VERSION.SDK_INT >= 24) {
                    requestPermissions(permissions, 80);
                } else {
                    //s
                }

                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
// Check if Bluetooth is enabled
                if (bluetoothAdapter.isEnabled()) {
                    // Get a list of paired (bonded) devices
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    // Find the currently connected device (if there is one)
                    BluetoothDevice connectedDevice = null;
                    for (BluetoothDevice device : pairedDevices) {
                        int state = bluetoothAdapter.getProfileConnectionState(BluetoothGatt.GATT);
                        if (state == BluetoothGatt.STATE_CONNECTED && device.getBondState() == BluetoothDevice.BOND_BONDED) {
                            connectedDevice = device;
                            break;
                        }
                    }

                    // If a connected device was found, update the TextView with its name
                    if (connectedDevice != null) {// check if there is a connected device
                        String deviceName = connectedDevice.getName(); // retrieve device name
                        if (deviceName != null) {
                            // check if device name is not null or empty
                            connectbtn.setText("Connected device: " + deviceName);
                        } else if (deviceName.isEmpty()) {
                            // if device has no name
                            for (BluetoothDevice device : pairedDevices) {
                                String noName = device.getName();
                                connectbtn.setText("Unknown" + noName);
                            }
                        } else {}
                    } else {
                        // if no device connected
                        connectbtn.setText("No device connected.");
                    }
                } else {
                    // Bluetooth is not enabled
                    connectbtn.setText("Bluetooth is not enabled");
                }
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 80)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //yo
            }else {
                Toast.makeText(this,"Not Connected", Toast.LENGTH_SHORT).show();
                requestPermissions(permissions, 80);
            }
        }
    }

    private boolean isConnected(BluetoothDevice device) {
        int connectionState = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.GATT);
        return connectionState == BluetoothProfile.STATE_CONNECTED;
    }

}