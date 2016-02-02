package com.example.android.opengl.mesh;

import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.primitives.Triangle;
import com.example.android.opengl.primitives.XYZf;

/**
 * This class is a wrapper around a {@link com.example.android.opengl.primitives.Triangle} that
 * augments it with data about its context with other triangles in a mesh. Specifically,
 * each of the primitiveTriangle's vertices can have a local, or smoothed normal vector associated with it.
 * For example, when adjacent triangles exist in a mesh because they are adjacent facets of a
 * curved surface, then the vertex normals might be the blended normal direction between the two.
 */
public class MeshTriangle {

    private final Triangle primitiveTriangle;
    private XYZf[] smoothedVertexNormals;

    /** This constructor sets the smoothedVertexNormals as copies of the primitive triangle's
     *  surface normal. I.e. they are not smoothed.
     */
    public MeshTriangle(Triangle primitiveTriangle) {
        this.primitiveTriangle = primitiveTriangle;
        final XYZf sharedNormal = primitiveTriangle.getNormal();
        this.smoothedVertexNormals = new XYZf[]{sharedNormal, sharedNormal, sharedNormal};
    }

    /** This constructor sets the smoothedVertexNormals to the explicit values provided by
     * the caller.
     */
    public MeshTriangle(final Triangle primitiveTriangle, final XYZf[] smoothedVertexNormals) {
        this.primitiveTriangle = primitiveTriangle;
        this.smoothedVertexNormals = smoothedVertexNormals;
    }

    public MeshTriangle transformed(final float[] transform) {
        final Triangle primitiveTriangle = TriangleManipulator.makeTransformedTriangle(
                transform, getPrimitiveTriangle());
        XYZf[] smoothedVertexNormals = new XYZf[3];
        final float[] directionTransform = TransformFactory.directionTransformFromVertexTransform(transform);
        final XYZf[] originalNormals = this.getVertexNormals();
        for (int i = 0; i < 3; i++) {
            smoothedVertexNormals[i] = TransformApply.direction(directionTransform, originalNormals[i]);
        }
        return new MeshTriangle(primitiveTriangle, smoothedVertexNormals);
    }

    public Triangle getPrimitiveTriangle() {
        return primitiveTriangle;
    }

    public final XYZf[] getVertexNormals() {
        return smoothedVertexNormals;
    }

    public void overwriteVertexNormal(final int vertexIndex, final XYZf newNormal) {
        this.smoothedVertexNormals[vertexIndex] = newNormal;
    }
}