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
}
