package com.example.controllf;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;


public class Loading extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String BLUETOOTH_PERMISSION_GRANTED = "bluetoothPermissionGranted";
    private static final int PERMISSION_REQUEST_BLUETOOTH = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkBluetoothPermission();
        }
    }

    // Check Bluetooth permission and show dialog box if not granted
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkBluetoothPermission() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean bluetoothPermissionGranted = prefs.getBoolean(BLUETOOTH_PERMISSION_GRANTED, false);

        if (bluetoothPermissionGranted) {
            // Permission already granted, proceed to MainActivity after delay
            new Handler().postDelayed(() -> {
                startActivity(new Intent(Loading.this, MainActivity.class));

                finish();
            }, 1500);
        } else {
            // Permission not granted, show dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Allow Permission");
            builder.setMessage("Allow Bluetooth Permission to work the app properly")
                    .setCancelable(false)
                    .setPositiveButton("Allow", (dialog, id) ->
                            // Request Bluetooth permission
                            ActivityCompat.requestPermissions(Loading.this,
                                    new String[]{Manifest.permission.BLUETOOTH,
                                            Manifest.permission.BLUETOOTH_ADMIN,
                                            Manifest.permission.BLUETOOTH_CONNECT
                                    },
                                    PERMISSION_REQUEST_BLUETOOTH));
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    // Handle permission request result
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_BLUETOOTH: {
                // If request is cancelled, the grantResults array is empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, set flag to true and save in SharedPreferences
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(BLUETOOTH_PERMISSION_GRANTED, true);
                    editor.apply();

                    // Check Bluetooth permission again and start delay if granted
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        checkBluetoothPermission();
                    }
                } else {
                    // Permission denied, show dialog box again
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Allow Permission");
                    builder.setMessage("Allow Bluetooth Permission to work the app properly")
                            .setCancelable(false)
                            .setPositiveButton("Allow", (dialog, id) ->
                                    // Request Bluetooth permission again
                                    ActivityCompat.requestPermissions(Loading.this,
                                            new String[]{Manifest.permission.BLUETOOTH,
                                                    Manifest.permission.BLUETOOTH_ADMIN,
                                                    Manifest.permission.BLUETOOTH_CONNECT
                                                    },
                                            PERMISSION_REQUEST_BLUETOOTH));
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;
            }
        }
    }
}