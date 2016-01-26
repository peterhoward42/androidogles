package com.example.android.opengl.vr_content;

import android.os.SystemClock;

import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;

/**
 * An onlooker who is travelling along a linear path (like a road), but who maintains their gaze
 * at a fixed point in space. Their movement wraps back to the start and repeats.
 *
 * The name borrows from the term used to describe motorists travelling along a road who slow down
 * and stare fixedly at an accident.
 */
public class OnLookerRubberNeck implements OnLooker {

    private final XYZf linearPathStart;
    private final XYZf linearPathEnd;
    private final float endToEndPeriod; // seconds
    private final XYZf lookAtPoint;

    final XYZf pathVector;
    final float lengthOfPath;

    public OnLookerRubberNeck(
            final XYZf linearPathStart, final XYZf linearPathEnd,
            final float endToEndPeriod, final XYZf lookAtPoint) {
        this.linearPathStart = linearPathStart;
        this.linearPathEnd = linearPathEnd;
        this.endToEndPeriod = endToEndPeriod;
        this.lookAtPoint = lookAtPoint;

        pathVector = linearPathEnd.minus(linearPathStart);
        lengthOfPath = pathVector.resultantLength();
    }

    // We expose this mainly to make unit testing easier by dependency injecting a controlled
    // time rather than using the current uptime.
    public XYZf evaluatePosition(final float currentTimeSeconds) {
        final float timeAlongPath = currentTimeSeconds % endToEndPeriod;
        final float distanceAlongPath = (timeAlongPath / endToEndPeriod) * lengthOfPath;
        final XYZf offsetVectorAlongPath = pathVector.vectorScaledToLength(distanceAlongPath);
        final XYZf position = linearPathStart.plus(offsetVectorAlongPath);
        return position;
    }

    @Override
    public Viewpoint getCurrentViewpoint() {
        final XYZf position = evaluatePosition((float) (SystemClock.uptimeMillis() / 1000.0));
        final XYZf lineOfSight = lookAtPoint.minus(position);
        return new Viewpoint(lineOfSight, position);
    }

    /** This is a factory method that creates a
     * {@link com.example.android.opengl.vr_content.OnLookerRubberNeck}
     * by inferring a suitable pathway based on the geometry of the scene provided.
     */
    public static OnLookerRubberNeck makeOnLookerWithDefaultSettings(DynamicScene dynamicScene) {
        // Let us traverse the z axis through the middle of the scene's sphere from some way in
        // front into the centre of it. Keeping our look at point at the rear, left, bottom corner.

        final float TRAVERSAL_DURATION = 10.0f; // seconds
        final Sphere sphere = dynamicScene.getCurrentEffectiveSphere();
        final float rad = sphere.getRadius();
        final XYZf trackStartOffset = new XYZf(0, 0, 3.0f * rad);
        final XYZf trackStart = sphere.getCentre().plus(trackStartOffset);

        final XYZf lookAtPointOffset = new XYZf(-rad, -rad, -rad);

        final XYZf lookAtPoint = sphere.getCentre().plus(lookAtPointOffset);
        return new OnLookerRubberNeck(
                trackStart, sphere.getCentre(), TRAVERSAL_DURATION, lookAtPoint);
    }
}
