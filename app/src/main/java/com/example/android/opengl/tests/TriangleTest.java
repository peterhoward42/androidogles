package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.geom.Triangle;
import com.example.android.opengl.geom.XYZf;

/**
 * Created by phoward on 12/01/2016.
 */
public class TriangleTest extends InstrumentationTestCase {


    public void testFormatRounded() throws Exception {
        XYZf a = new XYZf(0, 100, 0);
        XYZf b = new XYZf(0, 0, 100);
        XYZf c = new XYZf(0, 0, 0);
        Triangle triangle = new Triangle(a, b, c);
        assertEquals(
                "0.00000 100.00000 0.00000, 0.00000 0.00000 100.00000, 0.00000 0.00000 0.00000, 1.00000 0.00000 0.00000",
                triangle.formatRounded());
    }

    public void testGetNormalShouldBePlusX() throws Exception {
        // Create triangle that lies in ZY plane, and has CCW winding order that defines the
        // front faces as being the one facing the plux X direction. Check that the internally
        // calculated normal agrees with this direction, and has been normalised.
        XYZf a = new XYZf(0, 100, 0);
        XYZf b = new XYZf(0, 0, 100);
        XYZf c = new XYZf(0, 0, 0);
        Triangle triangle = new Triangle(a, b, c);
        XYZf normal = triangle.getNormal();
        String normalDiagnosticString = normal.formatRounded();
        assertEquals("1.00000 0.00000 0.00000", normalDiagnosticString);
    }
}
