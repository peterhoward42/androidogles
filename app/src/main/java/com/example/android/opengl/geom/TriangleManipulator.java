package com.example.android.opengl.geom;

import com.example.android.opengl.math.TransformApply;

import java.util.ArrayList;
import java.util.Collection;

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
}
