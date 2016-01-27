package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;
import com.example.android.opengl.vr_content.DynamicScene;
import com.example.android.opengl.vr_content.DynamicSceneCubes;
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
        final float theTimeToEvaluatePositionAt = 11.25f; // should be 1/4 along the third pass
        final XYZf sampledPoint = onLooker.evaluatePosition(theTimeToEvaluatePositionAt);
        assertEquals("125.00000 125.00000 125.00000", sampledPoint.formatRounded());
    }
}
