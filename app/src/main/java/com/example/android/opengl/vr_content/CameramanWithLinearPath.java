package com.example.android.opengl.vr_content;

import android.os.SystemClock;

import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;

/**
 * A {@link Cameraman} who is travelling along a linear path (like a road), but who keeps the
 * camera pointing always at a fixed point in the scene.
 */
public class CameramanWithLinearPath implements Cameraman {

    private final XYZf linearPathStart;
    private final XYZf linearPathEnd;
    private final float endToEndPeriod; // seconds
    private final XYZf lookAtPoint;

    final XYZf pathVector;
    final float lengthOfPath;

    public XYZf getLinearPathStart() {
        return linearPathStart;
    }

    public XYZf getLinearPathEnd() {
        return linearPathEnd;
    }

    public XYZf getLookAtPoint() {
        return lookAtPoint;
    }

    public CameramanWithLinearPath(
            final XYZf linearPathStart, final XYZf linearPathEnd,
            final float endToEndPeriod, final XYZf lookAtPoint) {
        this.linearPathStart = linearPathStart;
        this.linearPathEnd = linearPathEnd;
        this.endToEndPeriod = endToEndPeriod;
        this.lookAtPoint = lookAtPoint;

        pathVector = linearPathEnd.minus(linearPathStart);
        lengthOfPath = pathVector.resultantLength();
    }

    @Override
    public ViewingAxis getCurrentViewpoint() {
        final XYZf position = evaluatePosition((float) (SystemClock.uptimeMillis() / 1000.0));
        final XYZf lineOfSight = lookAtPoint.minus(position);
        return new ViewingAxis(lineOfSight, position);
    }

    /** This is a factory method that creates a
     * {@link CameramanWithLinearPath}
     * by inferring a suitable pathway based on the geometry of the scene provided.
     */
    public static CameramanWithLinearPath makeOnLookerWithDefaultSettings(DynamicScene dynamicScene) {
        // Let us traverse the z axis through the middle of the scene's sphere from some way in
        // front into the centre of it. Keeping our look at point at the rear, left, bottom corner.

        final float TRAVERSAL_DURATION = 10.0f; // seconds
        final Sphere sphere = dynamicScene.getCurrentEffectiveSphere();
        final float rad = sphere.getRadius();
        final XYZf trackStartOffset = new XYZf(0, 0, 3.0f * rad);
        final XYZf trackStart = sphere.getCentre().plus(trackStartOffset);

        final XYZf lookAtPointOffset = new XYZf(-rad, -rad, -rad);

        final XYZf lookAtPoint = sphere.getCentre().plus(lookAtPointOffset);
        return new CameramanWithLinearPath(
                trackStart, sphere.getCentre(), TRAVERSAL_DURATION, lookAtPoint);
    }

    /** This method is not intended to be called by clients, and exists only as a way to dependency
     * inject the time the path should be evaluated at, to make it easy to test.
     */
    public XYZf evaluatePosition(final float currentTimeSeconds) {
        final float timeAlongPath = currentTimeSeconds % endToEndPeriod;
        final float distanceAlongPath = (timeAlongPath / endToEndPeriod) * lengthOfPath;
        final XYZf offsetVectorAlongPath = pathVector.vectorScaledToLength(distanceAlongPath);
        final XYZf position = linearPathStart.plus(offsetVectorAlongPath);
        return position;
    }
}
