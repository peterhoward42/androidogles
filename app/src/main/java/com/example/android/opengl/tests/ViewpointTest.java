package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.primitives.XYZf;
import com.example.android.opengl.vr_content.Viewpoint;

/**
 * Created by phoward on 12/01/2016.
 */
public class ViewpointTest extends InstrumentationTestCase {

    public void testGetASamplePointInDirectLineOfSight() throws Exception {
        final XYZf lineOfSightVector = new XYZf(100, 0, 0);
        final XYZf viewersPosition = new XYZf(1,2,3);
        final Viewpoint viewpoint = new Viewpoint(lineOfSightVector, viewersPosition);
        final XYZf samplePointInDirectLineOfSight = viewpoint.getASamplePointInDirectLineOfSight();
        assertEquals("2.00000 2.00000 3.00000", samplePointInDirectLineOfSight.formatRounded());
    }
}
