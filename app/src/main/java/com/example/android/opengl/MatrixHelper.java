package com.example.android.opengl;

import android.opengl.Matrix;

/**
 * Created by phoward on 25/11/2015.
 */
public class MatrixHelper {

    public static String fmtXYZWVector(float[] vector) {
        return String.format("X: %6.02f, Y: %6.02f, Z: %6.02f, W: %6.02f",
                vector[0], vector[1], vector[2], vector[3]);
    }

    public static float[] perspectiveDivideXYZWVector(float[] pointInClipSpace) {
        float[] toReturn = new float[4];
        for (int i = 0; i < 4; i++)
            toReturn[i] = pointInClipSpace[i] / pointInClipSpace[3];
        return toReturn;
    }

    public static String fmtMatrix4x4(float[] matrix) {
        String s = "";
        for (int i = 0; i < 16; i++)
            s += String.format(", %6.2f", matrix[i]);
        return s;
    }

    public static float[] makeIdentity() {
        float[] m = new float[16];
        Matrix.setIdentityM(m, 0);
        return m;
    }

    public static XYZf transformPoint(float[] transform, XYZf point) {
        float[] result = new float[4];
        Matrix.multiplyMV(
                result, 0, transform, 0, new float[]{point.X(), point.Y(), point.Z(), 0}, 0);
        return new XYZf(result[0], result[1], result[2]);
    }

    public static float[] makeTranslationMatrix(float x, float y, float z) {
        float[] m = makeIdentity();
        Matrix.translateM(m, 0, x, y, z);
        return m;
    }

    public static float[] makeYAxisRotationMatrix(float angleDeg) {
        float[] m = new float[16];
        Matrix.setRotateM(m, 0, angleDeg, 0, 1, 0);
        return m;
    }

    public static float[] makeZAxisRotationMatrix(float angleDeg) {
        float[] m = new float[16];
        Matrix.setRotateM(m, 0, angleDeg, 0, 0, 1);
        return m;
    }
}
