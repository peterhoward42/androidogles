package com.example.android.opengl.vr_content;

import android.os.SystemClock;

import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;

public class CameramanForWormAndWheel implements Cameraman {

    private final XYZf linearPathStart;
    private final XYZf linearPathEnd;
    private final float endToEndPeriod; // seconds
    private final XYZf lookAtPoint;

    final XYZf pathVector;
    final float lengthOfPath;

    public CameramanForWormAndWheel() {
        final float heightOfPath = 80;
        this.linearPathStart = new XYZf(200, heightOfPath, 200);
        this.linearPathEnd = new XYZf(40, heightOfPath, 0);
        this.endToEndPeriod = 20;
        this.lookAtPoint = new XYZf(-200, heightOfPath, -200);

        pathVector = linearPathEnd.minus(linearPathStart);
        lengthOfPath = pathVector.resultantLength();
    }

    @Override
    public ViewingAxis getCurrentViewpoint() {
        //final float timeToEvaluateAt = (float) (SystemClock.uptimeMillis() / 1000.0);
        final float timeToEvaluateAt = endToEndPeriod * 0.70f;
        final XYZf position = evaluatePosition(timeToEvaluateAt);
        final XYZf lineOfSight = lookAtPoint.minus(position);
        return new ViewingAxis(lineOfSight, position);
    }

    private XYZf evaluatePosition(final float currentTimeSeconds) {
        final float timeAlongPath = currentTimeSeconds % endToEndPeriod;
        final float distanceAlongPath = (timeAlongPath / endToEndPeriod) * lengthOfPath;
        final XYZf offsetVectorAlongPath = pathVector.vectorScaledToLength(distanceAlongPath);
        final XYZf position = linearPathStart.plus(offsetVectorAlongPath);
        return position;
    }
}
