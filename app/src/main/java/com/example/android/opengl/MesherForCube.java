package com.example.android.opengl;

import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Capable of providing a set of @link com.example.android.opengl.Triangle Triangles
 * that wrap the surfaces of a given cuboid.
 */
public class MesherForCube {

    private float mHalfWidth;

    public MesherForCube(float acrossFlats) {
        mHalfWidth = 0.5f * acrossFlats;
    }

    public Collection<Triangle> getTriangles() {
        Collection<Triangle> triangles = new ArrayList<Triangle>();

        // Make the two triangles that comprise the right-facing square as a reference model
        Collection<Triangle> refTriangles = makeReferenceTriangles();

        // Now make transformed copies for each of the 6 faces

        Collection<Triangle> trianglesToReturn = new ArrayList<Triangle>();

        trianglesToReturn.addAll(makeTransformedCopies(rotationY(0), refTriangles));
        trianglesToReturn.addAll(makeTransformedCopies(rotationY(90), refTriangles));
        trianglesToReturn.addAll(makeTransformedCopies(rotationY(180), refTriangles));
        trianglesToReturn.addAll(makeTransformedCopies(rotationY(270), refTriangles));

        trianglesToReturn.addAll(makeTransformedCopies(rotationZ(90), refTriangles));
        trianglesToReturn.addAll(makeTransformedCopies(rotationZ(270), refTriangles));

        return trianglesToReturn;
    }

    private Collection<Triangle> makeTransformedCopies(
            float[] transform, Collection<Triangle> trianglesToTransform) {
        Collection<Triangle> twoTriangles = new ArrayList<Triangle>();
        for (Triangle triangle : trianglesToTransform) {
            twoTriangles.add(makeTransformedTriangle(transform, triangle));
        }
        return twoTriangles;
    }

    private Triangle makeTransformedTriangle(
            float[] transform, Triangle triangleToTransform) {
        return new Triangle(
                transformVertex(transform, triangleToTransform.firstVertex()),
                transformVertex(transform, triangleToTransform.secondVertex()),
                transformVertex(transform, triangleToTransform.thirdVertex())
        );
    }

    private XYZf transformVertex(float[] transform, XYZf vertex) {
        float[] result = new float[4];
        Matrix.multiplyMV(
                result, 0, transform, 0, new float[]{vertex.X(), vertex.Y(), vertex.Z(), 0}, 0);
        return new XYZf(result[0], result[1], result[2]);
    }

    private Collection<Triangle> makeReferenceTriangles() {
        // We use the right facing (+X facing) face as our reference model.
        Collection<Triangle> twoTriangles = new ArrayList<Triangle>();
        twoTriangles.add(new Triangle(
                new XYZf(+mHalfWidth, -mHalfWidth, -mHalfWidth),
                new XYZf(+mHalfWidth, +mHalfWidth, +mHalfWidth),
                new XYZf(+mHalfWidth, -mHalfWidth, +mHalfWidth)));

        twoTriangles.add(new Triangle(
                new XYZf(+mHalfWidth, +mHalfWidth, +mHalfWidth),
                new XYZf(+mHalfWidth, -mHalfWidth, -mHalfWidth),
                new XYZf(+mHalfWidth, +mHalfWidth, -mHalfWidth)));

        return twoTriangles;
    }

    private float[] rotationY(float angleDeg) {
        float[] m = new float[16];
        Matrix.setRotateM(m, 0, angleDeg, 0, 1, 0);
        return m;
    }

    private float[] rotationZ(float angleDeg) {
        float[] m = new float[16];
        Matrix.setRotateM(m, 0, angleDeg, 0, 0, 1);
        return m;
    }


}
