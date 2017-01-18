package edu.mit.media.living.skinduinodemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private String mBluetoothAddress = null;
    private BluetoothManager mBluetoothManager;
    private CapTouchParser mCapTouchParser;
    private List<BluetoothDevice> mDevices;

    private GridView mGridView;
    private TextView mStatus;
    private CapTouchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothManager = new BluetoothManager(this);
        mCapTouchParser = new CapTouchParser(8);

        mBluetoothManager.setBluetoothCallback(mBluetoothCallback);

        mGridView = (GridView)findViewById(R.id.gridview);
        mStatus = (TextView)findViewById(R.id.status);
        mAdapter = new CapTouchAdapter(this, mCapTouchParser.getCapTouchValues());
        mGridView.setAdapter(mAdapter);

        checkBluetoothEnabled();
    }

    private void checkBluetoothEnabled() {
        if(!mBluetoothManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        setStatusText("Disconnected");
        tryConnectBluetooth();
    }

    private void tryConnectBluetooth() {
        if(mBluetoothAddress != null) {
            if(mBluetoothManager.connect(mBluetoothAddress)) {
                clearStatus();
            } else {
                setStatusText("Failed to connect to " + mBluetoothAddress);
            }
        }
    }

    @Override
    protected void onStop() {
        mBluetoothManager.close();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect:
                clearStatus();

                mDevices = mBluetoothManager.getPairedDevices();
                PairedDeviceAdapter adapter = new PairedDeviceAdapter(this, mDevices);

                new AlertDialog.Builder(this)
                        .setTitle("Select a device to connect to")
                        .setAdapter(adapter, mSelectDeviceListener)
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private DialogInterface.OnClickListener mSelectDeviceListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int position) {
            mBluetoothAddress = mDevices.get(position).getAddress();
            setStatusText("Connecting to " + mBluetoothAddress);
            tryConnectBluetooth();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT ) {
            checkBluetoothEnabled();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void clearStatus() {
        setStatusText("");
        mStatus.setVisibility(View.GONE);
    }

    private BluetoothManager.BluetoothCallback mBluetoothCallback = new BluetoothManager.BluetoothCallback() {
        @Override
        public void lineReceived(String line) {
            if(mCapTouchParser.parse(line)) {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void deviceUnpaired() {
            setStatusText("Skinduino has not been paired to this device");
        }

        @Override
        public void connectionFailed() {
            setStatusText("Unable to connect to Skinduino");
        }

        @Override
        public void connectionBroken() {
            setStatusText("Disconnected from Skinduino");
        }
    };

    private void setStatusText(final String statusText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatus.setVisibility(View.VISIBLE);
                mStatus.setText(statusText);
            }
        });
    }
}
