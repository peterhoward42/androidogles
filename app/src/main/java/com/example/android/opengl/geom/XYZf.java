package com.example.android.opengl.geom;

/**
 * Created by phoward on 13/11/2015.
 */

import android.util.Log;

/**
 * An XYZ vector.
 */
public class XYZf {

    private float[] xyz;

    public XYZf(float x, float y, float z) {
        xyz = new float[]{x, y, z};
    }

    /** This constructor exists only to avoid the clutter of casting doubles that emerge from
     * functions like Math.max() to floats all over the place in calling code.
     */
    public XYZf(double x, double y, double z) {
        xyz = new float[]{(float)x, (float)y, (float)z};
    }

    public final float X() {
        return xyz[0];
    }

    public final float Y() {
        return xyz[1];
    }

    public final float Z() {
        return xyz[2];
    }

    public void overwriteX(final float q) {
        if ((q == q) == false) {
            int a = 42; // q is NaN
        }
        xyz[0] = q;
    }

    public void overwriteY(final float q) {
        xyz[1] = q;
    }

    public void overwriteZ(final float q) {
        xyz[2] = q;
    }

    public final float[] asFloatArray() {
        return xyz;
    }

    public XYZf plus(XYZf rhs) {
        return new XYZf(
                xyz[0] + rhs.xyz[0],
                xyz[1] + rhs.xyz[1],
                xyz[2] + rhs.xyz[2]
        );
    }

    public XYZf minus(XYZf rhs) {
        return new XYZf(
                xyz[0] - rhs.xyz[0],
                xyz[1] - rhs.xyz[1],
                xyz[2] - rhs.xyz[2]
        );
    }

    public float resultantLength() {
        return (float) Math.sqrt(xyz[0]
                        * xyz[0]
                        + xyz[1]
                        * xyz[1]
                        + xyz[2]
                        * xyz[2]
        );
    }

    public float dotProduct(final XYZf other) {
        float dot = (this.xyz[0] * other.xyz[0] + this.xyz[1] * other.xyz[1] + this.xyz[2] * other.xyz[2]);
        return dot;
    }

    public XYZf normalisedCrossProduct(XYZf b) {
        return (this.crossProduct(b)).normalised();
    }

    public XYZf crossProduct(XYZf p2) {
        XYZf result = new XYZf(0, 0, 0);
        result.xyz[0]
                = this.xyz[1]
                * p2.xyz[2]
                - p2.xyz[1]
                * this.xyz[2]
        ;
        result.xyz[1]
                = this.xyz[2]
                * p2.xyz[0]
                - p2.xyz[2]
                * this.xyz[0]
        ;
        result.xyz[2]
                = this.xyz[0]
                * p2.xyz[1]
                - p2.xyz[0]
                * this.xyz[1]
        ;
        return result;
    }

    public XYZf normalised() {
        float length = this.resultantLength();
        return new XYZf(xyz[0]
                / length, xyz[1]
                / length, xyz[2]
                / length);
    }

    public String formatRounded() {
        return String.format("%.5f %.5f %.5f", xyz[0]
                , xyz[1]
                , xyz[2]
        );
    }
}
