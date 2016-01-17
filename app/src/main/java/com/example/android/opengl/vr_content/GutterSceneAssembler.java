package com.example.android.opengl.vr_content;

import android.os.SystemClock;

import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.math.MatrixCombiner;
import com.example.android.opengl.math.TransformFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Knows where CubesSceneModels should be scaled, placed and oriented in world space to build the
 * required scene.
 */
public class GutterSceneAssembler implements ISceneAssembler {

    private GutterSceneModels mModel;

    public GutterSceneAssembler(GutterSceneModels model) {
        mModel = model;
    }

    public Set<String> getSiloNames() {
        Set<String> returnedSet = new HashSet<String>();
        returnedSet.add("mainSilo");
        return returnedSet;
    }

    public float getEffectiveRadius() {
        return mModel.getEffectiveRadius();
    }

    public float[] getCurrentObjectToWorldTransform(String siloName) {
        XYZf offsetOfCentre = mModel.getOffsetFromOriginOfBoundingBoxCentre();
        float[] transform = TransformFactory.translation(offsetOfCentre);
        return TransformFactory.inverted(transform);
    }
}

