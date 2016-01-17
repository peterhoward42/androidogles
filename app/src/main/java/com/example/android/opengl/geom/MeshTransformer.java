package com.example.android.opengl.geom;

/**
 * Knows how to make transformed copies of a {@link Mesh}
 */
public class MeshTransformer {

    public static Mesh transformedCopy(final float[] transform, final Mesh toTransform) {
        Mesh meshToReturn = new Mesh();
        for (Triangle triangle: toTransform.getTriangles()) {
            meshToReturn.addTriangle(TriangleManipulator.makeTransformedTriangle(transform, triangle));
        }
        return meshToReturn;
    }
}
