package com.example.controllf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.more);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.setup:
                    startActivity(new Intent(getApplicationContext(), Setup.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
                case R.id.control:
                    startActivity(new Intent(getApplicationContext(), Control.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
                case R.id.more:
                    return true;
            }
            return false;
        });

    }
}