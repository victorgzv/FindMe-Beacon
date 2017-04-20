package com.example.vctor.findme;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mDeviceListRecyclerView;
    private static final long SCAN_PERIOD = 10000;
    private boolean mScanning;
    private Handler mHandler = new Handler();
    private BluetoothAdapter mBtAdapter;
    private BluetoothLeScanner mBtScanner;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int REQUEST_PERMISSION_FINE = 163;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBluetooth();

         Toolbar mToolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
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
}