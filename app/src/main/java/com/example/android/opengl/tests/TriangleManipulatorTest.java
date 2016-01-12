package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.TransformFactory;
import com.example.android.opengl.Triangle;
import com.example.android.opengl.TriangleManipulator;
import com.example.android.opengl.XYZf;

/**
 * Created by phoward on 12/01/2016.
 */
public class TriangleManipulatorTest extends InstrumentationTestCase {

    public void testMakeTransformedTriangle() throws Exception {
        XYZf a = new XYZf(0, 100, 0);
        XYZf b = new XYZf(0, 0, 100);
        XYZf c = new XYZf(0, 0, 0);
        Triangle before = new Triangle(a, b, c);
        float[] transform = TransformFactory.translation(10f, 0f, 0f);
        Triangle after = TriangleManipulator.makeTransformedTriangle(transform, before);
        assertEquals(
                "wait till transform factory validated",
                after.formatRounded());
    }
}
