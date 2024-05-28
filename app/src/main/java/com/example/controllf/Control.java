package com.example.controllf;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

public class Control extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private TextView btnConnect;

    private static final String TAG = "Control";
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private static final String DEVICE_NAME = "HC-05";
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice hc05Device;
    private OutputStream outputStream;

    private boolean isConnected = false;


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
        RelativeLayout light3Part = findViewById(R.id.Cn_Light_3_Part);
        RelativeLayout light4Part = findViewById(R.id.Cn_Light_4_Part);
        RelativeLayout light5Part = findViewById(R.id.Cn_Light_5_Part);
        RelativeLayout light6Part = findViewById(R.id.Cn_Light_6_Part);
        RelativeLayout light7Part = findViewById(R.id.Cn_Light_7_Part);

        RelativeLayout fan1Part = findViewById(R.id.Cn_Fan_1_Part);
        RelativeLayout fan2Part = findViewById(R.id.Cn_Fan_2_Part);
        RelativeLayout fan3Part = findViewById(R.id.Cn_Fan_3_Part);
        RelativeLayout fan4Part = findViewById(R.id.Cn_Fan_4_Part);
        RelativeLayout fan5Part = findViewById(R.id.Cn_Fan_5_Part);

        // Set class for drawables from text view of lights and fans
        TextView cn_lt_1_txt = findViewById(R.id.Cn_Light_1_Text);
        TextView cn_lt_2_txt = findViewById(R.id.Cn_Light_2_Text);
        TextView cn_lt_3_txt = findViewById(R.id.Cn_Light_3_Text);
        TextView cn_lt_4_txt = findViewById(R.id.Cn_Light_4_Text);
        TextView cn_lt_5_txt = findViewById(R.id.Cn_Light_5_Text);
        TextView cn_lt_6_txt = findViewById(R.id.Cn_Light_6_Text);
        TextView cn_lt_7_txt = findViewById(R.id.Cn_Light_7_Text);

        TextView cn_fan_1_txt = findViewById(R.id.Cn_Fan_1_Text);
        TextView cn_fan_2_txt = findViewById(R.id.Cn_Fan_2_Text);
        TextView cn_fan_3_txt = findViewById(R.id.Cn_Fan_3_Text);
        TextView cn_fan_4_txt = findViewById(R.id.Cn_Fan_4_Text);
        TextView cn_fan_5_txt = findViewById(R.id.Cn_Fan_5_Text);

        TextView fan1SeekValue = findViewById(R.id.fan1SeekValue);
        TextView fan2SeekValue = findViewById(R.id.fan2SeekValue);
        TextView fan3SeekValue = findViewById(R.id.fan3SeekValue);
        TextView fan4SeekValue = findViewById(R.id.fan4SeekValue);
        TextView fan5SeekValue = findViewById(R.id.fan5SeekValue);

        // Set class for lights and fans toggle buttons
        ToggleButton lt1Toggle = findViewById(R.id.Cn_LtBtn1);
        ToggleButton lt2Toggle = findViewById(R.id.Cn_LtBtn2);
        ToggleButton lt3Toggle = findViewById(R.id.Cn_LtBtn3);
        ToggleButton lt4Toggle = findViewById(R.id.Cn_LtBtn4);
        ToggleButton lt5Toggle = findViewById(R.id.Cn_LtBtn5);
        ToggleButton lt6Toggle = findViewById(R.id.Cn_LtBtn6);
        ToggleButton lt7Toggle = findViewById(R.id.Cn_LtBtn7);

        ToggleButton fan1Toggle = findViewById(R.id.Cn_FanBtn1);
        ToggleButton fan2Toggle = findViewById(R.id.Cn_FanBtn2);
        ToggleButton fan3Toggle = findViewById(R.id.Cn_FanBtn3);
        ToggleButton fan4Toggle = findViewById(R.id.Cn_FanBtn4);
        ToggleButton fan5Toggle = findViewById(R.id.Cn_FanBtn5);

        // Set class for seekbars of fans
        SeekBar fan1SeekBar = findViewById(R.id.seekb_fan1);
        SeekBar fan2SeekBar = findViewById(R.id.seekb_fan2);
        SeekBar fan3SeekBar = findViewById(R.id.seekb_fan3);
        SeekBar fan4SeekBar = findViewById(R.id.seekb_fan4);
        SeekBar fan5SeekBar = findViewById(R.id.seekb_fan5);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        connectToHC05();
        btnConnect = findViewById(R.id.BtnConnect);
        btnConnect.setOnClickListener(v -> {
            if (!isConnected) {
                connectToHC05();
            }
        });

        // Load the saved preferences for each group
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int ltPrefIndex = prefs.getInt("LtGroupPref", -1);
        int fanPrefIndex = prefs.getInt("FanGroupPref", -1);

        // Show or hide the views based on saved value from setup
        adjustLightVisibility(ltPrefIndex, light1Part, light2Part, light3Part, light4Part, light5Part, light6Part, light7Part);
        adjustFanVisibility(fanPrefIndex, fan1Part, fan2Part,
                fan3Part, fan4Part, fan5Part);




        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Set initial state of toggle buttons, drawable colors and seekbars, based on saved value
        boolean isLt1On = sharedPreferences.getBoolean("isLt1On", false);
        boolean isLt2On = sharedPreferences.getBoolean("isLt2On", false);
        boolean isLt3On = sharedPreferences.getBoolean("isLt3On", false);
        boolean isLt4On = sharedPreferences.getBoolean("isLt4On", false);
        boolean isLt5On = sharedPreferences.getBoolean("isLt5On", false);
        boolean isLt6On = sharedPreferences.getBoolean("isLt6On", false);
        boolean isLt7On = sharedPreferences.getBoolean("isLt7On", false);

        boolean isFan1On = sharedPreferences.getBoolean("isFan1On", false);
        boolean isFan2On = sharedPreferences.getBoolean("isFan2On", false);
        boolean isFan3On = sharedPreferences.getBoolean("isFan3On", false);
        boolean isFan4On = sharedPreferences.getBoolean("isFan4On", false);
        boolean isFan5On = sharedPreferences.getBoolean("isFan5On", false);

        int fan1Progress = sharedPreferences.getInt("fan1Progress", 0);
        int fan2Progress = sharedPreferences.getInt("fan2Progress", 0);
        int fan3Progress = sharedPreferences.getInt("fan3Progress", 0);
        int fan4Progress = sharedPreferences.getInt("fan4Progress", 0);
        int fan5Progress = sharedPreferences.getInt("fan5Progress", 0);

        lt1Toggle.setChecked(isLt1On);
        lt2Toggle.setChecked(isLt2On);
        lt3Toggle.setChecked(isLt3On);
        lt4Toggle.setChecked(isLt4On);
        lt5Toggle.setChecked(isLt5On);
        lt6Toggle.setChecked(isLt6On);
        lt7Toggle.setChecked(isLt7On);

        fan1Toggle.setChecked(isFan1On);
        fan2Toggle.setChecked(isFan2On);
        fan3Toggle.setChecked(isFan3On);
        fan4Toggle.setChecked(isFan4On);
        fan5Toggle.setChecked(isFan5On);

        fan1SeekBar.setProgress(fan1Progress);
        fan2SeekBar.setProgress(fan2Progress);
        fan3SeekBar.setProgress(fan3Progress);
        fan4SeekBar.setProgress(fan4Progress);
        fan5SeekBar.setProgress(fan5Progress);

        fan1SeekBar.setEnabled(isFan1On); // Enable or disable SeekBar based on ToggleButton state
        fan2SeekBar.setEnabled(isFan2On);
        fan3SeekBar.setEnabled(isFan3On);
        fan4SeekBar.setEnabled(isFan4On);
        fan5SeekBar.setEnabled(isFan5On);

        fan1SeekValue.setText(String.valueOf(fan1Progress));
        fan2SeekValue.setText(String.valueOf(fan2Progress));
        fan3SeekValue.setText(String.valueOf(fan3Progress));
        fan4SeekValue.setText(String.valueOf(fan4Progress));
        fan5SeekValue.setText(String.valueOf(fan5Progress));

        cn_lt_1_txt.setCompoundDrawablesWithIntrinsicBounds(isLt1On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);
        cn_lt_2_txt.setCompoundDrawablesWithIntrinsicBounds(isLt2On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);
        cn_lt_3_txt.setCompoundDrawablesWithIntrinsicBounds(isLt3On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);
        cn_lt_4_txt.setCompoundDrawablesWithIntrinsicBounds(isLt4On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);
        cn_lt_5_txt.setCompoundDrawablesWithIntrinsicBounds(isLt5On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);
        cn_lt_6_txt.setCompoundDrawablesWithIntrinsicBounds(isLt6On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);
        cn_lt_7_txt.setCompoundDrawablesWithIntrinsicBounds(isLt6On ? R.drawable.light_white : R.drawable.light, 0, 0, 0);

        cn_fan_1_txt.setCompoundDrawablesWithIntrinsicBounds(isFan1On ? R.drawable.fan_white : R.drawable.fan, 0, 0, 0);
        cn_fan_2_txt.setCompoundDrawablesWithIntrinsicBounds(isFan2On ? R.drawable.fan_white : R.drawable.fan, 0, 0, 0);
        cn_fan_3_txt.setCompoundDrawablesWithIntrinsicBounds(isFan3On ? R.drawable.fan_white : R.drawable.fan, 0, 0, 0);
        cn_fan_4_txt.setCompoundDrawablesWithIntrinsicBounds(isFan4On ? R.drawable.fan_white : R.drawable.fan, 0, 0, 0);
        cn_fan_5_txt.setCompoundDrawablesWithIntrinsicBounds(isFan5On ? R.drawable.fan_white : R.drawable.fan, 0, 0, 0);

        // Add Listener to toggle buttons and seekbar, also change the color of drawable
        lt1Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt1On", isChecked);
            editor.apply();
            String message = isChecked ? "L11" : "L10";
            sendBluetoothData(message);
            cn_lt_1_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);

        });
        lt2Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt2On", isChecked);
            editor.apply();
            String message = isChecked ? "L21" : "L20";
            sendBluetoothData(message);
            cn_lt_2_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);

        });
        lt3Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt3On", isChecked);
            editor.apply();
            String message = isChecked ? "L31" : "L30";
            sendBluetoothData(message);
            cn_lt_3_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);

        });
        lt4Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt4On", isChecked);
            editor.apply();
            String message = isChecked ? "L41" : "L40";
            sendBluetoothData(message);
            cn_lt_4_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);

        });
        lt5Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt5On", isChecked);
            editor.apply();
            String message = isChecked ? "L51" : "L50";
            sendBluetoothData(message);
            cn_lt_5_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);

        });
        lt6Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt6On", isChecked);
            editor.apply();
            String message = isChecked ? "L61" : "L60";
            sendBluetoothData(message);
            cn_lt_6_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);

        });
        lt7Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLt7On", isChecked);
            editor.apply();
            String message = isChecked ? "L71" : "L70";
            sendBluetoothData(message);
            cn_lt_7_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.light_white : R.drawable.light,
                    0, 0, 0);

        });

        fan1Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan1On", isChecked);
            editor.apply();
            cn_fan_1_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.fan_white : R.drawable.fan,
                    0, 0, 0);
            String message = isChecked ? "F11" : "F10";
            sendBluetoothData(message);
            fan1SeekBar.setEnabled(isChecked); // Enable or disable seekbar based on toggle state

        });
        fan2Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan2On", isChecked);
            editor.apply();
            String message = isChecked ? "F21" : "F20";
            sendBluetoothData(message);
            cn_fan_2_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.fan_white : R.drawable.fan,
                    0, 0, 0);
            fan2SeekBar.setEnabled(isChecked);

        });
        fan3Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan3On", isChecked);
            editor.apply();
            String message = isChecked ? "F31" : "F30";
            sendBluetoothData(message);
            cn_fan_3_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.fan_white : R.drawable.fan,
                    0, 0, 0);
            fan3SeekBar.setEnabled(isChecked);

        });
        fan4Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan4On", isChecked);
            editor.apply();
            String message = isChecked ? "F41" : "F40";
            sendBluetoothData(message);
            cn_fan_4_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.fan_white : R.drawable.fan,
                    0, 0, 0);
            fan4SeekBar.setEnabled(isChecked);

        });
        fan5Toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFan4On", isChecked);
            editor.apply();
            String message = isChecked ? "F51" : "F50";
            sendBluetoothData(message);
            cn_fan_5_txt.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.fan_white : R.drawable.fan,
                    0, 0, 0);
            fan5SeekBar.setEnabled(isChecked);

        });

        fan1SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("fan1Progress", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Ensure value is sent when tracking stops

                if (fan1Toggle.isChecked()) {
                    int progress = seekBar.getProgress();
                    String message = "F1" + (progress + 2); // AG3 to AG12
                    sendBluetoothData(message);
                    fan1SeekValue.setText(String.valueOf(progress));
                }
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

                if (fan2Toggle.isChecked()) {
                    int progress = seekBar.getProgress();
                    String message = "F2" + (progress + 2);
                    sendBluetoothData(message);
                    fan2SeekValue.setText(String.valueOf(progress));
                }
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
                if (fan3Toggle.isChecked()) {
                    int progress = seekBar.getProgress();
                    String message = "F3" + (progress + 2);
                    sendBluetoothData(message);
                    fan3SeekValue.setText(String.valueOf(progress));
                }
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
                if (fan4Toggle.isChecked()) {
                    int progress = seekBar.getProgress();
                    String message = "F4" + (progress + 2);
                    sendBluetoothData(message);
                    fan4SeekValue.setText(String.valueOf(progress));
                }
            }
        });
        fan5SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                if (fan5Toggle.isChecked()) {
                    int progress = seekBar.getProgress();
                    String message = "F5" + (progress + 2);
                    sendBluetoothData(message);
                    fan5SeekValue.setText(String.valueOf(progress));
                }
            }
        });


    }
    private void adjustLightVisibility(int index, RelativeLayout light1, RelativeLayout light2,
                                       RelativeLayout light3, RelativeLayout light4, RelativeLayout light5,
                                       RelativeLayout light6, RelativeLayout light7) {
        if (index == -1) return;

        light1.setVisibility(View.GONE);
        light2.setVisibility(View.GONE);
        light3.setVisibility(View.GONE);
        light4.setVisibility(View.GONE);
        light5.setVisibility(View.GONE);
        light6.setVisibility(View.GONE);
        light7.setVisibility(View.GONE);

        switch (index) {
            case 0:
                light1.setVisibility(View.VISIBLE);
                break;
            case 1:
                light1.setVisibility(View.VISIBLE);
                light2.setVisibility(View.VISIBLE);
                break;
            case 2:
                light1.setVisibility(View.VISIBLE);
                light2.setVisibility(View.VISIBLE);
                light3.setVisibility(View.VISIBLE);
                break;
            case 3:
                light1.setVisibility(View.VISIBLE);
                light2.setVisibility(View.VISIBLE);
                light3.setVisibility(View.VISIBLE);
                light4.setVisibility(View.VISIBLE);
                break;
            case 4:
                light1.setVisibility(View.VISIBLE);
                light2.setVisibility(View.VISIBLE);
                light3.setVisibility(View.VISIBLE);
                light4.setVisibility(View.VISIBLE);
                light5.setVisibility(View.VISIBLE);
                break;
            case 5:
                light1.setVisibility(View.VISIBLE);
                light2.setVisibility(View.VISIBLE);
                light3.setVisibility(View.VISIBLE);
                light4.setVisibility(View.VISIBLE);
                light5.setVisibility(View.VISIBLE);
                light6.setVisibility(View.VISIBLE);
                break;
            case 6:
                light1.setVisibility(View.VISIBLE);
                light2.setVisibility(View.VISIBLE);
                light3.setVisibility(View.VISIBLE);
                light4.setVisibility(View.VISIBLE);
                light5.setVisibility(View.VISIBLE);
                light6.setVisibility(View.VISIBLE);
                light7.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void adjustFanVisibility(int index, RelativeLayout fan1, RelativeLayout fan2, RelativeLayout fan3,
                                     RelativeLayout fan4, RelativeLayout fan5) {
        if (index == -1) return;

        fan1.setVisibility(View.GONE);
        fan2.setVisibility(View.GONE);
        fan3.setVisibility(View.GONE);
        fan4.setVisibility(View.GONE);
        fan5.setVisibility(View.GONE);

        switch (index) {
            case 0:
                fan1.setVisibility(View.VISIBLE);
                break;
            case 1:
                fan1.setVisibility(View.VISIBLE);
                fan2.setVisibility(View.VISIBLE);
                break;
            case 2:
                fan1.setVisibility(View.VISIBLE);
                fan2.setVisibility(View.VISIBLE);
                fan3.setVisibility(View.VISIBLE);
                break;
            case 3:
                fan1.setVisibility(View.VISIBLE);
                fan2.setVisibility(View.VISIBLE);
                fan3.setVisibility(View.VISIBLE);
                fan4.setVisibility(View.VISIBLE);
                break;
            case 4:
                fan1.setVisibility(View.VISIBLE);
                fan2.setVisibility(View.VISIBLE);
                fan3.setVisibility(View.VISIBLE);
                fan4.setVisibility(View.VISIBLE);
                fan5.setVisibility(View.VISIBLE);
                break;
        }
    }


    @SuppressLint("SetTextI18n")
    private void connectToHC05() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasBluetoothPermissions()) {
                requestBluetoothPermissions();
                return;
            }
        }

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
                    runOnUiThread(() -> btnConnect.setText("Connected to ControlLF"));
                    isConnected = true;
                } catch (IOException e) {
                    runOnUiThread(() -> btnConnect.setText("Not Connected"));
                    e.printStackTrace();
                    isConnected = false;
                }
            }).start();
        } else {
            runOnUiThread(() -> btnConnect.setText("Not Connected"));
            isConnected = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private boolean hasBluetoothPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
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