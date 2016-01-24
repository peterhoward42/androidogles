package com.example.android.opengl.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.example.android.opengl.application.OpenGLES20Activity;
import com.example.android.opengl.geom.BoundingBox;
import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.math.MatrixFormatter;
import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.math.TransformFactory;

/**
 * Created by phoward on 12/01/2016.
 */

public class TransformApplyTest extends ActivityInstrumentationTestCase2<OpenGLES20Activity> {

    public TransformApplyTest() {
        super(OpenGLES20Activity.class);
    }

    public void testPoint() throws Exception {
        XYZf origPoint = new XYZf(0,0,0);
        float[] transform = TransformFactory.translation(3,0,0);
        XYZf transformedPoint = TransformApply.point(transform, origPoint);
        assertEquals("3.00000 0.00000 0.00000", transformedPoint.formatRounded());
    }

    public void testBoundingBox() throws Exception {
        BoundingBox origBox = new BoundingBox(new XYZf(0,0,0), new XYZf(1,1,1));
        float[] transform = TransformFactory.translation(3, 0, 0);
        BoundingBox newBox = TransformApply.boundingBox(transform, origBox);
        assertEquals("3.00000 0.00000 0.00000", newBox.getMinima().formatRounded());
        assertEquals("4.00000 1.00000 1.00000", newBox.getMaxima().formatRounded());
    }
}
