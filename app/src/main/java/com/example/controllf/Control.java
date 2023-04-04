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

        ToggleButton lt1Toggle = findViewById(R.id.Cn_LtBtn1);
        ToggleButton lt2Toggle = findViewById(R.id.Cn_LtBtn2);
        ToggleButton lt3Toggle = findViewById(R.id.Cn_LtBtn3);
        ToggleButton lt4Toggle = findViewById(R.id.Cn_LtBtn4);

        ToggleButton fan1Toggle = findViewById(R.id.Cn_FanBtn1);
        ToggleButton fan2Toggle = findViewById(R.id.Cn_FanBtn2);
        ToggleButton fan3Toggle = findViewById(R.id.Cn_FanBtn3);
        ToggleButton fan4Toggle = findViewById(R.id.Cn_FanBtn4);

        TextView cn_lt_1_txt= findViewById(R.id.Cn_Light_1_Text);
        TextView cn_lt_2_txt= findViewById(R.id.Cn_Light_2_Text);
        TextView cn_lt_3_txt= findViewById(R.id.Cn_Light_3_Text);
        TextView cn_lt_4_txt= findViewById(R.id.Cn_Light_4_Text);

        TextView cn_fan_1_txt= findViewById(R.id.Cn_Fan_1_Text);
        TextView cn_fan_2_txt= findViewById(R.id.Cn_Fan_2_Text);
        TextView cn_fan_3_txt= findViewById(R.id.Cn_Fan_3_Text);
        TextView cn_fan_4_txt= findViewById(R.id.Cn_Fan_4_Text);

        SeekBar fan1SeekBar = findViewById(R.id.seekb_fan1);
        SeekBar fan2SeekBar = findViewById(R.id.seekb_fan2);
        SeekBar fan3SeekBar = findViewById(R.id.seekb_fan3);
        SeekBar fan4SeekBar = findViewById(R.id.seekb_fan4);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        boolean isFan1On = sharedPreferences.getBoolean("isFan1On", false);
        boolean isFan2On = sharedPreferences.getBoolean("isFan2On", false);
        boolean isFan3On = sharedPreferences.getBoolean("isFan3On", false);
        boolean isFan4On = sharedPreferences.getBoolean("isFan4On", false);

        fan1Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan1On", isChecked);
            editor.apply();
        });
        fan2Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan2On", isChecked);
            editor.apply();
        });
        fan3Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan3On", isChecked);
            editor.apply();
        });
        fan4Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan4On", isChecked);
            editor.apply();
        });

        fan1Toggle.setChecked(isFan1On);
        fan2Toggle.setChecked(isFan2On);
        fan3Toggle.setChecked(isFan3On);
        fan4Toggle.setChecked(isFan4On);

        int fan1Progress = sharedPreferences.getInt("fan1Progress", 0);
        int fan2Progress = sharedPreferences.getInt("fan2Progress", 0);
        int fan3Progress = sharedPreferences.getInt("fan3Progress", 0);
        int fan4Progress = sharedPreferences.getInt("fan4Progress", 0);

        fan1SeekBar.setProgress(fan1Progress);
        fan2SeekBar.setProgress(fan2Progress);
        fan3SeekBar.setProgress(fan3Progress);
        fan4SeekBar.setProgress(fan4Progress);

        fan1SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fan1Progress", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        fan2SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fan2Progress", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        fan3SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fan3Progress", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        fan4SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fan4Progress", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


    }

    private void saveColor(TextView textView, int colorResource) {
        Drawable[] drawables = textView.getCompoundDrawables();
        Drawable startDrawable = drawables[0];
        startDrawable.setTint(getResources().getColor(colorResource));
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(startDrawable, null, null, null);
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

}