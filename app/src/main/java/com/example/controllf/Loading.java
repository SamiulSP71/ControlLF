package com.example.controllf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Loading extends AppCompatActivity {
    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        h.postDelayed(() -> {
            Intent intent = new Intent(Loading.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            finish();
             },1200);

    }
}