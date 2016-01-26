package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.primitives.XYZf;
import com.example.android.opengl.vr_content.ViewingAxis;

/**
 * Created by phoward on 12/01/2016.
 */
public class ViewingAxisTest extends InstrumentationTestCase {

    public void testGetASamplePointInDirectLineOfSight() throws Exception {
        final XYZf lineOfSightVector = new XYZf(100, 0, 0);
        final XYZf viewersPosition = new XYZf(1,2,3);
        final ViewingAxis viewingAxis = new ViewingAxis(lineOfSightVector, viewersPosition);
        final XYZf samplePointInDirectLineOfSight = viewingAxis.getASamplePointInDirectLineOfSight();
        assertEquals("2.00000 2.00000 3.00000", samplePointInDirectLineOfSight.formatRounded());
    }

    public void testWorldToCameraTransform() throws Exception {
        // The camera is situated at 1,1,1 and is looking in the direction of the
        // +X axis.

        // So if we apply the transform offered to a world point at 2,1,1, we will expect it
        // produce the camera-relative point of 0,0-1
        final XYZf lineOfSightVector = new XYZf(100, 0, 0);
        final XYZf viewersPosition = new XYZf(1,1,1);
        final ViewingAxis viewingAxis = new ViewingAxis(lineOfSightVector, viewersPosition);
        final float[] t = viewingAxis.worldToCameraTransform();
        final XYZf worldPoint = new XYZf(2,1,1);
        final XYZf cameraPoint = TransformApply.point(t, worldPoint);
        assertEquals("0.00000 0.00000 -1.00000", cameraPoint.formatRounded());
    }
}
