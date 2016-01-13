package com.example.android.opengl.vr_content;
import java.util.Set;

/**
 * Knows where CubesSceneModels should be scaled, placed and oriented in world space to build the
 * required scene.
 */
public interface ISceneAssembler {

    public Set<String> getSiloNames();
    public float[] getCurrentObjectToWorldTransform(String siloName);
}

