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
BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
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

        TextView chkupdate = findViewById(R.id.update);
        chkupdate.setOnClickListener(v -> {
            Intent intent = new Intent(More.this, CheckUpdate.class);
            intent.putExtra("upUrl", "https://tinyurl.com/yc5x7e7c");
            startActivity(intent);
        });

        TextView chkmproducts = findViewById(R.id.checkpd);
        chkmproducts.setOnClickListener(v -> {
            Intent intent = new Intent(More.this, CheckMProduct.class);
            intent.putExtra("prUrl", "https://www.google.com");
            startActivity(intent);
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
            Uri uri = Uri.parse("https://t.me/+GdNsZKQTZ38yYzVl");
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
            Uri uri = Uri.parse("https://m.me/j/AbY4WUpv_Ql0BHBp/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}