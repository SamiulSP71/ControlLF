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
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton button = (RadioButton) group.getChildAt(i);
            if (button.isChecked()) {
                prefValue.append(button.getId()).append(",");
            }
        }
        editor.putString(prefName, prefValue.toString());
        editor.apply();
    }

}