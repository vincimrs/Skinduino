package edu.mit.media.living.anotherdemotest;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/*
 * THIS FILE IS OBSELETE
 */
public class BackgroundListenerService extends Service {
    private ThermistorBluetoothManager mBluetoothManager;

    public BackgroundListenerService() {
        mBluetoothManager = new ThermistorBluetoothManager(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification foregroundNotification = new Notification.Builder(this)
                .setContentTitle("Demo Wizard")
                .setContentText("Listening")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(1, foregroundNotification);

        //mBluetoothManager.setOnTemperatureChangedListener(tl);
        mBluetoothManager.init();

        return super.onStartCommand(intent, flags, startId);
    }

    /*
    private ThermistorBluetoothManager.OnCapacityChangedListener tl = new ThermistorBluetoothManager.OnCapacityChangedListener() {
        @Override
        public void temperatureChanged(int adcReading, float temperature) {
            Log.i("output", "Receive" + adcReading + ", temperature=" + temperature);
            //Intent dialogIntent = new Intent(BackgroundListenerService.this, ImageActivity.class);
            //dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(dialogIntent);
        }
    };*/

    @Override
    public void onDestroy() {
        mBluetoothManager.close();
        stopForeground(true);
        super.onDestroy();
    }
}
