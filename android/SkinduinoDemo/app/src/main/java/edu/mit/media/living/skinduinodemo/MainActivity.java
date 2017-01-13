package edu.mit.media.living.skinduinodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    //private static final String BLUETOOTH_ADDRESS = "00:06:66:85:98:C8";
    //private static final String BLUETOOTH_ADDRESS = "00:06:66:85:98:CA";
    //private static final String BLUETOOTH_ADDRESS = "00:06:66:85:98:86"; // bad
    //private static final String BLUETOOTH_ADDRESS = "00:06:66:85:9A:10";
    private static final String BLUETOOTH_ADDRESS = "00:06:66:85:9A:0E";
    private BluetoothManager mBluetoothManager;
    private CapTouchParser mCapTouchParser;

    private GridView mGridView;
    private CapTouchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothManager = new BluetoothManager(this, BLUETOOTH_ADDRESS);
        mCapTouchParser = new CapTouchParser(15);

        mBluetoothManager.setOnLineReceivedListener(tl);

        mGridView = (GridView)findViewById(R.id.gridview);
        mAdapter = new CapTouchAdapter(this, mCapTouchParser.getCapTouchValues());
        mGridView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBluetoothManager.init();
    }

    @Override
    protected void onStop() {
        mBluetoothManager.close();
        super.onStop();
    }

    private BluetoothManager.OnLineReceivedListener tl = new BluetoothManager.OnLineReceivedListener() {
        @Override
        public void lineReceived(String line) {
            if(mCapTouchParser.parse(line)) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
