package com.example.android.opengl.mesh;

import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.primitives.Triangle;

/**
 * Created by phoward on 09/01/2016.
 */
public class TriangleManipulator {

    public static Triangle makeTransformedTriangle(
            float[] transform, Triangle triangleToTransform) {
        return new Triangle(
                TransformApply.point(transform, triangleToTransform.firstVertex()),
                TransformApply.point(transform, triangleToTransform.secondVertex()),
                TransformApply.point(transform, triangleToTransform.thirdVertex())
        );
    }

    public static Triangle toggleWindingOrder(final Triangle triangle) {
        return new Triangle(triangle.firstVertex(), triangle.thirdVertex(), triangle.secondVertex());
    }
}
