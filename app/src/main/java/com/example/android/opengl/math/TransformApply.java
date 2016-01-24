package com.example.android.opengl.math;

import android.opengl.Matrix;

import com.example.android.opengl.mesh.BoundingBox;
import com.example.android.opengl.primitives.XYZf;

/**
 * Created by phoward on 11/01/2016.
 */
public class TransformApply {

    /** Creates a transformed variant of a point.
     *
     */
    public static XYZf point(float[] transform, XYZf point) {
        float[] result = new float[4];
        Matrix.multiplyMV(
                result, 0, transform, 0, new float[]{point.X(), point.Y(), point.Z(), 1}, 0);
        return new XYZf(result[0], result[1], result[2]);
    }

    public static BoundingBox boundingBox(float[] transform, final BoundingBox box) {
        XYZf minimaVertex = point(transform, box.getMinima());
        XYZf maximaVertex = point(transform, box.getMaxima());
        return new BoundingBox(minimaVertex, maximaVertex);
    }

    /** Create a transformed variant of a direction vector.
     *
     */
    public static XYZf direction(float[] threeByThreeDirectionTransform, XYZf direction) {
        float[] expandedTransform =
                TransformFactory.expand3x3To4x4WithZeros(threeByThreeDirectionTransform);
        return point(expandedTransform, direction);
    }
}
