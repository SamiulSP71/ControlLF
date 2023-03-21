package com.example.controllf;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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

    Set<BluetoothDevice> pairedDevices;


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


                if (Build.VERSION.SDK_INT >= 31) {
                    requestPermissions(permissions, 80);
                }
                pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
                if (listView.getVisibility() == View.GONE) {
                    listView.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.GONE);
                }

                ArrayList<String> deviceList = new ArrayList<String>();
                for (BluetoothDevice device : pairedDevices) {
                    deviceList.add(device.getName());

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_view, deviceList);
                    listView.setAdapter(adapter);

                    BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

                    if (Build.VERSION.SDK_INT >= 31) {

                        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
                    } else {
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    }
                    List<BluetoothDevice> connectedDevices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED && connectedDevices.contains(device)) {
                        String deviceName = device.getName();
                        connectbtn.setText("Connected" + deviceName);

                    }


                    if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                        //yo
                    } else {
                        Toast.makeText(getApplicationContext(), "Turn on the Bluetooth", Toast.LENGTH_SHORT).show();
                    }
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

}