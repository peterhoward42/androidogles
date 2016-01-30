package com.example.android.opengl.mesh;

import com.example.android.opengl.primitives.XYZf;

/**
 * Models a single vertex that takes part in a mesh, in terms of an XYZ position coordinate,
 * but also, a local normal direction vector. It follows that several triangles might share such a
 * vertex if their relationship was such that the mesh normal is shared by the triangles. For
 * example a vertex in common between two
 * adjacent panels in the surface of a cylinder. However there might well be another vertex at
 * the same position, with a markedly different mesh normal where for example two surfaces meet
 * at right angles perhaps. This would require two different MeshVertex objects.
 */
public class MeshVertex {
    private final XYZf position;
    private final XYZf normal;

    public XYZf getPosition() {
        return position;
    }

    public XYZf getNormal() {
        return normal;
    }

    public MeshVertex(XYZf position, XYZf normal) {
        this.position = position;
        this.normal = normal;
    }

    /** This method provides a string representation of the object's fields, after
     * they have been rounded to 5 decimal places. The emphasis is on using the result as a hashing
     * value - that resolves two objects that differ only due to calculation rounding differences
     * and similar - into the same hash code.
     * @return
     */
    public final String hashAfterNumericalRounding() {
        return position.hashAfterNumericalRounding() + normal.hashAfterNumericalRounding();
    }
}
