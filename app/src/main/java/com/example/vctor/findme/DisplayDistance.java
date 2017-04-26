package com.example.vctor.findme;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import static com.example.vctor.findme.BleItem.getRoundedDistanceString;

import java.util.Collection;
import java.util.Iterator;

public class DisplayDistance extends AppCompatActivity implements BeaconConsumer, RangeNotifier {
    private BluetoothAdapter mBtAdapter;
    private BackgroundPowerSaver backgroundPowerSaver;
    String beaconMac;
    TextView macAddress,distanceBeacon;
    protected static final String TAX = "MonitoringActivity2";
    BeaconManager beaconManager;
    TextView distview;
    String dist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_distance);
        macAddress= (TextView)findViewById(R.id.txtMac);
        beaconMac=getIntent().getStringExtra("MAC");
        macAddress.setText(beaconMac);
        distview= (TextView)findViewById(R.id.txtDistance);
        initBeaconManager();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(3000); // in milliseconds
                        runOnUiThread(new Runnable() { //it has to run on the ui thread  to update the Textview
                            @Override
                            public void run() {
                                distview.setText(dist);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

    }
    private void initBeaconManager() {
        //beaconManager.setBackgroundMode(PreferencesUtil.isBackgroundScan(this));
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        beaconManager.setBackgroundBetweenScanPeriod(PreferencesUtil.getBackgroundScanInterval(this));

        beaconManager.setBackgroundScanPeriod(10000L);          // default is 10000L
        beaconManager.setForegroundBetweenScanPeriod(0L);      // default is 0L
        beaconManager.setForegroundScanPeriod(1100L);          // Default is 1100L

        //mBeaconManager.setMaxTrackingAge(10000);
        //mBeaconManager.setRegionExitPeriod(12000L);

        /*
        RangedBeacon.setMaxTrackingAge() only controls the period of time ranged beacons will continue to be
        returned after the scanning service stops detecting them.
        It has no affect on when monitored regions trigger exits. It is set to 5 seconds by default.

        Monitored regions are exited whenever a scan period finishes and the BeaconManager.setRegionExitPeriod()
        has passed since the last detection.
        By default, this is 10 seconds, but you can customize it.

        Using the defaults, the library will stop sending ranging updates five seconds after a beacon was last seen,
         and then send a region exit 10 seconds after it was last seen.
        You are welcome to change these two settings to meet your needs, but the BeaconManager.setRegionExitPeriod()
        should generally be the same or longer than the RangedBeacon.setMaxTrackingAge().
         */

        backgroundPowerSaver = new BackgroundPowerSaver(this);
        beaconManager.addRangeNotifier(this);

        try {
            if (beaconManager.isAnyConsumerBound()) {
                beaconManager.updateScanPeriods();
            }
        } catch (RemoteException e) {
            //Log.e(Constants.TAG, "update scan periods error", e);
        }
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


    @Override
    public void onBeaconServiceConnect() {

    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Iterator<Beacon> iterator = beacons.iterator();
        while (iterator.hasNext()) {
            Beacon beacon = iterator.next();
            dist= getRoundedDistanceString(beacon.getDistance());



        }
    }
}
