package com.example.android.opengl.mesh;

import com.example.android.opengl.primitives.Triangle;

/**
 * Knows how to make transformed copies of a {@link Mesh}
 */
public class MeshTransformer {

    public static Mesh transformedCopy(final float[] transform, final Mesh meshToTransform) {
        Mesh meshToReturn = new Mesh();
        for (MeshTriangle meshTriangle: meshToTransform.getTriangles()) {
            meshToReturn.addMeshTriangle(meshTriangle.transformed(transform));
        }
        return meshToReturn;
    }
}
