package com.example.android.opengl.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A mesh comprising a collection of {@link com.example.android.opengl.geom.Triangle}.
 */
public class Mesh {

    private Collection<Triangle> triangles;

    public Mesh() {
        triangles = new ArrayList<Triangle>();
    }

    public void addTriangle(final Triangle newTriangle) {
        triangles.add(newTriangle);
    }

    public void addAllTriangles(final Mesh meshToAdd) {
        this.triangles.addAll(meshToAdd.getTriangles());
    }

    public final Collection<Triangle> getTriangles() {
        return triangles;
    }

    public final int getNumberOfTriangles() {
        return triangles.size();
    }
}
