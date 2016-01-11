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
                DoTransform.point(transform, triangleToTransform.firstVertex()),
                DoTransform.point(transform, triangleToTransform.secondVertex()),
                DoTransform.point(transform, triangleToTransform.thirdVertex())
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
