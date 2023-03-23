package com.example.controllf;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("ALL")
public class Setup extends AppCompatActivity {

    String[] permissions = {"android.permission.BLUETOOTH","android.permission.BLUETOOTH_ADMIN",
            "android.permission.BLUETOOTH_CONNECT","android.permission.BLUETOOTH_SCAN"};
    BottomNavigationView bottomNavigationView;
    BluetoothAdapter bluetoothAdapter;

    private RadioGroup ltGroup, fanGroup;
    private SharedPreferences ltGroupPrefs, fanGroupPrefs;
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

        ListView listView = findViewById(R.id.blueList);
        TextView connectbtn = findViewById(R.id.connectButton);

        ltGroup = findViewById(R.id.LtGroup);
        fanGroup = findViewById(R.id.FanGroup);

        // Get the SharedPreferences instances
        ltGroupPrefs = getSharedPreferences("LtGroupPrefs", MODE_PRIVATE);
        fanGroupPrefs = getSharedPreferences("FanGroupPrefs", MODE_PRIVATE);

        // Load the saved data from SharedPreferences and set the corresponding radio buttons as checked
        int selectedButtonIdLtGroup = ltGroupPrefs.getInt("selectedButtonId", -1);
        if (selectedButtonIdLtGroup != -1) {
            ltGroup.check(selectedButtonIdLtGroup);
        }
        int selectedButtonIdFanGroup = fanGroupPrefs.getInt("selectedButtonId", -1);
        if (selectedButtonIdFanGroup != -1) {
            fanGroup.check(selectedButtonIdFanGroup);
        }

        // Set a listener to detect when a radio button is checked
        RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Save the selected button's ID to SharedPreferences
                SharedPreferences.Editor editor;
                if (group == ltGroup) {
                    editor = ltGroupPrefs.edit();
                } else {
                    editor = fanGroupPrefs.edit();
                }
                editor.putInt("selectedButtonId", checkedId);
                editor.apply();
            }
        };
        ltGroup.setOnCheckedChangeListener(radioGroupListener);
        fanGroup.setOnCheckedChangeListener(radioGroupListener);

        // Loop through all child views of the RadioGroups to set their click listeners
        for (int i = 0; i < ltGroup.getChildCount(); i++) {
            View view = ltGroup.getChildAt(i);
            if (view instanceof RadioButton) {
                ((RadioButton) view).setClickable(true);
                ((RadioButton) view).setFocusable(true);
            }
        }
        for (int i = 0; i < fanGroup.getChildCount(); i++) {
            View view = fanGroup.getChildAt(i);
            if (view instanceof RadioButton) {
                ((RadioButton) view).setClickable(true);
                ((RadioButton) view).setFocusable(true);
            }
        }
        connectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    connectbtn.setText("Bluetooth not supported");
                } else if (!bluetoothAdapter.isEnabled()) {
                    connectbtn.setText("Turn On Bluetooth Connection");
                } else {
                    BluetoothDevice connectedDevice = getConnectedDevice(bluetoothAdapter);
                    if (connectedDevice == null) {
                        connectbtn.setText("No Connected Device");
                    } else {
                        String deviceName = connectedDevice.getName();
                        if (deviceName == null) {
                            deviceName = "Unknown Named Device Connected";
                        }
                        connectbtn.setText("Connected to: " + deviceName);
                    }
                }
            }
        });
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 80) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //yo
            } else {
                Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
                requestPermissions(permissions, 80);
            }
        }
    }



}