package com.example.controllf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import java.util.Arrays;


public class Loading extends AppCompatActivity {
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean hasPermission = sharedPreferences.getBoolean("hasBluetoothPermission", false);

        if (hasPermission) {
            redirectToMainActivity();
        } else {
            requestBluetoothPermission();
        }
    }

    private void requestBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(Loading.this,
                Arrays.toString(new String[]{"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"}))
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Loading.this,
                    new String[]{"android.permission.BLUETOOTH_ADMIN","android.permission.BLUETOOTH"},
                    REQUEST_BLUETOOTH_PERMISSION);
        }
    }

    private void redirectToMainActivity() {
        // Create a handler to delay the redirection
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(Loading.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }, 1000); // Delay for 1 second (adjust as needed)
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sharedPreferences.edit().putBoolean("hasBluetoothPermission", true).apply();
                redirectToMainActivity();
            } else {
                // Permission denied, try again
                requestBluetoothPermission();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}


