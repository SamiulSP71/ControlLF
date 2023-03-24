package com.example.controllf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Control extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private SharedPreferences prefs;

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
                switch (item.getItemId()){
                    case R.id.setup:
                        startActivity(new Intent(getApplicationContext(), Setup.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.control:
                        return true;
                    case R.id.more:
                        startActivity(new Intent(getApplicationContext(), More.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        // Retrieve shared preferences
        prefs = getPreferences(Context.MODE_PRIVATE);
        // Get LtGroup checked button name and display in TextView
        TextView ltGroupText = findViewById(R.id.ForLtGroup);
        if (prefs.getBoolean("LtGroupPref_1", false)) {
            ltGroupText.setText("Button 1 is checked in LtGroup");
        } else if (prefs.getBoolean("LtGroupPref_2", false)) {
            ltGroupText.setText("Button 2 is checked in LtGroup");
        } else if (prefs.getBoolean("LtGroupPref_3", false)) {
            ltGroupText.setText("Button 3 is checked in LtGroup");
        } else if (prefs.getBoolean("LtGroupPref_4", false)) {
            ltGroupText.setText("Button 4 is checked in LtGroup");
        } else {
            ltGroupText.setText("No buttons are checked in LtGroup");
        }

        // Get FanGroup checked button name and display in TextView
        TextView fanGroupText = findViewById(R.id.ForFanGroup);
        if (prefs.getBoolean("FanGroupPref_1", false)) {
            fanGroupText.setText("Button 1 is checked in FanGroup");
        } else if (prefs.getBoolean("FanGroupPref_2", false)) {
            fanGroupText.setText("Button 2 is checked in FanGroup");
        } else if (prefs.getBoolean("FanGroupPref_3", false)) {
            fanGroupText.setText("Button 3 is checked in FanGroup");
        } else if (prefs.getBoolean("FanGroupPref_4", false)) {
            fanGroupText.setText("Button 4 is checked in FanGroup");
        } else {
            fanGroupText.setText("No buttons are checked in FanGroup");
        }
    }
}