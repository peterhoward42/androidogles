package com.example.android.opengl;

import java.util.Collection;
import java.util.Stack;

/**
 * Offers the service of packing the vertices and face-normals from a collection of triangles into
 * a contiguous array of floats. The face-normal of the triangle is duplicated for each vertex.
 *
 */
public class TriangleSerializer {

    private Collection<Triangle> mTriangles;

    public TriangleSerializer(Collection<Triangle> triangles) {
        mTriangles = triangles;
    }

    public float[] serializeToContiguousFloats() {
        int numberOfVertices = 3 * mTriangles.size();
        int numberOfFloats = 6 * numberOfVertices; // 3 for position, 3 for normal vector
        float[] theFloats = new float[numberOfFloats];
        int i = 0;
        for (Triangle triangle: mTriangles) {
            XYZf faceNormal =  triangle.getNormal();
            for (XYZf vertex: triangle.vertices()) {
                theFloats[i++] = vertex.X();
                theFloats[i++] = vertex.Y();
                theFloats[i++] = vertex.Z();

                theFloats[i++] = faceNormal.X();
                theFloats[i++] = faceNormal.Y();
                theFloats[i++] = faceNormal.Z();
            }
        }
        return theFloats;
    }
}
