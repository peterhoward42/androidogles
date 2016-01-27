package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.primitives.XYZf;
import com.example.android.opengl.vr_content.DynamicScene;
import com.example.android.opengl.vr_content.DynamicSceneCubes;
import com.example.android.opengl.vr_content.CameramanWithLinearPath;

/**
 * Created by phoward on 12/01/2016.
 */
public class CameramanWithLinearPathTest extends InstrumentationTestCase {

    // These are in dependency order

    public void testEvaluatePosition() throws Exception {
        final XYZf start = new XYZf(100,100,100);
        final XYZf end = new XYZf(200, 200, 200);
        final float traverseDuration = 5; // seconds
        final XYZf lookAt = null; // unused for this test
        CameramanWithLinearPath onLooker = new CameramanWithLinearPath(start, end, traverseDuration, lookAt);
        final float theTimeToEvaluatePositionAt = 11.25f; // should be 1/4 along the third pass
        final XYZf sampledPoint = onLooker.evaluatePosition(theTimeToEvaluatePositionAt);
        assertEquals("125.00000 125.00000 125.00000", sampledPoint.formatRounded());
    }

    public void testMakeOnLookerWithDefaultSettings() throws Exception {
        DynamicScene dynamicScene = new DynamicSceneCubes();
        CameramanWithLinearPath onLooker =
                CameramanWithLinearPath.makeWithDefaultSettings(dynamicScene);
        assertEquals("0.00000 0.00000 255.00000", onLooker.getLinearPathStart().formatRounded());
        assertEquals("0.00000 0.00000 0.00000", onLooker.getLinearPathEnd().formatRounded());
        assertEquals("-85.00000 -85.00000 -85.00000", onLooker.getLookAtPoint().formatRounded());
    }
}
