package com.example.android.opengl.primitives;

/**
 * Created by phoward on 13/11/2015.
 */

/**
 * An XYZ vector with floating point precision.
 */
public class XYZf {

    private float[] xyz;

    public XYZf(float x, float y, float z) {
        xyz = new float[]{x, y, z};
    }

    /**
     * These constructors exists only to spare calling code the typecasting of parameters in
     * special cases.
     */
    public XYZf(double x, double y, double z) {
        xyz = new float[]{(float) x, (float) y, (float) z};
    }

    public XYZf(int x, int y, int z) {
        xyz = new float[]{(float) x, (float) y, (float) z};
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

    public XYZf clampedComponentWiseToMinima(final XYZf potentialNewMinima) {
        XYZf q = potentialNewMinima;
        return new XYZf(
                Math.min(X(), q.X()),
                Math.min(Y(), q.Y()),
                Math.min(Z(), q.Z()));
    }

    public XYZf clampedComponentWiseToMaxima(final XYZf potentialNewMaxima) {
        XYZf q = potentialNewMaxima;
        return new XYZf(
                Math.max(X(), q.X()),
                Math.max(Y(), q.Y()),
                Math.max(Z(), q.Z()));
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

    public XYZf vectorScaledToLength(final float lengthRequired) {
        final float scaleRequired = lengthRequired / resultantLength();
        return new XYZf(X() * scaleRequired, Y() * scaleRequired, Z() * scaleRequired);
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

    /**
     * This method provides a string representation of the three constituent values
     * after rounding them to 5 decimal places. The emphasis is on formatting - i.e. the intent
     * being either for a human being to look at the result.
     *
     * @return The string produced.
     */
    public final String formatRounded() {
        return String.format("%.5f %.5f %.5f", xyz[0]
                , xyz[1]
                , xyz[2]
        );
    }

    /**
     * This method provides a string representation of the three constituent values, after
     * they have been rounded to 5 decimal places. The emphasis is on using the result as a hashing
     * value - that resolves two objects that differ only due to calculation rounding differences
     * and similar - into the same hash code.
     *
     * @return
     */
    public final String hashAfterNumericalRounding() {
        // share implementation but have separate function to make intentclear.
        return formatRounded();
    }
}
