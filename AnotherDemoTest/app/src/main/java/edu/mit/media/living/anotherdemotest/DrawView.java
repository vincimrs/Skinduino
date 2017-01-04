package edu.mit.media.living.anotherdemotest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

/**
 * Created by vincimrs on 1/4/17.
 */

public class DrawView extends View {
    private int[] channels = new int[6];
    private boolean isValid = false;

    public DrawView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isValid) {
            for (int i = 0; i < 6; i++) {
                channels[i] -= 3;
                if (channels[i] < 0)
                    channels[i] = 0;
            }
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < 6; i++) {
            int ci = 255 - channels[i];
            if (isValid)
                paint.setColor(Color.rgb(255, ci, ci));
            else
                paint.setColor(Color.rgb(255, 255, ci));
            int xl = 30 + 150 * i;
            int xr = xl + 145;
            int yt = 300;
            int yb = yt + 145;
            canvas.drawRect(xl, yt, xr, yb, paint);
        }
    }

    public void updateChannels(int[] values) {
        for (int i = 0; i < 6; i++)
            channels[i] = values[i];
        isValid = true;
    }

    public void invalidateChannels() {
        isValid = false;
    }
}
