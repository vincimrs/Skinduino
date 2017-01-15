package edu.mit.media.living.skinduinodemo;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class CapTouchAdapter extends BaseAdapter {
    private static final int SIZE = 100;
    private Context mContext;
    private int[] mCapTouchValues;

    public CapTouchAdapter(Context c, int[] capTouchValues) {
        mContext = c;
        mCapTouchValues = capTouchValues;
    }

    public int getCount() {
        return 6;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = new View(mContext);

            int size = mContext.getResources().getDimensionPixelSize(R.dimen.cell_size);
            v.setLayoutParams(new GridView.LayoutParams(size, size));
            v.setPadding(0, 0, 0, 0);
        } else {
            v = convertView;
        }

        int pos = 255 - mCapTouchValues[position];

        int color = Color.rgb(pos, pos, pos);
        //int color = (Integer) new ArgbEvaluator().evaluate(pos, 0x000000, 0xffffff);
        v.setBackgroundColor(color);

        return v;
    }
}
