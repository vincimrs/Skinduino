package edu.mit.media.living.anotherdemotest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements ThermistorBluetoothManager.OnCapacityChangedListener {
    private static final int MSG_WILL_CONNECT = 1;
    private static final int MSG_CONNECT = 2;
    private static final int MSG_ON_DATA = 3;
    private static final int MSG_NO_PAIRED_DEVICE = 4;
    private static final int MSG_CONNECTION_FAILED = 5;
    private static final int MSG_CONNECTION_BROKEN = 6;

    private DrawView drawView;
    private TextView textStatus;

    private TimeString mTimeString;
    private ThermistorBluetoothManager mBluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout drawViewLayout = (RelativeLayout) findViewById(R.id.drawViewLayout);

        drawView = new DrawView(this);
        drawView.setBackgroundColor(Color.WHITE);

        drawViewLayout.addView(drawView);

        textStatus = (TextView) findViewById(R.id.textStatus);

        mTimeString = new TimeString();
        mBluetoothManager = new ThermistorBluetoothManager(this);
        mBluetoothManager.setOnCapacityChangedListener(this);

        int[] tmp = {0, 50, 100, 150, 200, 255};
        drawView.updateChannels(tmp);
        drawView.invalidateChannels();
        drawView.setWillNotDraw(false);

        mHandler.sendEmptyMessageDelayed(MSG_WILL_CONNECT, 2000L);
        frameCountHandler.sendEmptyMessage(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent startBackground = new Intent(this, BackgroundListenerService.class);
        if (id == R.id.menu_start) {
            startService(startBackground);
            return true;
        } else if(id == R.id.menu_stop) {
            stopService(startBackground);
        } else if(id == R.id.menu_test) {
            //startActivity(new Intent(this, ImageActivity.class));
        }

        return super.onOptionsItemSelected(item);

    }
    */

    //----- State handler ------------------------------------------------------------------------
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == MSG_WILL_CONNECT) {
                textStatus.setText("Status: Will connect in 3 seconds");
                mHandler.sendEmptyMessageDelayed(MSG_CONNECT, 3000L);
            }
            else if (msg.what == MSG_CONNECT) {
                textStatus.setText("Status: Connecting...");
                mBluetoothManager.init();
            }
            else if (msg.what == MSG_ON_DATA) {
                textStatus.setText("Status: Connected (update: " + mTimeString.currentTimeForDisplay() + ")");
            }
            else if (msg.what == MSG_NO_PAIRED_DEVICE) {
                textStatus.setText("Status: No paired device");
                mHandler.sendEmptyMessageDelayed(MSG_WILL_CONNECT, 7000L);
            }
            else if (msg.what == MSG_CONNECTION_FAILED) {
                textStatus.setText("Status: Cannot connect");
                mHandler.sendEmptyMessageDelayed(MSG_WILL_CONNECT, 7000L);
            }
            else if (msg.what == MSG_CONNECTION_BROKEN) {
                textStatus.setText("Status: Connection is broken");
                mHandler.sendEmptyMessageDelayed(MSG_WILL_CONNECT, 7000L);
            }
        }
    };

    private Handler frameCountHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            drawView.invalidate();
            frameCountHandler.sendEmptyMessageDelayed(0, 30L);
        }
    };

    //----- Callbacks of Bluetooth manager -------------------------------------------------------
    @Override
    public void onData(int[] channels) {
        mHandler.sendEmptyMessage(MSG_ON_DATA);
        drawView.updateChannels(channels);
        Log.i("GET_DATA", "data=" + channels);
    }

    @Override
    public void onNoDeviceFound() {
        mHandler.sendEmptyMessage(MSG_NO_PAIRED_DEVICE);
        drawView.invalidateChannels();
    }

    @Override
    public void onConnectionFailed(Exception e) {
        mHandler.sendEmptyMessage(MSG_CONNECTION_FAILED);
        drawView.invalidateChannels();
    }

    @Override
    public void onConnectionBroken(Exception e) {
        mHandler.sendEmptyMessage(MSG_CONNECTION_BROKEN);
        drawView.invalidateChannels();
    }


}
