package com.example.wifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WifiManager mainWifi;
    private WifiReceiver receiverWifi;
    ListAdapter adapter;
    private Button btnRefresh;
    public List<ScanResult> wifiList;
    ListView lvWifiDetails;

    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    private final int MY_PERMISSIONS_WIFI = 1;

    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvWifiDetails = (ListView) findViewById(R.id.list);
        btnRefresh = (Button) findViewById(R.id.scanBtn);


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, MY_PERMISSIONS_WIFI);
        }
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (!mainWifi.isWifiEnabled())//включен ли wifi
        {//если нет, то включаем
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            mainWifi.setWifiEnabled(true);
        }

        receiverWifi = new WifiReceiver();

        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
       // Log.i(getClass().getSimpleName(), "registered receiver: " + receiverWifi );
        mainWifi.startScan();

        btnRefresh.setOnClickListener(new View.OnClickListener() {//->
            @Override
            public void onClick(View v) {
                mainWifi.startScan();
            }
        });
    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            wifiList = mainWifi.getScanResults();
            setAdapter();
        }
    }

    private void setAdapter() {
        adapter = new ListAdapter(getApplicationContext(), wifiList);
        lvWifiDetails.setAdapter(adapter);
        lvWifiDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                WifiInfo wi = mainWifi.getConnectionInfo();
                int signal = wifiList.get(position).level;
                //String signalText = Integer.toString(signal);
                String signalText = "No signal";
                if (signal >= -50 )
                    signalText = "Excellent";
                else if (signal < -50  && signal >= -60)
                    signalText = "Good";
                else if (signal < -60  && signal >= -70)
                    signalText = "Fair";
                else if (signal == -70)
                    signalText = "Weak";
                int frequency =  wifiList.get(position).frequency;

                String frequencyText = Integer.toString(frequency);
                Intent intentInfo = new Intent(view.getContext(), info.class);
                intentInfo.putExtra("name", wifiList.get(position).SSID);
                intentInfo.putExtra("bssid", wifiList.get(position).BSSID);
                intentInfo.putExtra("signal", signalText);
                intentInfo.putExtra("security", wifiList.get(position).capabilities);
                intentInfo.putExtra("frequency", frequencyText);
                startActivityForResult(intentInfo,1);
            }
            });
    }
}


