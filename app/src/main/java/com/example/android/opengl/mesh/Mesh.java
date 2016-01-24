package com.example.android.opengl.mesh;

import com.example.android.opengl.primitives.Triangle;
import com.example.android.opengl.primitives.XYZf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A mesh comprising a collection of {@link Triangle}.
 * With meta data about aggregate properties like bounding box.
 */
public class Mesh {

    private Collection<Triangle> triangles;
    private BoundingBox mBoundingBox;

    public Mesh() {
        triangles = new ArrayList<Triangle>();
        mBoundingBox = new BoundingBox();
    }

    public void addTriangle(final Triangle newTriangle) {
        triangles.add(newTriangle);
        updateBoundingBoxWithThisTriangle(newTriangle);
    }

    public void appendAllTrianglesFromMesh(final Mesh meshToAdd) {
        for (Triangle triangle: meshToAdd.getTriangles()) {
            addTriangle(triangle);
        }
    }

    public final Collection<Triangle> getTriangles() {
        return triangles;
    }

    public final BoundingBox getBoundingBox() {
        return mBoundingBox;
    }

    public final int getNumberOfTriangles() {
        return triangles.size();
    }

    private void updateBoundingBoxWithThisTriangle(final Triangle triangle) {
        for (XYZf vertex: triangle.vertices()) {
            mBoundingBox.addVertex(vertex);
        }
    }
}
