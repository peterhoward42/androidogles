package com.example.android.opengl;

/**
 * Created by phoward on 11/01/2016.
 */
public class MatrixFormatter {

    public static String fmtXYZWVector(float[] vector) {
        return String.format("X: %6.02f, Y: %6.02f, Z: %6.02f, W: %6.02f",
                vector[0], vector[1], vector[2], vector[3]);
    }

    public static String fmtMatrix4x4(float[] matrix) {
        String s = "";
        for (int i = 0; i < 16; i++)
            s += String.format(", %6.2f", matrix[i]);
        return s;
    }
}
