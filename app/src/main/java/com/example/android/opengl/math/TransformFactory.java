package com.example.android.opengl.math;

import android.opengl.Matrix;

/**
 * Created by phoward on 11/01/2016.
 */
public class TransformFactory {

    public static float[] identity() {
        float[] m = new float[16];
        Matrix.setIdentityM(m, 0);
        return m;
    }

    public static float[] translation(float x, float y, float z) {
        // @formatter:off
        float[] toReturn = {
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                x, y, z, 1};
        // @formatter:on
        return toReturn;
    }

    public static float[] yAxisRotation(float angleDeg) {
        float[] m = new float[16];
        Matrix.setRotateM(m, 0, angleDeg, 0, 1, 0);
        return m;
    }

    public static float[] zAxisRotation(float angleDeg) {
        float[] m = new float[16];
        Matrix.setRotateM(m, 0, angleDeg, 0, 0, 1);
        return m;
    }

    public static float[] inverted(float[] toInvert) {
        float[] inverted = identity();
        Matrix.invertM(inverted, 0, toInvert, 0);
        return inverted;
    }

    public static float[] transposed(float[] toTranspose) {
        float[] transposed = new float[16];
        Matrix.transposeM(transposed, 0, toTranspose, 0);
        return transposed;
    }

    public static float[] isolate3x3From4x4(float[] in) {
        float[] out = new float[9];

        out[0] = in[0];
        out[1] = in[1];
        out[2] = in[2];

        out[3] = in[4];
        out[4] = in[5];
        out[5] = in[6];

        out[6] = in[8];
        out[7] = in[9];
        out[8] = in[10];

        return out;
    }

    public static float[] expand3x3To4x4WithZeros(float[] threeByThree) {
        float[] in = threeByThree;
        // @formatter:off
        float[] toReturn = new float[] {
                in[0], in[1], in[2], 0,
                in[3], in[4], in[5], 0,
                in[6], in[7], in[8], 0,
                0,     0,     0,     0};
        // @formatter:on
        return toReturn;
    }

    /**
     * If we have a point transform from space A to space B, then this function will derive
     * from it, a (3*3) transform that will transform direction vectors from space A to space B.
     *
     * @param fullTransform The point transform from space A to space B.
     * @return The 3*3 transform for direction vectors.
     */
    public static float[] directionTransformFromVertexTransform(float[] fullTransform) {
        float[] inverseOfFullTransform = inverted(fullTransform);
        float[] transposed = transposed(inverseOfFullTransform);
        float[] topLeftPart = isolate3x3From4x4(transposed);
        return topLeftPart;
    }
}
