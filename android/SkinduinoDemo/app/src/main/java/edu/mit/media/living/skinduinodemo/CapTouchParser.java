package edu.mit.media.living.skinduinodemo;

public class CapTouchParser {
    private int[] mCapTouchValues;
    private int mSize;

    public CapTouchParser(int size) {
        mCapTouchValues = new int[size];
        mSize = size;

        mCapTouchValues = new int[] { 0, 255, 255, 100, 150, 0, 0, 50, 100, 150, 200, 250, 255, 0, 100};
    }

    public int[] getCapTouchValues() {
        return mCapTouchValues;
    }

    public boolean parse(String line) {
        String[] toks = line.split(",");
        if(toks.length >= mSize) {
            for (int i = 0; i < toks.length; ++i) {
                mCapTouchValues[i] = Integer.parseInt(toks[i]);
            }

            return true;
        }

        return false;
    }
}
