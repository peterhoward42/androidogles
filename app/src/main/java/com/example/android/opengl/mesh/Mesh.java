package com.example.android.opengl.mesh;

import com.example.android.opengl.primitives.Triangle;
import com.example.android.opengl.primitives.XYZf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A mesh comprising a collection of {@link MeshTriangle}.
 * With meta data about aggregate properties like bounding box.
 */
public class Mesh {

    private Collection<MeshTriangle> meshTriangles;
    private BoundingBox mBoundingBox;

    public Mesh() {
        meshTriangles = new ArrayList<MeshTriangle>();
        mBoundingBox = new BoundingBox();
    }

    /**
     * Add a primitive triangle to this mesh. All three of the individual vertex normals on the
     MeshTriangle that is is created are set to the same value as the surface normal of the
     incoming primitive triangle.
     */
    public void addPrimitiveTriangle(final Triangle primitiveTriangle) {
        meshTriangles.add(new MeshTriangle(primitiveTriangle));
        updateBoundingBoxWithThisTriangle(primitiveTriangle);
    }

    /** Add a mesh triangle to this mesh - preserving the individual vertex normals from the
     * incoming one.
     */
    public void addMeshTriangle(final MeshTriangle meshTriangle) {
        meshTriangles.add(meshTriangle);
    }

    public void appendAllTrianglesFromMesh(final Mesh meshToAdd) {
        for (MeshTriangle meshTriangle: meshToAdd.getTriangles()) {
            addPrimitiveTriangle(meshTriangle.getPrimitiveTriangle());
        }
    }

    public final Collection<MeshTriangle> getTriangles() {
        return meshTriangles;
    }

    public final BoundingBox getBoundingBox() {
        return mBoundingBox;
    }

    public final int getNumberOfTriangles() {
        return meshTriangles.size();
    }

    private void updateBoundingBoxWithThisTriangle(final Triangle triangle) {
        for (XYZf vertex: triangle.vertices()) {
            mBoundingBox.addVertex(vertex);
        }
    }
}
