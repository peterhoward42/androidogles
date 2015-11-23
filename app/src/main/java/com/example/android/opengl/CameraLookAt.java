package com.example.android.opengl;

import android.opengl.Matrix;

/**
 * A camera with a fixed field of view, that is permanently bound to look at the centre of an
 * invariant spherical space given at construction time. Offers the service of providing
 * a world-to-clipspace perspective transform for any given camera position.
 * The perspective is derived from placing the near and far clip planes at the near
 * and far boundaries of the sphere.
 */
public class CameraLookAt {

    private final float FOV = 90;

    private XYZf mSphereCentre;
    private float mSphereRadius;
    private float mScreenAspectRatio;

    public CameraLookAt(XYZf sphereCentre, float sphereRadius, float screenAspectRatio) {
        mSphereCentre = sphereCentre;
        mSphereRadius = sphereRadius;
        mScreenAspectRatio = screenAspectRatio;
    }

    public float[] worldToClipTransform(XYZf cameraPosition) {
        float[] lookAtM = new float[16];
        // The "up" direction is hard coded as plusY
        Matrix.setLookAtM(lookAtM, 0, cameraPosition.X(), cameraPosition.Y(), cameraPosition.Z(),
                mSphereCentre.X(), mSphereCentre.Y(), mSphereCentre.Z(), 0, 1, 0);
        float[] projectionM = new float[16];
        float camToCentreDist = Matrix.length(
                mSphereCentre.X() - cameraPosition.X(),
                mSphereCentre.Y() - cameraPosition.Y(),
                mSphereCentre.Z() - cameraPosition.Z());
        // OpenGL demands that near and far planes are both positive and that far is greater
        // than near. So I think we think of them as unsigned?
        float zNear = camToCentreDist - mSphereRadius;
        float zFar = camToCentreDist + mSphereRadius;
        Matrix.perspectiveM(projectionM, 0, FOV, mScreenAspectRatio, zNear, zFar);
        float[] resultantM = new float[16];
        Matrix.multiplyMM(resultantM, 0, projectionM, 0, lookAtM, 0);
        return resultantM;
    }
}