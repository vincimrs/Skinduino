package edu.mit.media.living.anotherdemotest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.UUID;

public class ThermistorBluetoothManager_Old {
    static final int THERMISTOR_NOMINAL = 10000;
    static final int TEMPERATURE_NOMINAL = 25;
    static final int BCOEFFICIENT = 3950;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private Handler mHandler;
    private BluetoothDevice mBluetoothDevice;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private Context mContext;
    private OnTemperatureChangedListener mListener;

    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //private static String mAddress = "00:06:66:45:0D:15";
    private static String mAddress = "00:06:66:85:98:6C";

    public interface OnTemperatureChangedListener {
        void temperatureChanged(int adcReading, float temperature);
    }

    public ThermistorBluetoothManager_Old(Context context) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        mContext = context;
        mHandler = new Handler();
    }

    public OnTemperatureChangedListener getOnTemperatureChangedListener() {
        return mListener;
    }

    public void setOnTemperatureChangedListener(OnTemperatureChangedListener l) {
        mListener = l;
    }

    public boolean isBluetoothEnabled() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        return true;
    }

    public boolean init() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                if(mAddress.equals(device.getAddress())) {
                    mBluetoothDevice = device;
                    Log.i("init function", "found paired device: " + device.getAddress());
                    break;
                }
            }
        }

        if(mBluetoothDevice == null) {
            Log.i("init function", "No device");
            return false;
        } else {
            mConnectThread = new ConnectThread(mBluetoothDevice);
            mConnectThread.start();

            return true;
        }
    }

    public void close() {
        if(mConnectThread != null) {
            mConnectThread.cancel();
        }

        if(mConnectedThread != null) {
            mConnectedThread.cancel();
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                Log.i("ConnectThread", "Cannot connect", connectException);
                return;
            }

            Log.i("ConnectThread", "successfully connected");

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void manageConnectedSocket(BluetoothSocket bsocket) {
        mConnectedThread = new ConnectedThread(bsocket);
        mConnectedThread.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BufferedReader mmInStream;
        private final OutputStream mmOutStream;
        private final BufferedWriter mOut;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = new BufferedReader(new InputStreamReader(tmpIn));
            mmOutStream = tmpOut;
            mOut = new BufferedWriter(new OutputStreamWriter(mmOutStream));
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream

            while (true) {
                try {
                    // Read from the InputStream
                    String line = mmInStream.readLine();
                    Log.i("RawData", line);

                    String[] toks = line.split(",");
                    if(toks.length > 1) {
                        try {
                            int adcReading = Integer.parseInt(toks[0]);
                            float r = 10000f / (1023f / adcReading - 1);
                            float t = 1f / (273.15f + TEMPERATURE_NOMINAL) + (1f / BCOEFFICIENT) * (float)Math.log(r / THERMISTOR_NOMINAL);
                            t = 1f / t - 273.15f;

                            Log.i("tempData", "Case 1: adcReading=" + adcReading + ", t=" + t);
                            MyRunnable mr = new MyRunnable(adcReading, t);//temperature);
                            mHandler.post(mr);
                        } catch(NumberFormatException e) {}
                    } else if(toks.length == 1) {
                        try {
                            float adcReading = Float.parseFloat(toks[0]);
                            Log.i("tempData", "Case 2: adcReading=" + adcReading);
                            MyRunnable mr = new MyRunnable(0, adcReading);//temperature);
                            mHandler.post(mr);
                        } catch (NumberFormatException e) {}
                    } else {
                        Log.i("tempData", "Case 3: ahhhhhh");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext.getApplicationContext(), "ahhhhh", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    // Send the obtained bytes to the UI activity
                    //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                    //      .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public class MyRunnable implements Runnable {
        private int mAdcValue;
        private float mTemp;

        public MyRunnable(int adcValue, float temp) {
            mAdcValue = adcValue;
            mTemp = temp;
        }

        public void run() {
            if(mListener != null) {
                mListener.temperatureChanged(mAdcValue, mTemp);
            }
        }
    }
}
