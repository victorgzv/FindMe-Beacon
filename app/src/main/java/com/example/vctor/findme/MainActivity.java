package com.example.vctor.findme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    //Add a Background Saver so that when the app runs u have don't drain the battery much
    private BackgroundPowerSaver backgroundPowerSaver;
    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private BluetoothLeScanner mBtScanner;
    private BluetoothAdapter mBtAdapter;
    private static final int REQUEST_ENABLE_BT = 3;
   //Array of options-->array adapater--> ListView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initBluetooth();
        populateListView();

        beaconManager = BeaconManager.getInstanceForApplication(this);

        //Below is the profile for iBeacon don't touch it ... by default is altBeacon profile
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        backgroundPowerSaver = new BackgroundPowerSaver(this);


    }

    private void populateListView() {
       //Create list of items
        String[]myItems={"Blue","Green","Purple","Red"};
        String[]myItems2={"58","54","65","34"};
        //Build Adapter
        BeaconAdapter adapter= new BeaconAdapter(this,myItems);
        /*ArrayAdapter<String> adapater = new ArrayAdapter<String>(
                this,               //Context for the activity
                R.layout.beacon_items,   //Layout to use (Create)
                myItems);           //Items to be displayed*/
        //Configure the list view
        ListView list =(ListView)findViewById(R.id.listViewMain);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_bluetooth){
            enableBluetooth();
        }

        return super.onOptionsItemSelected(item);
    }

    void initBluetooth() {
        //the following method is recommended to use for getting BluetoothAdapter
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = bluetoothManager.getAdapter();
        mBtAdapter.enable();
        if (mBtAdapter == null || !mBtAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }
        mBtScanner = mBtAdapter.getBluetoothLeScanner();
    }
    void enableBluetooth(){
        if (!mBtAdapter.isEnabled()) {
            mBtAdapter.enable();
        }else{
            mBtAdapter.disable();
        }
    }


    @Override
    public void onBeaconServiceConnect() {

        //SetMonitorNotifier for Monitoring Function
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        //Range Setting Notifier
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {

                    Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
                    Log.i(TAG, "The RRSI OF THE BEACON IS " + beacons.iterator().next().getRssi() + " RSSI.");
                    Log.i(TAG, "The first beacon I see is having UUID as: " + beacons.iterator().next().getId1()+":"+beacons.iterator().next().getId2()+":"+beacons.iterator().next().getId3() );
                    Log.i(TAG, "UUID IS " + beacons.iterator().next().getServiceUuid() + ".");
                    String uuid= beacons.iterator().next().getId1()+":"+beacons.iterator().next().getId2()+":"+beacons.iterator().next().getId3();
                    double dist=beacons.iterator().next().getDistance();
                    BleItem device= new BleItem(uuid,dist);
                }
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }
}
