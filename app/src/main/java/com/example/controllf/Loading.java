package com.example.controllf;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;

public class Loading extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String BLUETOOTH_PERMISSION_GRANTED = "bluetoothPermissionGranted";
    private static final int PERMISSION_REQUEST_BLUETOOTH = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    private static final int REQUEST_ENABLE_LOCATION = 3;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkBluetoothPermission();
        } else {
            checkBluetoothAndLocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkBluetoothPermission() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean bluetoothPermissionGranted = prefs.getBoolean(BLUETOOTH_PERMISSION_GRANTED, false);

        if (bluetoothPermissionGranted) {
            checkBluetoothAndLocation();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Allow Permission");
            builder.setMessage("Allow Bluetooth Permission to work the app properly")
                    .setCancelable(false)
                    .setPositiveButton("Allow", (dialog, id) ->
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

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_BLUETOOTH) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(BLUETOOTH_PERMISSION_GRANTED, true);
                editor.apply();
                checkBluetoothAndLocation();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Allow Permission");
                builder.setMessage("Allow Bluetooth Permission to work the app properly")
                        .setCancelable(false)
                        .setPositiveButton("Allow", (dialog, id) ->
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
    }

    private void checkBluetoothAndLocation() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        } else if (!isLocationEnabled()) {
            promptEnableLocation();
        } else {
            proceedToMainActivity();
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void promptEnableLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Location")
                .setMessage("Location services are required for the app to function properly. Please enable Location.")
                .setCancelable(false)
                .setPositiveButton("Enable", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, REQUEST_ENABLE_LOCATION);
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (bluetoothAdapter.isEnabled()) {
                if (!isLocationEnabled()) {
                    promptEnableLocation();
                } else {
                    proceedToMainActivity();
                }
            } else {
                Toast.makeText(this, "Bluetooth is required to use this app", Toast.LENGTH_SHORT).show();
                checkBluetoothAndLocation();
            }
        } else if (requestCode == REQUEST_ENABLE_LOCATION) {
            if (isLocationEnabled()) {
                proceedToMainActivity();
            } else {
                Toast.makeText(this, "Location is required to use this app", Toast.LENGTH_SHORT).show();
                promptEnableLocation();
            }
        }
    }

    private void proceedToMainActivity() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(Loading.this, MainActivity.class));
            finish();
        }, 1500);
    }

}
