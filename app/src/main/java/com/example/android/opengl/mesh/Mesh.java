package com.example.android.opengl.mesh;

import com.example.android.opengl.primitives.Triangle;
import com.example.android.opengl.primitives.XYZf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mesh comprising a collection of {@link MeshTriangle}.
 * With meta data about aggregate properties like bounding box.
 * In general, you build the mesh by adding {@link Triangle}s one at a time.
 * Each triangle has a surface normal in it, but the mesh is based on a wrapper type of
 * triangle called {@link MeshTriangle}, which also carries a seperate vertex normal for each
 * vertex. By default these are set trivially to be the same as the underlying triangle's surface
 * normal, but if you want the mesh to deduce which triangles belong to continuous
 * surfaces, then you can ask it to smooth the vertex normals. This will replace the vertex normals
 * with values influence by the surface normals of the adjacent triangles.
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
     * MeshTriangle that is is created are set to the same value as the surface normal of the
     * incoming primitive triangle.
     */
    public void addPrimitiveTriangle(final Triangle primitiveTriangle) {
        meshTriangles.add(new MeshTriangle(primitiveTriangle));
        updateBoundingBoxWithThisTriangle(primitiveTriangle);
    }

    /**
     * Add a mesh triangle to this mesh - preserving the individual vertex normals from the
     * incoming one.
     */
    public void addMeshTriangle(final MeshTriangle meshTriangle) {
        meshTriangles.add(meshTriangle);
    }

    public void appendAllTrianglesFromMesh(final Mesh meshToAdd) {
        for (MeshTriangle meshTriangle : meshToAdd.getTriangles()) {
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

    private void registerVertexInLookupTable(
            Map<String, List<XYZf>> verticesAtPosition,
            final String positionHash, final XYZf vertex) {
        if (verticesAtPosition.containsKey(positionHash) == false) {
            verticesAtPosition.put(positionHash, new ArrayList<XYZf>());
        }
        verticesAtPosition.get(positionHash).add(vertex);
    }

    private void updateBoundingBoxWithThisTriangle(final Triangle triangle) {
        for (XYZf vertex : triangle.vertices()) {
            mBoundingBox.addVertex(vertex);
        }
    }
}
