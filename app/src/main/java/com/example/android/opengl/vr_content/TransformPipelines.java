package com.example.android.opengl.vr_content;

/**
 * A trivial container for the two model to world transforms you need. I.e. one
 * for vertices and for direction vectors.
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
