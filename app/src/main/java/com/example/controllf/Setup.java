package com.example.controllf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Setup extends AppCompatActivity {

BottomNavigationView bottomNavigationView;
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
                    switch (item.getItemId()){
                        case R.id.setup:
                            return true;
                        case R.id.control:
                            startActivity(new Intent(getApplicationContext(), Control.class));
                            finish();
                            overridePendingTransition(0,0);
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

            RadioGroup LtGroup = findViewById(R.id.LtGroup);
            RadioGroup FanGroup = findViewById(R.id.FanGroup);

            // Add a listener to the radio group

            LtGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    String selectedText = radioButton.getText().toString();

                    // Save the selected value to SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    RadioButton selectedRadioButton = findViewById(checkedId);
                    editor.putString("selected_value", selectedRadioButton.getText().toString());
                    editor.apply();

                }
            });





        }
}