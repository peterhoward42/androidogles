package com.example.android.opengl;

/**
 * Created by phoward on 11/01/2016.
 */
public class RenderingTransforms {

    private float[] mvp;
    private float[] modelToWorldDirectionVector;
    public RenderingTransforms(final float[] mvp, final float[] modelToWorldDirectionVector) {
        this.mvp = mvp;
        this.modelToWorldDirectionVector = modelToWorldDirectionVector;
    }

    public float[] getModelToWorldDirectionVector() {
        return modelToWorldDirectionVector;
    }

    public float[] getMVP() {
        return mvp;
    }
}
