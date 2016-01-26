package com.example.android.opengl.vr_content;

import android.opengl.Matrix;

import com.example.android.opengl.primitives.XYZf;

/**
 * A roving camera that always looks at a point
 * given at construction time. Offers the service of providing
 * a world-to-camera-space transform for any given camera position.
 */
public class CameraLookAt {

    private XYZf mLookAtPoint;

    public CameraLookAt(XYZf lookAtPoint) {
        mLookAtPoint = lookAtPoint;
    }

    public float[] worldToCameraTransform(XYZf cameraPosition) {
        float[] transform = new float[16];
        // The "up" direction is hard coded as plusY
        Matrix.setLookAtM(transform, 0, cameraPosition.X(), cameraPosition.Y(), cameraPosition.Z(),
                mLookAtPoint.X(), mLookAtPoint.Y(), mLookAtPoint.Z(), 0, 1, 0);
        return transform;
    }

    public static float[] worldToCameraTransform(final Viewpoint viewpoint) {
        float[] transform = new float[16];
        // The "up" direction is hard coded as plusY
        final XYZf cameraPosition = viewpoint.getViewersPosition();
        final XYZf lookAtPoint = viewpoint.getASamplePointInDirectLineOfSight();
        Matrix.setLookAtM(transform, 0, cameraPosition.X(), cameraPosition.Y(), cameraPosition.Z(),
                lookAtPoint.X(), lookAtPoint.Y(), lookAtPoint.Z(), 0, 1, 0);
        return transform;
    }
}