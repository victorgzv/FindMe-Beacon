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
import android.widget.AdapterView;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.example.vctor.findme.BleItem.getRoundedDistanceString;

public class MainActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {

    //Add a Background Saver so that when the app runs u have don't drain the battery much
    private BackgroundPowerSaver backgroundPowerSaver;
    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private BluetoothLeScanner mBtScanner;
    private BluetoothAdapter mBtAdapter;
    private static final int REQUEST_ENABLE_BT = 3;
    //Array of options-->array adapater--> ListView
    private ArrayList<BleItem> device_list= new ArrayList<>();
    private String uuid,dist,mac,majorMinor;

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
                populateListView();
                Snackbar.make(view, "Scanning ...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initBluetooth();
        initBeaconManager();
        populateListView();


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

    private void populateListView() {

        //Build Adapter
        BeaconAdapter adapter= new BeaconAdapter(this,device_list);


        /*ArrayAdapter<String> adapater = new ArrayAdapter<String>(
                this,               //Context for the activity
                R.layout.beacon_items,   //Layout to use (Create)
                myItems);           //Items to be displayed*/
        //Configure the list view
        ListView list =(ListView)findViewById(R.id.listViewMain);
        list.setAdapter(adapter);
        list.setOnItemClickListener(listenerForBeacon);
    }

    AdapterView.OnItemClickListener listenerForBeacon = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
            Intent openAd = new Intent(MainActivity.this,DisplayDistance.class);
            //BleItem chosen = adapter.getItem(position);
            //openAd.putExtra("chosen", Parcels.wrap(chosen));//change this when object is made parcelable. then send whole object advertisment
            startActivity(openAd);
        }
    };

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


        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }


    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons != null && beacons.size() > 0  && region != null) {
            Iterator<Beacon> iterator = beacons.iterator();

            while (iterator.hasNext()) {
                Beacon beacon = iterator.next();
                uuid= beacon.getId1()+":"+beacon.getId2()+":"+beacon.getId3();
                dist=getRoundedDistanceString(beacon.getDistance());
                mac= beacon.getBluetoothAddress();
                majorMinor = beacon.getId2().toString() + "-" + beacon.getId3().toString();

                if(device_list.size()>0){
                    for (int i=0;i<device_list.size();i++){
                        if(!device_list.get(i).uuid_no.equals(uuid)){
                            BleItem device= new BleItem(uuid,dist,mac);
                            device_list.add(device);
                        }else{

                        }
                    }
                }else{
                    BleItem device= new BleItem(uuid,dist,mac);
                    device_list.add(device);
                }


            }
            Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
            Log.i(TAG, "The RRSI OF THE BEACON IS " + beacons.iterator().next().getRssi() + " RSSI.");
            Log.i(TAG, "The first beacon I see is having UUID as: " + beacons.iterator().next().getId1()+":"+beacons.iterator().next().getId2()+":"+beacons.iterator().next().getId3() );
            Log.i(TAG, "UUID IS " + beacons.iterator().next().getServiceUuid() + ".");
            //uuid= beacons.iterator().next().getId1()+":"+beacons.iterator().next().getId2()+":"+beacons.iterator().next().getId3();
            //dist=getRoundedDistanceString(beacons.iterator().next().getDistance());
            //mac= beacons.iterator().next().getBluetoothAddress();


        }
    }
}
