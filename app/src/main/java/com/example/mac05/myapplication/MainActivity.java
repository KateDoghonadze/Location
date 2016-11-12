package com.example.mac05.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int ACCESS_LOCATION_PERMISSION = 1;
    private Button startButton, stopButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        textView = (TextView) findViewById(R.id.textView);

        setButtonStates(LocationService.isInstanceCreated());
    }

    @TargetApi(23)
    private void requestLocationPermission() {
        int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }
    }

    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationService();
                }
            }
        }
    }

    private void startLocationService() {
        if (!LocationService.isInstanceCreated()) {
            startService(new Intent(this, LocationService.class));
            setButtonStates(true);
        }
    }

    public void startLocationUpdates(View view) {
        if (Tools.atLeastMarshmallow()) {
            requestLocationPermission();
        } else {
            startLocationService();
        }
    }

    private void setButtonStates(boolean serviceRunning){
        startButton.setEnabled(!serviceRunning);
        stopButton.setEnabled(serviceRunning);
    }

    public void stopLocationUpdates(View view) {
        if (LocationService.isInstanceCreated()) {
            stopService(new Intent(this, LocationService.class));
            setButtonStates(false);
        }
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (action.equals("com.example.mac05.myapplication.service")) {
                textView.setText(bundle.getFloat("distance")+"");
            }
        }
    };

    @Override
    protected void onStart() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.mac05.myapplication.service");

        registerReceiver(receiver, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {

        unregisterReceiver(receiver);
        super.onStop();
    }
}
