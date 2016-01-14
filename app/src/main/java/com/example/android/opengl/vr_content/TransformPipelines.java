package com.example.android.opengl.vr_content;

/**
 * Created by phoward on 11/01/2016.
 */
public class TransformPipelines {

    private float[] mvpForVertices;
    private float[] modelToWorldForDirections;
    public TransformPipelines(final float[] mvpForVertices, final float[] modelToWorldForDirections) {
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
