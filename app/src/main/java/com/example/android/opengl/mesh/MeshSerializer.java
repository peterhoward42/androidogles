package com.example.android.opengl.mesh;

import com.example.android.opengl.primitives.Triangle;
import com.example.android.opengl.primitives.XYZf;

/**
 * Offers the service of packing the vertices and vertex-normals from a Mesh into
 * a contiguous array of floats.
 *
 */
public class MeshSerializer {

    private Mesh mesh;

    public MeshSerializer(Mesh mesh) {
        this.mesh = mesh;
    }

    public float[] serializeToContiguousFloats() {
        int numberOfVertices = 3 * mesh.getNumberOfTriangles();
        int numberOfFloats = 6 * numberOfVertices; // 3 for position, 3 for normal vector
        float[] theFloats = new float[numberOfFloats];
        int i = 0;
        for (MeshTriangle meshTriangle: mesh.getTriangles()) {
            XYZf[] vertices = meshTriangle.getPrimitiveTriangle().vertices();
            XYZf[] normals = meshTriangle.getVertexNormals();

            for (int vertexIndex = 0; vertexIndex < 3; vertexIndex++) {
                XYZf vertex = vertices[vertexIndex];
                XYZf normal = normals[vertexIndex];

                theFloats[i++] = vertex.X();
                theFloats[i++] = vertex.Y();
                theFloats[i++] = vertex.Z();

                theFloats[i++] = normal.X();
                theFloats[i++] = normal.Y();
                theFloats[i++] = normal.Z();
            }
        }
        return theFloats;
    }
}
