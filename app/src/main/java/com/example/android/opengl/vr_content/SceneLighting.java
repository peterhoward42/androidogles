package com.example.android.opengl.vr_content;

import com.example.android.opengl.primitives.XYZf;

/**
 * An thing that can answer questions about how a scene is lit.
 */
public class SceneLighting {

    private static final XYZf HIGH_AND_TO_THE_RIGHT = new XYZf(15f, 8f, 10f).normalised();

    public XYZf getDirectionTowardsLight() {
        return HIGH_AND_TO_THE_RIGHT;
    }
}
