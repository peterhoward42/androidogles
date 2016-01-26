package com.example.android.opengl.vr_content;

import android.opengl.Matrix;

import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.primitives.XYZf;

/** Models both the direction a camera is pointing, and where it is. Can answer a few relevant
 * queries, for example providing a sample point that lies somewhere along the line of sight, or
 * providing the corresponding world-to-camera transform matrix.
 *
 */
public class ViewingAxis {
    private XYZf viewersPosition;
    private XYZf lineOfSightVector; // normalised on receipt in constructor

    public ViewingAxis(XYZf lineOfSightVector, XYZf viewersPosition) {
        this.lineOfSightVector = lineOfSightVector.normalised();
        this.viewersPosition = viewersPosition;
    }

    public XYZf getViewersPosition() {
        return viewersPosition;
    }

    public XYZf getLineOfSightVector() {
        return lineOfSightVector;
    }

    public XYZf getASamplePointInDirectLineOfSight() {
        float[] translateAlongLineOfSight = TransformFactory.translation(
                lineOfSightVector.X(), lineOfSightVector.Y(), lineOfSightVector.Z());
        XYZf samplePoint = TransformApply.point(translateAlongLineOfSight, viewersPosition);
        return samplePoint;
    }

    public float[] worldToCameraTransform() {
        float[] transform = new float[16];
        // The "up" direction is hard coded as plusY
        final XYZf lookAtPoint = getASamplePointInDirectLineOfSight();
        Matrix.setLookAtM(transform, 0, viewersPosition.X(), viewersPosition.Y(), viewersPosition.Z(),
                lookAtPoint.X(), lookAtPoint.Y(), lookAtPoint.Z(), 0, 1, 0);
        return transform;
    }
}
