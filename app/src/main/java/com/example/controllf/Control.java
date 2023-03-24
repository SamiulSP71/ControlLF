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
    private TextView ltTextView;
    private TextView fanTextView;
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
    }

}