package com.example.android.opengl.vr_content;

import android.os.SystemClock;

import com.example.android.opengl.math.MatrixCombiner;
import com.example.android.opengl.math.TransformFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Knows where ModelCollectionCubes should be scaled, placed and oriented in world space to build the
 * required scene.
 */
public class SceneAssemblerCubes implements ISceneAssembler {

    public Set<String> getSiloNames() {
        Set<String> returnedSet = new HashSet<String>();
        returnedSet.add("mainSilo");
        returnedSet.add("auxSilo");

        return returnedSet;
    }

    public float getEffectiveRadius() {
        // The worst case bounding box to include the satellite cube is 120mm across flats.
        // I.e. with a half-width of 60. And the worst case diagonal then by Pythagorus is the root
        // of the sum of this squared.
        return 85.0f;
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
        return TransformFactory.identity();
    }

    /** The auxilliary silo is animated.
     *
     */
    private float[] auxSiloWorldTrans() {
        // Cause it live somewhere away from the origin and to spin.
        float period = 5; // seconds
        float angle = (360 * SystemClock.uptimeMillis() / (period * 1000)) % 360;
        float rotationM[] = TransformFactory.yAxisRotation(angle);
        float[] translationM = TransformFactory.translation(60, 60, -60);
        return MatrixCombiner.combineTwo(translationM, rotationM);
    }
}

