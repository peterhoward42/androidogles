package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;
import com.example.android.opengl.vr_content.CameramanOrbiting;

/**
 * Created by phoward on 12/01/2016.
 */
public class CameramanOrbitingTest extends InstrumentationTestCase {

    // These are in dependency order

    public void testEvaluatePosition() throws Exception {
        final XYZf centre = new XYZf(0,0,0);
        final float radius = 100;
        CameramanOrbiting onLooker = new CameramanOrbiting(new Sphere(centre, radius));
        // I have not verified or modelled this calculation. It seems to work in a real
        // application, and this will do for now as a regression test. Todo - do a proper
        // prediction-based test.
        final float theTimeToEvaluatePositionAt = 11.25f;
        final XYZf sampledPoint = onLooker.evaluatePosition(theTimeToEvaluatePositionAt);
        assertEquals("-50.80390 95.26280 46.52585", sampledPoint.formatRounded());
    }
}
