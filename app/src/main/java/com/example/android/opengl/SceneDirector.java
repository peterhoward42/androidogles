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

    public float[] getObjectToWorldTransformForSilo(String siloName) {
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
    private float[] auxSiloWorldTrans() {
        float period = 5;
        float xRotnAngle = (360 * SystemClock.uptimeMillis() / (period * 1000)) % 360;
        float yRotnAngle = 0;
        float zRotnAngle = 0;
        float[] eulerRotationsMatrix = new float[16];
        Matrix.setRotateEulerM(eulerRotationsMatrix, 0, xRotnAngle, yRotnAngle, zRotnAngle);
        float[] startOfOrbit = new float[] {50, 60, 0, 1};
        float[] orbitPosition = new float[4];
        Matrix.multiplyMV(orbitPosition, 0, eulerRotationsMatrix, 0, startOfOrbit, 0);
        float[] toReturn = new float[16];
        Matrix.setIdentityM(toReturn, 0);
        Matrix.translateM(toReturn, 0, orbitPosition[0], orbitPosition[1], orbitPosition[2]);
        return toReturn;
    }
}
