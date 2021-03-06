package com.example.android.opengl.vr_content;

import android.os.SystemClock;

import com.example.android.opengl.mesh.Mesh;
import com.example.android.opengl.mesh.MeshFactorySimpleCubes;
import com.example.android.opengl.math.MatrixCombiner;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A trivial implementation of DynamicScene for test and development purposes, with content
 * comprising just a 100mm cube and a 20mm cube.
 */
public class DynamicSceneCubes implements DynamicScene {

    private Map<String, Mesh> mSilos;

    public DynamicSceneCubes() {
        mSilos = new HashMap<String, Mesh>();

        Mesh mainSilo = new MeshFactorySimpleCubes(100).makeMesh();
        mSilos.put("mainSilo", mainSilo);

        Mesh auxSilo = new MeshFactorySimpleCubes(20).makeMesh();
        mSilos.put("auxSilo", auxSilo);
    }

    public Mesh getSilo(String siloName) {
        return mSilos.get(siloName);
    }

    public Set<String> getSiloNames() {
        return mSilos.keySet();
    }

    public Sphere getCurrentEffectiveSphere() {
        // The worst case bounding box to include the satellite cube is 120mm across flats.
        // I.e. with a half-width of 60. And the worst case diagonal then by Pythagorus is the root
        // of the sum of this squared.
        return new Sphere(new XYZf(0,0,0), 85.0f);
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
        float rotationM[] = TransformFactory.rotationAboutY(angle);
        float[] translationM = TransformFactory.translation(60, 60, -60);
        return MatrixCombiner.combineTwo(translationM, rotationM);
    }
}
