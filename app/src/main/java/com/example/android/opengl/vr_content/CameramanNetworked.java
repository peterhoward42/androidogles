package com.example.android.opengl.vr_content;

import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;
import com.example.android.opengl.threadsafe.ConcurrentXYZf;

/**
 * A {@link Cameraman} constrained to orbit the scene at a constant radius from the
 * centre - that takes continuously changing latitude and longitude guidance from an external
 * position injector. The demands on the external party are only that it writes positions to
 * the public injectedPosition field from time to time. (A threadsafe object).
 * The camera points always at the centre of the scene.
 */
public class CameramanNetworked implements Cameraman {

    private final static float HEIGHT_FACTOR = 1.1f;
    final Sphere mSphere;
    public ConcurrentXYZf injectedPosition;

    public CameramanNetworked(final Sphere sceneSphere) {
        this.mSphere = sceneSphere;
        this.injectedPosition = new ConcurrentXYZf(new XYZf(0, 0, 0));
    }

    @Override
    public ViewingAxis getCurrentViewpoint() {
        final XYZf position = evaluatePosition();
        final XYZf lineOfSight = mSphere.getCentre().minus(position);
        return new ViewingAxis(lineOfSight, position);
    }

    private XYZf evaluatePosition() {
        final float eastings = -30.0f + this.injectedPosition.synchronizedGet().X() * 60.0f;
        final float northings = 60.0f;
        final float[] transform = TransformFactory.eulerAngles(northings, eastings, 0);
        final XYZf pole = new XYZf(0, 0, mSphere.getRadius() * HEIGHT_FACTOR);
        final XYZf position = TransformApply.point(transform, pole);
        return position;
    }
}
