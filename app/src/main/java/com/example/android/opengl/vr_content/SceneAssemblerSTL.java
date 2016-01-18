package com.example.android.opengl.vr_content;

import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.math.TransformFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * A simple implementation of ISceneAssembler that knows how to assemble a {@Class ModelCollectionSTL}
 */
public class SceneAssemblerSTL implements ISceneAssembler {

    private ModelCollectionSTL mModel;

    public SceneAssemblerSTL(ModelCollectionSTL model) {
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
        XYZf offsetOfCentre = mModel.getBoundingBoxCentre();
        float[] transform = TransformFactory.translation(offsetOfCentre);
        return TransformFactory.inverted(transform);
    }
}

