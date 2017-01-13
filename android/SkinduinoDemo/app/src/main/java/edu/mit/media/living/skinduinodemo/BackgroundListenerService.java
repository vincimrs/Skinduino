package edu.mit.media.living.skinduinodemo;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class BackgroundListenerService extends Service {

    // TODO move to activity

    public BackgroundListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification foregroundNotification = new NotificationCompat.Builder(this)
                .setContentTitle("Demo Wizard")
                .setContentText("Listening")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(1, foregroundNotification);


        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }
}
