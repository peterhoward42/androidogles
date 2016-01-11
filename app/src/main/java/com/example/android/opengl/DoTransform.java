package com.example.android.opengl;

import android.opengl.Matrix;

/**
 * Created by phoward on 11/01/2016.
 */
public class DoTransform {

    /** Divide through the given 4 dimensional vector with the fourth element.
     *
     * @param pointInClipSpace The XYZW point to operate on.
     * @return The divided-through XYZW point.
     */
    public static float[] perspectiveDivide(float[] pointInClipSpace) {
        float[] toReturn = new float[4];
        for (int i = 0; i < 4; i++)
            toReturn[i] = pointInClipSpace[i] / pointInClipSpace[3];
        return toReturn;
    }

    public static XYZf point(float[] transform, XYZf point) {
        float[] result = new float[4];
        Matrix.multiplyMV(
                result, 0, transform, 0, new float[]{point.X(), point.Y(), point.Z(), 0}, 0);
        return new XYZf(result[0], result[1], result[2]);
    }
}
