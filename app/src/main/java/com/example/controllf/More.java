package com.example.controllf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class More extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.more);
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
                        startActivity(new Intent(getApplicationContext(), Control.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.more:
                        return true;
                }
                return false;
            }
        });

        TextView aboutUs = findViewById(R.id.about);
        aboutUs.setOnClickListener(v -> {
            Intent intent = new Intent(More.this, AboutUs.class);
            startActivity(intent);
        });

        TextView chkupdate = findViewById(R.id.update);
        chkupdate.setOnClickListener(v -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://samtoyet.blogspot.com/2024/05/controllf-android-app.html"));
            startActivity(browserIntent);
        });

        TextView chkmproducts = findViewById(R.id.checkpd);
        chkmproducts.setOnClickListener(v -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://samtoyet.blogspot.com/index.html"));
            startActivity(browserIntent);
        });

        RelativeLayout conHelpBox = findViewById(R.id.helpbox);

        TextView cnhelpBtn = findViewById(R.id.help);
        cnhelpBtn.setOnClickListener(view -> {
            if (conHelpBox.getVisibility() == View.GONE) {
                conHelpBox.setVisibility(View.VISIBLE);
            } else {
                conHelpBox.setVisibility(View.GONE);
            }
        });

        TextView teleBtn = findViewById(R.id.telegramBtn);
        teleBtn.setOnClickListener(view ->{
            Uri uri = Uri.parse("https://t.me/+VvTmlrVtGFNlMDQ9");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        TextView whtBtn = findViewById(R.id.whatsappBtn);
        whtBtn.setOnClickListener(view ->{
            Uri uri = Uri.parse("https://chat.whatsapp.com/ITcd2UJNNzA7aBlbOdRnDQ");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        TextView msnBtn = findViewById(R.id.messengerBtn);
        msnBtn.setOnClickListener(view ->{
            Uri uri = Uri.parse("https://m.me/j/AbbeiclbI4cTALuh/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}