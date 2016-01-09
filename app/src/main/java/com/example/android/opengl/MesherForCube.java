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

        trianglesToReturn.addAll(TriangleManipulator.makeTransformedCopies(
                MatrixHelper.makeYAxisRotationMatrix(0), refTriangles));
        trianglesToReturn.addAll(TriangleManipulator.makeTransformedCopies(
                MatrixHelper.makeYAxisRotationMatrix(90), refTriangles));
        trianglesToReturn.addAll(TriangleManipulator.makeTransformedCopies(
                MatrixHelper.makeYAxisRotationMatrix(180), refTriangles));
        trianglesToReturn.addAll(TriangleManipulator.makeTransformedCopies(
                MatrixHelper.makeYAxisRotationMatrix(270), refTriangles));

        trianglesToReturn.addAll(TriangleManipulator.makeTransformedCopies(
                MatrixHelper.makeZAxisRotationMatrix(90), refTriangles));
        trianglesToReturn.addAll(TriangleManipulator.makeTransformedCopies(
                MatrixHelper.makeZAxisRotationMatrix(270), refTriangles));

        return trianglesToReturn;
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
}
