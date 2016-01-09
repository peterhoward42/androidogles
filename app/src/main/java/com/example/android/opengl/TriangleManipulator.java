package com.example.android.opengl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by phoward on 09/01/2016.
 */
public class TriangleManipulator {

    public static Triangle makeTransformedTriangle(
            float[] transform, Triangle triangleToTransform) {
        return new Triangle(
                MatrixHelper.transformPoint(transform, triangleToTransform.firstVertex()),
                MatrixHelper.transformPoint(transform, triangleToTransform.secondVertex()),
                MatrixHelper.transformPoint(transform, triangleToTransform.thirdVertex())
        );
    }

    public static Collection<Triangle> makeTransformedCopies(
            float[] transform, Collection<Triangle> trianglesToTransform) {
        Collection<Triangle> twoTriangles = new ArrayList<Triangle>();
        for (Triangle triangle : trianglesToTransform) {
            twoTriangles.add(TriangleManipulator.makeTransformedTriangle(transform, triangle));
        }
        return twoTriangles;
    }
}
