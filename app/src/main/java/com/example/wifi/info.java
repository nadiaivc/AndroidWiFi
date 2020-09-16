package com.example.wifi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class info extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String bssid = intent.getStringExtra("bssid");
        String signal = intent.getStringExtra("signal");
        String security = intent.getStringExtra("security");
        String frequency = intent.getStringExtra("frequency");
        TextView info = findViewById(R.id.infoWifi);
        info.setText("SSID: " + name
                + "\n\nSignal: " + signal
                + "\nBSSID: " + bssid
                + "\nFrequency: " + frequency + " MHz"
                + "\nSecurity: " + security);
    }
}
