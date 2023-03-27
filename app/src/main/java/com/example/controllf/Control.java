package com.example.controllf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Control extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    BluetoothSocket bluetoothSocket;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket socket;
    OutputStream outputStream;
    private SeekBar seekBar;
    private SharedPreferences sharedPreferences;
    private int progress1 = 0, progress2 = 0, progress3 = 0, progress4 = 0;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.control);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setup:
                        startActivity(new Intent(getApplicationContext(), Setup.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.control:
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

//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        // Request user to enable Bluetooth if it's not already enabled
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, 0);
//        }
//
//        // Connect to the paired Bluetooth device
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            // Loop through paired devices until find the correct one
//            for (BluetoothDevice device : pairedDevices) {
//                try {
//                    bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.randomUUID());
//                    bluetoothSocket.connect();
//                    break;
//                } catch (IOException e) {
//                    // Connection failed, try next device
//                }
//            }
//        }else {
//            //
//        }
//
//        try {
//            // Get OutputStream from Bluetooth socket
//            outputStream = bluetoothSocket.getOutputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        // Load the saved preferences for each group
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String ltPref = prefs.getString("LtGroupPref", "");
        String fanPref = prefs.getString("FanGroupPref", "");


        // Set the button names in the TextViews
        if (!ltPref.isEmpty()) {
            String[] ltIds = ltPref.split(",");
            for (String idStr : ltIds) {
                int id = Integer.parseInt(idStr);
                if (id == R.id.LtBtn1) {
                    //
                } else if (id == R.id.LtBtn2) {
                    //
                } else if (id == R.id.LtBtn3) {
                    //
                } else if (id == R.id.LtBtn4) {
                    //
                }
            }
        }
        if (!fanPref.isEmpty()) {
            String[] fanIds = fanPref.split(",");
            for (String idStr : fanIds) {
                int id = Integer.parseInt(idStr);
                if (id == R.id.FanBtn1) {
                    //
                } else if (id == R.id.FanBtn2) {
                    //
                } else if (id == R.id.FanBtn3) {
                    //
                } else if (id == R.id.FanBtn4) {
                    //
                }
            }
        }

        ToggleButton cn_LtBtn1 = findViewById(R.id.Cn_LtBtn1);
        ToggleButton cn_LtBtn2 = findViewById(R.id.Cn_LtBtn2);
        ToggleButton cn_LtBtn3 = findViewById(R.id.Cn_LtBtn3);
        ToggleButton cn_LtBtn4 = findViewById(R.id.Cn_LtBtn4);

        ToggleButton cn_FanBtn1 = findViewById(R.id.Cn_FanBtn1);
        ToggleButton cn_FanBtn2 = findViewById(R.id.Cn_FanBtn2);
        ToggleButton cn_FanBtn3 = findViewById(R.id.Cn_FanBtn3);
        ToggleButton cn_FanBtn4 = findViewById(R.id.Cn_FanBtn4);

        TextView cn_lt_1_txt= findViewById(R.id.Cn_Light_1_Text);
        TextView cn_lt_2_txt= findViewById(R.id.Cn_Light_2_Text);
        TextView cn_lt_3_txt= findViewById(R.id.Cn_Light_3_Text);
        TextView cn_lt_4_txt= findViewById(R.id.Cn_Light_4_Text);

        TextView cn_fan_1_txt= findViewById(R.id.Cn_Fan_1_Text);
        TextView cn_fan_2_txt= findViewById(R.id.Cn_Fan_2_Text);
        TextView cn_fan_3_txt= findViewById(R.id.Cn_Fan_3_Text);
        TextView cn_fan_4_txt= findViewById(R.id.Cn_Fan_4_Text);

        SeekBar seekBar1 = findViewById(R.id.seekb_fan1);
        SeekBar seekBar2 = findViewById(R.id.seekb_fan2);
        SeekBar seekBar3 = findViewById(R.id.seekb_fan3);
        SeekBar seekBar4 = findViewById(R.id.seekb_fan4);
        
        
        sharedPreferences = getSharedPreferences("ControlPrefs", Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        loadToggleButtonState(cn_LtBtn1, "Cn_Ltbtn1");
        loadToggleButtonState(cn_LtBtn2, "Cn_Ltbtn2");
        loadToggleButtonState(cn_LtBtn3, "Cn_Ltbtn3");
        loadToggleButtonState(cn_LtBtn4, "Cn_Ltbtn4");

        loadToggleButtonState(cn_FanBtn1, "Cn_Fanbtn1");
        loadToggleButtonState(cn_FanBtn2, "Cn_Fanbtn2");
        loadToggleButtonState(cn_FanBtn3, "Cn_Fanbtn3");
        loadToggleButtonState(cn_FanBtn4, "Cn_Fanbtn4");

        progress1 = sharedPreferences.getInt("progress1", 0);
        progress2 = sharedPreferences.getInt("progress2", 0);
        progress3 = sharedPreferences.getInt("progress3", 0);
        progress4 = sharedPreferences.getInt("progress4", 0);

        seekBar1.setProgress(progress1);
        seekBar2.setProgress(progress2);
        seekBar3.setProgress(progress3);
        seekBar4.setProgress(progress4);

        setToggleButtonListener(cn_LtBtn1, cn_lt_1_txt);
        setToggleButtonListener(cn_LtBtn2, cn_lt_2_txt);
        setToggleButtonListener(cn_LtBtn3, cn_lt_3_txt);
        setToggleButtonListener(cn_LtBtn4, cn_lt_4_txt);

        setToggleButtonListener(cn_FanBtn1, cn_fan_1_txt);
        setToggleButtonListener(cn_FanBtn2, cn_fan_2_txt);
        setToggleButtonListener(cn_FanBtn3, cn_fan_3_txt);
        setToggleButtonListener(cn_FanBtn4, cn_fan_4_txt);


        // Set listeners for the SeekBars
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress1 = progress;
                sendProgress(progress1);
                saveProgress(seekBar, "seekBar1_prog");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progress2 = progress;
                sendProgress(progress2);
                saveProgress(seekBar, "seekBar2_prog");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress3 = progress;
                sendProgress(progress3);
                saveProgress(seekBar, "seekBar3_prog");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress4 = progress;
                sendProgress(progress4);
                saveProgress(seekBar, "seekBar4_prog");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    private void setToggleButtonListener(ToggleButton toggleButton, final TextView textView) {
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                saveToggleButtonState((ToggleButton) compoundButton, isChecked);

                if (isChecked) {
                    // Toggle button is ON
                    sendData("A");

                    saveColor(textView, R.color.white);
                } else {
                    // Toggle button is OFF
                    sendData("B");

                    saveColor(textView, R.color.drawcolor);
                }
            }
        });
    }

    private void saveColor(TextView textView, int colorResource) {
        Drawable[] drawables = textView.getCompoundDrawables();
        Drawable startDrawable = drawables[0];
        startDrawable.setTint(getResources().getColor(colorResource));
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(startDrawable, null, null, null);
    }

    private void saveToggleButtonState(ToggleButton toggleButton, boolean isChecked) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(toggleButton.getTag().toString(), isChecked);
        editor.apply();
    }
    private void loadToggleButtonState(ToggleButton toggleButton, String key) {
        boolean isChecked = sharedPreferences.getBoolean(key, false);
        toggleButton.setChecked(isChecked);
    }
    @SuppressLint("MissingPermission")
    private void sendData(String data) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device does not support Bluetooth
            return;
        }

        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            try (BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.randomUUID())) {
                socket.connect();
                socket.getOutputStream().write(data.getBytes());
            } catch (IOException e) {
                // Error occurred while sending data
                e.printStackTrace();
            }
        }
    }
    private void sendProgress(int progress) {
        // Send data to connected Bluetooth device
        String data = Integer.toString(progress);
        try {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveProgress(SeekBar seekBar, String key) {
        getPreferences(MODE_PRIVATE).edit().putInt(key, seekBar.getProgress()).apply();
    }

}