package com.example.android.opengl.vr_content;

import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.Triangle;

import java.util.Collection;
import java.util.Set;

/**
 * An interface that exposes a set of models that live in a scene, each in a named silo
 * (person, engine...). The models are defined with coordinates in their own private model space,
 * and need to be shifted to their proper dispositions to create a proper scene.
 */
public interface IModelCollection {

    public Set<String> getSiloNames();
    public Mesh getSilo(String siloName);
    public int getNumberOfTrianglesInSilo(String siloName);
}
