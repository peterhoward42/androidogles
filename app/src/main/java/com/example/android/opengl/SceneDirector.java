package com.example.android.opengl;

import android.opengl.Matrix;
import android.os.SystemClock;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by phoward on 25/11/2015.
 */
public class SceneDirector {

    public Set<String> getSiloNames() {
        Set<String> returnedSet = new HashSet<String>();
        returnedSet.add("mainSilo");
        returnedSet.add("auxSilo");

        return returnedSet;
    }

    public float[] getCurrentObjectToWorldTransform(String siloName) {
        float[] returnedMatrix = null;
        switch (siloName) {
            case "mainSilo":
                returnedMatrix = mainSiloWorldTrans();
                break;
            case "auxSilo":
                returnedMatrix = auxSiloWorldTrans();
                break;
        }
        return returnedMatrix;
    }

    private float[] mainSiloWorldTrans() {
        float[] m = new float[16];
        Matrix.setIdentityM(m, 0);
        return m;
    }

    /** The auxilliary silo is animated.
     *
     */
    private float[] auxSiloWorldTrans() {
        // Cause it live somewhere away from the origin and to spin.
        float period = 5; // seconds
        float angle = (360 * SystemClock.uptimeMillis() / (period * 1000)) % 360;
        float rotationM[] = MatrixHelper.makeYAxisRotationMatrix(angle);
        float[] translationM = MatrixHelper.makeTranslationMatrix(60, 60, -60);
        return MatrixCombiner.combineTwo(translationM, rotationM);
    }
}

