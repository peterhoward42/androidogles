package com.example.android.opengl.vr_content;

import android.os.SystemClock;

import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;

/**
 * A {@link Cameraman} who is orbiting the scene about the Y axis, with a constant, hard coded
 * latitude. With the camera pointing always at the centre of the scene.
 */
public class CameramanOrbiting implements Cameraman {

    private Sphere mSphere;

    private final static float ANGULAR_VELOCITY_EASTINGS = 0.30f; // radians/sec
    private final static float HEIGHT_FACTOR = 1.1f;

    public CameramanOrbiting(final Sphere sceneSphere) {
        this.mSphere = sceneSphere;
    }

    @Override
    public ViewingAxis getCurrentViewpoint() {
        final XYZf position = evaluatePosition((float) (SystemClock.uptimeMillis() / 1000.0));
        final XYZf lineOfSight = mSphere.getCentre().minus(position);
        return new ViewingAxis(lineOfSight, position);
    }

    /** This method is not intended to be called by clients, and exists only as a way to dependency
     * inject the time the path should be evaluated at, to make it easy to test.
     */
    public XYZf evaluatePosition(final float currentTimeSeconds) {
        final float eastings = (float)Math.toDegrees(ANGULAR_VELOCITY_EASTINGS * currentTimeSeconds);
        //final float northings = (float)Math.toDegrees(ANGULAR_VELOCITY_NORTHINGS * currentTimeSeconds);
        final float northings = 60.0f;
        final float[] transform = TransformFactory.eulerAngles(northings, eastings, 0);
        final XYZf pole = new XYZf(0, 0, mSphere.getRadius() * HEIGHT_FACTOR);
        final XYZf position = TransformApply.point(transform, pole);
        return position;
    }
}
