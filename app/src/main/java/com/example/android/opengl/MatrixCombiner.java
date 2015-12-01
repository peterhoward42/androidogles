package com.example.android.opengl;

import android.opengl.Matrix;

/**
 * Created by phoward on 01/12/2015.
 */
public class MatrixCombiner {

    private float[] mApplyFirst;
    private float[] mApplySecond;
    private float[] mApplyThird;

    public MatrixCombiner(float[] applyThird, float[] applySecond, float[] applyFirst) {
        mApplyFirst = applyFirst;
        mApplySecond = applySecond;
        mApplyThird = applyThird;
    }

    public float[] combine() {
        float[] tmp = new float[16];
        Matrix.multiplyMM(tmp, 0, mApplySecond, 0, mApplyFirst, 0);
        float[] toReturn = new float[16];
        Matrix.multiplyMM(toReturn, 0, mApplyThird, 0, tmp, 0);
        return toReturn;
    }
}
