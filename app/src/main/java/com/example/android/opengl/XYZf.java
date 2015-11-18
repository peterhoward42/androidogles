package com.example.android.opengl;

/**
 * Created by phoward on 13/11/2015.
 */
public class XYZf {

    private float mX;
    private float mY;
    private float mZ;

    public XYZf(float x, float y, float z) {
        mX = x;
        mY = y;
        mZ = z;
    }

    public final float X() {
        return mX;
    }
    public final float Y() {
        return mY;
    }
    public final float Z() {
        return mZ;
    }

    public Object roundingHash() {
        // Format each field in optimum format (including scientific) in 8 significant
        // digits.
        return String.format("%8g", mX) + String.format("%8g", mY) + String.format("%8g", mZ);
    }
}
