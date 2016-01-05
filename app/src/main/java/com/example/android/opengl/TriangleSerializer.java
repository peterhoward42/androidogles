package com.example.android.opengl;

import java.util.Collection;
import java.util.Stack;

/**
 * Created by phoward on 05/01/2016.
 */
public class TriangleSerializer {

    private Collection<Triangle> mTriangles;

    public TriangleSerializer(Collection<Triangle> triangles) {
        mTriangles = triangles;
    }

    public float[] serializeToContiguousFloats() {
        int numberOfVertices = 3 * mTriangles.size();
        int numberOfFloats = 3 * numberOfVertices;
        float[] theFloats = new float[numberOfFloats];
        int i = 0;
        for (Triangle triangle: mTriangles) {
            for (XYZf vertex: triangle.vertices()) {
                theFloats[i++] = vertex.X();
                theFloats[i++] = vertex.Y();
                theFloats[i++] = vertex.Z();
            }
        }
        return theFloats;
    }
}
