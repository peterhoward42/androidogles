package com.example.android.opengl;

import android.opengl.Matrix;

/**
 * Created by phoward on 11/01/2016.
 */
public class TransformApply {

    public static XYZf point(float[] transform, XYZf point) {
        float[] result = new float[4];
        Matrix.multiplyMV(
                result, 0, transform, 0, new float[]{point.X(), point.Y(), point.Z(), 0}, 0);
        return new XYZf(result[0], result[1], result[2]);
    }
}
