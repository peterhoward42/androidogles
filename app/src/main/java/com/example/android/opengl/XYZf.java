package com.example.android.opengl;

/**
 * Created by phoward on 13/11/2015.
 */

/**
 * An XYZ vector.
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

    public final float[] asFloatArray() {
        return new float[] {mX, mY, mZ};
    }

    public XYZf plus(XYZf rhs) {
        return new XYZf(mX + rhs.mX, mY + rhs.mY, mZ + rhs.mZ);
    }

    public XYZf minus(XYZf rhs) {
        return new XYZf(mX - rhs.mX, mY - rhs.mY, mZ - rhs.mZ);
    }

    public float resultantLength() {
        return (float) Math.sqrt(mX * mX + mY * mY + mZ * mZ);
    }

    public XYZf normalisedCrossProduct(XYZf b) {
        return (this.crossProduct(b)).normalised();
    }

    public XYZf crossProduct(XYZf p2) {
        XYZf result = new XYZf(0, 0, 0);
        result.mX = this.mY * p2.mZ - p2.mY * this.mZ;
        result.mY = this.mZ * p2.mX - p2.mZ * this.mX;
        result.mZ = this.mX * p2.mY - p2.mX * this.mY;
        return result;
    }

    public XYZf normalised() {
        float length = this.resultantLength();
        return new XYZf(mX / length, mY / length, mZ / length);
    }

    public String formatRounded() {
        return String.format("%.5f %.5f %.5f", mX, mY, mZ);
    }
}
