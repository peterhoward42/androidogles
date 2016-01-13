package com.example.android.opengl.vr_content;

import android.opengl.Matrix;

import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.math.TimeBasedSinusoid;
import com.example.android.opengl.math.TransformFactory;

/**
 * Responsible for the optical presentation of the scene. Ie. camera position, properties and
 * lighting etc.
 */
public class SceneOptics {

    private CameraLookAt mCameraLookAt;

    public SceneOptics() {
        // Use hard coded look at point for now - ie the origin.
        mCameraLookAt = new CameraLookAt(new XYZf(0f, 0f, 0f));
    }

    public float[] calculateWorldToCameraTransform() {
        return mCameraLookAt.worldToCameraTransform(getCurrentCameraPosition());
    }

    public float[] calculateProjectionTransform(float screenAspect) {
        final float fieldOfViewDegrees = 90;
        final float near = 120;
        final float far = 280;
        return TransformFactory.perspective(fieldOfViewDegrees, screenAspect, near, far);
    }

    private XYZf getCurrentCameraPosition() {
        // Centred over Greenwich, oscillating with SHM East and West.
        final float period = 2; // seconds
        final float amplitude = 35; // degrees
        final float latitude = 51.4f; // degrees
        final float orbitHeightFromCentre = 200; // scene linear dimensions
        float[] equatorAtMeridian = new float[]{0, 0, orbitHeightFromCentre, 1};

        float longitude = new TimeBasedSinusoid(amplitude, period).evaluateAtTimeNow();

        float[] eulerRotationsMatrix = new float[16];
        Matrix.setRotateEulerM(eulerRotationsMatrix, 0, latitude, longitude, 0);
        float[] positionVect = new float[4];
        Matrix.multiplyMV(positionVect, 0, eulerRotationsMatrix, 0, equatorAtMeridian, 0);
        return new XYZf(positionVect[0], positionVect[1], positionVect[2]);
    }
}
