package com.example.android.opengl;

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
        float[] m = identity();
        Matrix.translateM(m, 0, x, y, z);
        return m;
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
        float[] transposed = identity();
        Matrix.transposeM(transposed, 0, toTranspose, 0);
        return transposed;
    }

    /** If we have a point transform from space A to space B, then this function will derive
     * from it, a transform that will transform direction vectors from space A to space B.
     * @param fullTransform The point transform from space A to space B.
     * @return The transform for direction vectors.
     */
    public static float[] directionVectorTransformFromFullTransform(float[] fullTransform) {
        float[] inverseOfFullTransform = inverted(fullTransform);
        return transposed(inverseOfFullTransform);
    }
}