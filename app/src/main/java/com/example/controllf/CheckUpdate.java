package com.example.controllf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class CheckUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_update);

        WebView webView = findViewById(R.id.Chk_update_wv);
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }
}