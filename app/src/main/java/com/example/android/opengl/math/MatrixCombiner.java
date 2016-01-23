package com.example.android.opengl.math;

import android.opengl.Matrix;

/**
 * Created by phoward on 01/12/2015.
 */
public class MatrixCombiner {

    public static float[] combineTwo(float[] applySecond, float[] applyFirst) {
        float[] toReturn = new float[16];
        Matrix.multiplyMM(toReturn, 0, applySecond, 0, applyFirst, 0);
        return toReturn;
    }

    public static float[] combineThree(float[] applyThird, float[] applySecond, float[] applyFirst) {
        float[] tmp = combineTwo(applySecond, applyFirst);
        return combineTwo(applyThird, tmp);
    }

    public static float[] combineFour(
            float[] applyFourth, float[] applyThird, float[] applySecond, float[] applyFirst) {
        float[] tmp = combineThree(applyThird, applySecond, applyFirst);
        return combineTwo(applyFourth, tmp);
    }
}
