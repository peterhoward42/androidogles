package com.example.android.opengl;

import android.opengl.Matrix;

/**
 * A camera with a fixed field of view, that is permanently bound to look at a point
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
}