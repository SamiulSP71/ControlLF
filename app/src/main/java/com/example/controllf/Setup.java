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
    private SharedPreferences prefs;
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


        TextView connectbtn = findViewById(R.id.connectButton);
        RadioGroup LtGroup = findViewById(R.id.LtGroup);
        RadioGroup FanGroup = findViewById(R.id.FanGroup);

        // Retrieve shared preferences
        prefs = getPreferences(Context.MODE_PRIVATE);

        // Set LtGroup buttons' checked state
        for (int i = 1; i <= 4; i++) {
            int buttonId = getResources().getIdentifier("ltButton" + i, "id", getPackageName());
            RadioButton button = findViewById(buttonId);
            button.setChecked(prefs.getBoolean("LtGroupPref_" + i, false));
        }

        // Set FanGroup buttons' checked state
        for (int i = 1; i <= 4; i++) {
            int buttonId = getResources().getIdentifier("fanButton" + i, "id", getPackageName());
            RadioButton button = findViewById(buttonId);
            button.setChecked(prefs.getBoolean("FanGroupPref_" + i, false));
        }

        // Set LtGroup buttons' onCheckedChangeListener
        RadioGroup ltGroup = findViewById(R.id.LtGroup);
        ltGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = findViewById(checkedId);
                int index = ltGroup.indexOfChild(button) + 1;
                prefs.edit().putBoolean("LtGroupPref_" + index, true).apply();
                for (int i = 1; i <= 4; i++) {
                    if (i != index) {
                        prefs.edit().putBoolean("LtGroupPref_" + i, false).apply();
                    }
                }
            }
        });

        // Set FanGroup buttons' onCheckedChangeListener
        RadioGroup fanGroup = findViewById(R.id.FanGroup);
        fanGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = findViewById(checkedId);
                int index = fanGroup.indexOfChild(button) + 1;
                prefs.edit().putBoolean("FanGroupPref_" + index, true).apply();
                for (int i = 1; i <= 4; i++) {
                    if (i != index) {
                        prefs.edit().putBoolean("FanGroupPref_" + i, false).apply();
                    }
                }
            }
        });

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