package com.example.controllf;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class Setup extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private BluetoothAdapter bluetoothAdapter;


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


        // Find your buttons
        RadioButton[] ltButtons = {
                findViewById(R.id.LtBtn1),
                findViewById(R.id.LtBtn2),
                findViewById(R.id.LtBtn3),
                findViewById(R.id.LtBtn4),
                findViewById(R.id.LtBtn5),
                findViewById(R.id.LtBtn6)
        };

        RadioButton[] fanButtons = {
                findViewById(R.id.FanBtn1),
                findViewById(R.id.FanBtn2),
                findViewById(R.id.FanBtn3),
                findViewById(R.id.FanBtn4),
                findViewById(R.id.FanBtn5)
        };

        // Load the saved preferences for each group
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        loadPreferences(ltButtons, prefs.getInt("LtGroupPref", -1));
        loadPreferences(fanButtons, prefs.getInt("FanGroupPref", -1));

        // Set up listeners for when buttons are checked
        setButtonListeners(ltButtons, "LtGroupPref");
        setButtonListeners(fanButtons, "FanGroupPref");
    }

    // Helper method to load saved preferences for a given group
    private void loadPreferences(RadioButton[] buttons, int checkedIndex) {
        if (checkedIndex >= 0 && checkedIndex < buttons.length) {
            buttons[checkedIndex].setChecked(true);
        }
    }

    // Helper method to set listeners for radio buttons
    private void setButtonListeners(RadioButton[] buttons, String prefName) {
        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            buttons[i].setOnClickListener(view -> {
                // Uncheck all other buttons
                for (RadioButton button : buttons) {
                    if (button != buttons[index]) {
                        button.setChecked(false);
                    }
                }
                // Save the preferences
                savePreferences(index, prefName);
            });
        }
    }

    // Helper method to save preferences for a given group
    private void savePreferences(int index, String prefName) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(prefName, index);
        editor.apply();
    }
}