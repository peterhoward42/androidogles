package com.example.android.opengl;

/**
 * Created by phoward on 11/01/2016.
 */
public class MatrixFormatter {

    public static String fmtXYZWVector(float[] vector) {
        return String.format("X: %.2f, Y: %.2f, Z: %.2f, W: %.2f",
                vector[0], vector[1], vector[2], vector[3]);
    }

    public static String fmtMatrix4x4(float[] matrix) {
        String s = "";
        for (int i = 0; i < 15; i++)
            s += String.format("%.2f,", matrix[i]);
        s += String.format("%.2f", matrix[15]);
        return s;
    }

    public static String fmtMatrix3x3(float[] matrix) {
        String s = "";
        for (int i = 0; i < 8; i++)
            s += String.format("%.2f,", matrix[i]);
        s += String.format("%.2f", matrix[8]);
        return s;
    }
}
