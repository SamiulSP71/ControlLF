package com.example.controllf;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.OutputStream;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class Control extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private BluetoothAdapter bluetoothAdapter;
    private TextView btnConnect;

    private static final String TAG = "Control";
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private static final String DEVICE_NAME = "HC-05";
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private BluetoothDevice hc05Device;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.control);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
        });

        RelativeLayout light1Part = findViewById(R.id.Cn_Light_1_Part);
        RelativeLayout light2Part = findViewById(R.id.Cn_Light_2_Part);
        RelativeLayout lightBotPart = findViewById(R.id.Cn_Lights_Bot_Part);
        RelativeLayout light3Part = findViewById(R.id.Cn_Light_3_Part);
        RelativeLayout light4Part = findViewById(R.id.Cn_Light_4_Part);

        RelativeLayout fan1Part = findViewById(R.id.Cn_Fan_1_Part);
        RelativeLayout fan2Part = findViewById(R.id.Cn_Fan_2_Part);
        RelativeLayout fanBotPart = findViewById(R.id.Cn_Fans_Bot_Part);
        RelativeLayout fan3Part = findViewById(R.id.Cn_Fan_3_Part);
        RelativeLayout fan4Part = findViewById(R.id.Cn_Fan_4_Part);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnConnect = findViewById(R.id.BtnConnect);
        btnConnect.setOnClickListener(v -> connectToHC05());

        // Load the saved preferences for each group
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String ltPref = prefs.getString("LtGroupPref", "");
        String fanPref = prefs.getString("FanGroupPref", "");

        // Show or hide the buttons based on saved value from setup
        if (!ltPref.isEmpty()) {
            String[] ltIds = ltPref.split(",");
            for (String idStr : ltIds) {
                int id = Integer.parseInt(idStr);
                if (id == R.id.LtBtn1) {
                    light1Part.setVisibility(View.VISIBLE);
                    light2Part.setVisibility(View.GONE);
                    lightBotPart.setVisibility(View.GONE);

                } else if (id == R.id.LtBtn2) {
                    light1Part.setVisibility(View.VISIBLE);
                    light2Part.setVisibility(View.VISIBLE);
                    lightBotPart.setVisibility(View.GONE);
                } else if (id == R.id.LtBtn3) {
                    light1Part.setVisibility(View.VISIBLE);
                    light2Part.setVisibility(View.VISIBLE);
                    lightBotPart.setVisibility(View.VISIBLE);
                    light3Part.setVisibility(View.VISIBLE);
                    light4Part.setVisibility(View.GONE);
                } else if (id == R.id.LtBtn4) {
                    light1Part.setVisibility(View.VISIBLE);
                    light2Part.setVisibility(View.VISIBLE);
                    lightBotPart.setVisibility(View.VISIBLE);
                    light3Part.setVisibility(View.VISIBLE);
                    light4Part.setVisibility(View.VISIBLE);
                }
            }
        }
        if (!fanPref.isEmpty()) {
            String[] fanIds = fanPref.split(",");
            for (String idStr : fanIds) {
                int id = Integer.parseInt(idStr);
                if (id == R.id.FanBtn1) {
                    fan1Part.setVisibility(View.VISIBLE);
                    fan2Part.setVisibility(View.GONE);
                    fanBotPart.setVisibility(View.GONE);
                } else if (id == R.id.FanBtn2) {
                    fan1Part.setVisibility(View.VISIBLE);
                    fan2Part.setVisibility(View.VISIBLE);
                    fanBotPart.setVisibility(View.GONE);
                } else if (id == R.id.FanBtn3) {
                    fan1Part.setVisibility(View.VISIBLE);
                    fan2Part.setVisibility(View.VISIBLE);
                    fanBotPart.setVisibility(View.VISIBLE);
                    fan3Part.setVisibility(View.VISIBLE);
                    fan4Part.setVisibility(View.GONE);
                } else if (id == R.id.FanBtn4) {
                    fan1Part.setVisibility(View.VISIBLE);
                    fan2Part.setVisibility(View.VISIBLE);
                    fanBotPart.setVisibility(View.VISIBLE);
                    fan3Part.setVisibility(View.VISIBLE);
                    fan4Part.setVisibility(View.VISIBLE);
                }
            }
        }

        // Set class for drawables from text view of lights and fans
        TextView cn_lt_1_txt = findViewById(R.id.Cn_Light_1_Text);
        TextView cn_lt_2_txt = findViewById(R.id.Cn_Light_2_Text);
        TextView cn_lt_3_txt = findViewById(R.id.Cn_Light_3_Text);
        TextView cn_lt_4_txt = findViewById(R.id.Cn_Light_4_Text);

        TextView cn_fan_1_txt = findViewById(R.id.Cn_Fan_1_Text);
        TextView cn_fan_2_txt = findViewById(R.id.Cn_Fan_2_Text);
        TextView cn_fan_3_txt = findViewById(R.id.Cn_Fan_3_Text);
        TextView cn_fan_4_txt = findViewById(R.id.Cn_Fan_4_Text);

        // Set class for lights and fans toggle buttons
        ToggleButton lt1Toggle = findViewById(R.id.Cn_LtBtn1);
        ToggleButton lt2Toggle = findViewById(R.id.Cn_LtBtn2);
        ToggleButton lt3Toggle = findViewById(R.id.Cn_LtBtn3);
        ToggleButton lt4Toggle = findViewById(R.id.Cn_LtBtn4);

        ToggleButton fan1Toggle = findViewById(R.id.Cn_FanBtn1);
        ToggleButton fan2Toggle = findViewById(R.id.Cn_FanBtn2);
        ToggleButton fan3Toggle = findViewById(R.id.Cn_FanBtn3);
        ToggleButton fan4Toggle = findViewById(R.id.Cn_FanBtn4);

        // Set class for seekbars of fans
        SeekBar fan1SeekBar = findViewById(R.id.seekb_fan1);
        SeekBar fan2SeekBar = findViewById(R.id.seekb_fan2);
        SeekBar fan3SeekBar = findViewById(R.id.seekb_fan3);
        SeekBar fan4SeekBar = findViewById(R.id.seekb_fan4);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Set initial state of toggle buttons, drawable colors and seekbars, based on saved value
        boolean isLt1On = sharedPreferences.getBoolean("isLt1On", false);
        boolean isLt2On = sharedPreferences.getBoolean("isLt2On", false);
        boolean isLt3On = sharedPreferences.getBoolean("isLt3On", false);
        boolean isLt4On = sharedPreferences.getBoolean("isLt4On", false);

        boolean isFan1On = sharedPreferences.getBoolean("isFan1On", false);
        boolean isFan2On = sharedPreferences.getBoolean("isFan2On", false);
        boolean isFan3On = sharedPreferences.getBoolean("isFan3On", false);
        boolean isFan4On = sharedPreferences.getBoolean("isFan4On", false);

        int fan1Progress = sharedPreferences.getInt("fan1Progress", 0);
        int fan2Progress = sharedPreferences.getInt("fan2Progress", 0);
        int fan3Progress = sharedPreferences.getInt("fan3Progress", 0);
        int fan4Progress = sharedPreferences.getInt("fan4Progress", 0);

        lt1Toggle.setChecked(isLt1On);
        lt2Toggle.setChecked(isLt2On);
        lt3Toggle.setChecked(isLt3On);
        lt4Toggle.setChecked(isLt4On);

        fan1Toggle.setChecked(isFan1On);
        fan2Toggle.setChecked(isFan2On);
        fan3Toggle.setChecked(isFan3On);
        fan4Toggle.setChecked(isFan4On);

        fan1SeekBar.setProgress(fan1Progress);
        fan2SeekBar.setProgress(fan2Progress);
        fan3SeekBar.setProgress(fan3Progress);
        fan4SeekBar.setProgress(fan4Progress);


        cn_lt_1_txt.setCompoundDrawablesWithIntrinsicBounds(isLt1On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);
        cn_lt_2_txt.setCompoundDrawablesWithIntrinsicBounds(isLt2On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);
        cn_lt_3_txt.setCompoundDrawablesWithIntrinsicBounds(isLt3On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);
        cn_lt_4_txt.setCompoundDrawablesWithIntrinsicBounds(isLt4On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);

        cn_fan_1_txt.setCompoundDrawablesWithIntrinsicBounds(isFan1On ? R.drawable.fan_white : R.drawable.fan, 0, 0, 0);
        cn_fan_2_txt.setCompoundDrawablesWithIntrinsicBounds(isFan2On ? R.drawable.fan_white : R.drawable.fan, 0, 0, 0);
        cn_fan_3_txt.setCompoundDrawablesWithIntrinsicBounds(isFan3On ? R.drawable.fan_white : R.drawable.fan, 0, 0, 0);
        cn_fan_4_txt.setCompoundDrawablesWithIntrinsicBounds(isFan4On ? R.drawable.fan_white : R.drawable.fan, 0, 0, 0);

        // Add Listener to toggle buttons and seekbar, also change the color of drawable
        lt1Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt1On", isChecked);
            editor.apply();
            cn_lt_1_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);
            String message1 = isChecked ? "AA1" : "AA2";
            sendBluetoothData(message1);


        });
        lt2Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt2On", isChecked);
            editor.apply();

            cn_lt_2_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);

        });
        lt3Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt3On", isChecked);
            editor.apply();

            cn_lt_3_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);

        });
        lt4Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt4On", isChecked);
            editor.apply();

            cn_lt_4_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);

        });

        fan1Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan1On", isChecked);
            editor.apply();
            cn_fan_1_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.fan_white : R.drawable.fan,
                    0, 0, 0);

        });
        fan2Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan2On", isChecked);
            editor.apply();
            cn_fan_2_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.fan_white : R.drawable.fan,
                    0, 0, 0);


        });
        fan3Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan3On", isChecked);
            editor.apply();
            cn_fan_3_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.fan_white : R.drawable.fan,
                    0, 0, 0);


        });
        fan4Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan4On", isChecked);
            editor.apply();
            cn_fan_4_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.fan_white : R.drawable.fan,
                    0, 0, 0);


        });

        fan1SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fan1Progress", progress);
                editor.apply();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        fan2SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fan2Progress", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        fan3SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fan3Progress", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        fan4SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fan4Progress", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }

    private void connectToHC05() {
        if (!hasBluetoothPermissions()) {
            requestBluetoothPermissions();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices != null && !pairedDevices.isEmpty()) {
            for (BluetoothDevice device : pairedDevices) {
                if (DEVICE_NAME.equals(device.getName())) {
                    hc05Device = device;
                    break;
                }
            }
        }

        if (hc05Device != null) {
            new Thread(() -> {
                try {
                    bluetoothSocket = createBluetoothSocket(hc05Device);
                    bluetoothSocket.connect();
                    outputStream = bluetoothSocket.getOutputStream();
                    runOnUiThread(() -> btnConnect.setText("Connected to HC-05"));
                } catch (IOException e) {
                    runOnUiThread(() -> btnConnect.setText("Not Connected"));
                    e.printStackTrace();
                }
            }).start();
        } else {
            runOnUiThread(() -> btnConnect.setText("Not Connected"));
        }
    }

    private boolean hasBluetoothPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestBluetoothPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.BLUETOOTH_CONNECT, android.Manifest.permission.BLUETOOTH_SCAN},
                REQUEST_BLUETOOTH_PERMISSIONS);
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            Method m = device.getClass().getMethod("createRfcommSocket", int.class);
            return (BluetoothSocket) m.invoke(device, 1);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IOException("Could not create RFComm socket", e);
        }
    }

    private void sendBluetoothData(String data) {
        if (outputStream != null) {
            try {
                outputStream.write(data.getBytes());
                Log.d(TAG, "Data sent: " + data);
            } catch (IOException e) {
                Log.e(TAG, "Error sending data", e);
            }
        } else {
            Log.e(TAG, "Output stream is null, cannot send data");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, set up Bluetooth
                connectToHC05();
            } else {
                // Permissions denied, show a message to the user
                Toast.makeText(this, "Bluetooth permissions are required to use this app", Toast.LENGTH_LONG).show();
            }
        }
    }
}