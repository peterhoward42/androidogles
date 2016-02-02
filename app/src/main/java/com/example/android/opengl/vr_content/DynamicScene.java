package com.example.android.opengl.vr_content;

import com.example.android.opengl.mesh.Mesh;
import com.example.android.opengl.primitives.Sphere;

import java.util.Set;

/**
 * An interface that exposes a scene comprising a collection of {@link Mesh} - that you can
 * refer to by name, and a set of methods that tell you how the models should be transformed
 * into their scene - to create the correct juxtapositions and animations.
 */
public interface DynamicScene {

    Set<String> getSiloNames();
    Mesh getSilo(String siloName);
    float[] getCurrentObjectToWorldTransform(String siloName);
    Sphere getCurrentEffectiveSphere();
}