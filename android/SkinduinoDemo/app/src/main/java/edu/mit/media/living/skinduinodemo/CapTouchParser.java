package edu.mit.media.living.skinduinodemo;

public class CapTouchParser {
    private int[] mCapTouchValues;
    private int mSize;

    public CapTouchParser(int size) {
        mCapTouchValues = new int[size];
        mSize = size;

        mCapTouchValues = new int[size];
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
