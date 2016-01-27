package com.example.android.opengl.vr_content;

import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;

public class CameramanStatic implements Cameraman {

    private Sphere mSphere;
    private final static float CAM_HEIGHT_FACTOR = 1.25f;

    public CameramanStatic(final Sphere timeInvariantSceneSphere) {
        mSphere = timeInvariantSceneSphere;
    }

    @Override
    public ViewingAxis getCurrentViewpoint() {
        // We will place the camera in front of the scene, offset to the right and upwards.
        // And we will look diagonally at the scene centre.

        final float distanceOfCameraFromCentre = mSphere.getRadius() * CAM_HEIGHT_FACTOR;
        final XYZf sceneCentreToCamera = new XYZf(1, 1, 1).vectorScaledToLength(distanceOfCameraFromCentre);
        final XYZf cameraPosition = mSphere.getCentre().plus(sceneCentreToCamera);

        final XYZf lineOfSightVector = mSphere.getCentre().minus(cameraPosition);

        return new ViewingAxis(lineOfSightVector, cameraPosition);
    }
}
