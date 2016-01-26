package com.example.android.opengl.vr_content;

import android.opengl.Matrix;

import com.example.android.opengl.primitives.XYZf;
import com.example.android.opengl.math.TransformFactory;

/**
 * Responsible for the optical presentation of the scene. Ie. camera position, properties and
 * lighting etc.
 */
public class SceneOpticsDeprecated {

    private CameraLookAt mCameraLookAt;

    // Hard-coded lighting direction
    private final XYZf towardsLightDirection = new XYZf(15f, 8f, 10f).normalised();

    private final float mCameraOrbitalHeight;
    private final float mNear;
    private final float mFar;

    public SceneOpticsDeprecated(final float sceneSphericalRadiusApprox) {
        // Use hard coded camera look at point for now - ie the origin.
        mCameraLookAt = new CameraLookAt(new XYZf(0f, 0f, 0f));
        // Set the camera orbital height as a function of the scene's effective radius
        mCameraOrbitalHeight = 2.5f * sceneSphericalRadiusApprox; // empirically satisfactory
        mNear = mCameraOrbitalHeight - sceneSphericalRadiusApprox;
        mFar = mCameraOrbitalHeight + sceneSphericalRadiusApprox;
    }

    public float[] calculateCurrentWorldToCameraTransform() {
        return mCameraLookAt.worldToCameraTransform(getCurrentCameraPosition());
    }

    public float[] calculateProjectionTransform(float screenAspect) {
        final float fieldOfViewDegrees = 90;
        final float near = 120;
        final float far = 280;
        return TransformFactory.perspective(fieldOfViewDegrees, screenAspect, mNear, mFar);
    }

    public XYZf getTowardsLightDirection() {
        return towardsLightDirection;
    }

    private XYZf getCurrentCameraPosition() {
        // Centred over Greenwich, oscillating with SHM East and West.
        final float period = 4; // seconds
        final float amplitude = 35; // degrees
        final float latitude = 51.4f; // degrees
        float[] equatorAtMeridian = new float[]{0, 0, mCameraOrbitalHeight, 1};

        float longitude = -30.0f;
        //float longitude = new TimeBasedSinusoid(amplitude, period).evaluateAtTimeNow();

        float[] eulerRotationsMatrix = new float[16];
        Matrix.setRotateEulerM(eulerRotationsMatrix, 0, latitude, longitude, 0);
        float[] positionVect = new float[4];
        Matrix.multiplyMV(positionVect, 0, eulerRotationsMatrix, 0, equatorAtMeridian, 0);
        return new XYZf(positionVect[0], positionVect[1], positionVect[2]);
    }
}
