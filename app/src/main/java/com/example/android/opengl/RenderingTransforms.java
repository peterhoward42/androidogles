package com.example.android.opengl;

/**
 * Created by phoward on 11/01/2016.
 */
public class RenderingTransforms {

    private float[] mvpForVertices;
    private float[] modelToWorldForDirections;
    public RenderingTransforms(final float[] mvpForVertices, final float[] modelToWorldForDirections) {
        this.mvpForVertices = mvpForVertices;
        this.modelToWorldForDirections = modelToWorldForDirections;
    }

    public float[] getModelToWorldForDirections() {
        return modelToWorldForDirections;
    }

    public float[] getMvpForVertices() {
        return mvpForVertices;
    }
}
