package com.example.vctor.findme;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;

import java.util.Collection;
import java.util.Iterator;

public class DisplayDistance extends AppCompatActivity   {
    private BluetoothAdapter mBtAdapter;
    String beaconMac;
    TextView macAddress,distanceBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_distance);
        macAddress= (TextView)findViewById(R.id.txtMac);
        beaconMac=getIntent().getStringExtra("MAC");
        macAddress.setText(beaconMac);
        logMsg("Hola");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_bluetooth) {
            enableBluetooth();
        }

        return super.onOptionsItemSelected(item);
    }

    void enableBluetooth() {
        if (!mBtAdapter.isEnabled()) {
            mBtAdapter.enable();
        } else {
            mBtAdapter.disable();
        }
    }
    @UiThread
    public void logMsg(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("UI thread", "I am the UI thread");


            }
        });
    }

}
