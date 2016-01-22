package com.example.android.opengl.vr_content;

import com.example.android.opengl.geom.Mesh;

import java.util.Set;

/**
 * An interface that exposes a scene comprising a collection of {@link Mesh} - that you can
 * refer to by name, and a set of methods that tell you how the models should be transformed
 * into their scene - to create the correct juxtapositions and animations.
 */
public interface DynamicScene {

    public Set<String> getSiloNames();
    public Mesh getSilo(String siloName);
    public int getNumberOfTrianglesInSilo(String siloName);
    public float[] getCurrentObjectToWorldTransform(String siloName);
    public float getEffectiveRadius();
}