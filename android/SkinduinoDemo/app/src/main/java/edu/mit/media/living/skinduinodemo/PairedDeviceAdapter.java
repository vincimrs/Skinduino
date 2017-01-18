package edu.mit.media.living.skinduinodemo;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PairedDeviceAdapter extends BaseAdapter {
    private Context mContext;
    private List<BluetoothDevice> mDevices;

    public PairedDeviceAdapter(Context context, List<BluetoothDevice> devices) {
        mContext = context;
        mDevices = devices;
    }

    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if(convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text1 = (TextView) view.findViewById(R.id.text1);
            viewHolder.text2 = (TextView) view.findViewById(R.id.text2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BluetoothDevice bd = mDevices.get(position);
        viewHolder.text1.setText(bd.getName());
        viewHolder.text2.setText(bd.getAddress());

        return view;
    }

    private class ViewHolder {
        public TextView text1, text2;
    }
}
