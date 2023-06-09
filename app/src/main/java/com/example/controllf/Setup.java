package com.example.controllf;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Method;
import java.util.Set;

public class Setup extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.setup);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
        });

        // Find the radio groups in the layout

        RadioGroup ltGroup = findViewById(R.id.LtGroup);
        RadioGroup fanGroup = findViewById(R.id.FanGroup);

        // Load the saved preferences for each group
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        loadPreferences(ltGroup, prefs.getString("LtGroupPref", ""));
        loadPreferences(fanGroup, prefs.getString("FanGroupPref", ""));

        // Set up listeners for when buttons are checked
        ltGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Save the preferences for this group
            savePreferences(group, "LtGroupPref");
        });
        fanGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Save the preferences for this group
            savePreferences(group, "FanGroupPref");
        });
        updateConnectBtnText();
        TextView connectbtn = findViewById(R.id.connectButton);
        connectbtn.setOnClickListener(v ->{
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                connectbtn.setText("Bluetooth is not supported");
            } else if (!bluetoothAdapter.isEnabled()) {
                connectbtn.setText("Turn On Bluetooth Connection");
            } else {
                BluetoothDevice connectedDevice = getConnectedDevice(bluetoothAdapter);
                if (connectedDevice == null) {
                    connectbtn.setText("No Connected Devices");
                } else {
                    @SuppressLint("MissingPermission") String deviceName = connectedDevice.getName();
                    if (deviceName == null) {
                        deviceName = "Connected an Unknown Named Device";
                    }
                    connectbtn.setText("Connected to: " + deviceName);
                }
            }
        });
    }
    public void updateConnectBtnText() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        TextView connectbtn = findViewById(R.id.connectButton);
        if (bluetoothAdapter == null) {
            connectbtn.setText("Bluetooth is not supported");
        } else if (!bluetoothAdapter.isEnabled()) {
            connectbtn.setText("Turn On Bluetooth Connection");
        } else {
            BluetoothDevice connectedDevice = getConnectedDevice(bluetoothAdapter);
            if (connectedDevice == null) {
                connectbtn.setText("No Connected Devices");
            } else {
                @SuppressLint("MissingPermission") String deviceName = connectedDevice.getName();
                if (deviceName == null) {
                    deviceName = "Connected an Unknown Named Device";
                }
                connectbtn.setText("Connected to: " + deviceName);
            }
        }
    }
    @SuppressLint("MissingPermission")
    private BluetoothDevice getConnectedDevice(BluetoothAdapter bluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getBondState() == BluetoothDevice.BOND_BONDED && isDeviceConnected(device)) {
                return device;
            }
        }
        return null;
    }
    private boolean isDeviceConnected(BluetoothDevice device) {
        try {
            Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected");
            isConnectedMethod.setAccessible(true);
            return (boolean) isConnectedMethod.invoke(device);
        } catch (Exception e) {
            // Handle any exceptions here
        }
        return false;
    }
    // Helper method to load saved preferences for a given group
    private void loadPreferences(RadioGroup group, String prefString) {
        if (!prefString.isEmpty()) {
            String[] parts = prefString.split(",");
            for (String part : parts) {
                RadioButton button = group.findViewById(Integer.parseInt(part));
                button.setChecked(true);

            }
        }
    }
    // Helper method to save preferences for a given group
    private void savePreferences(RadioGroup group, String prefName) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder prefValue = new StringBuilder();
        for (int i=0; i<group.getChildCount(); i++) {
            RadioButton button = (RadioButton) group.getChildAt(i);
            if (button.isChecked()) {
                prefValue.append(button.getId()).append(",");
            }
        }
        editor.putString(prefName, prefValue.toString());
        editor.apply();
    }

}