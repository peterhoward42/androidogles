package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.primitives.XYZf;

/**
 * Created by phoward on 12/01/2016.
 */
public class XYZfTest extends InstrumentationTestCase {

    // These are in dependency order

    public void testFormatRounded() throws Exception {
        XYZf point = new XYZf(0.123454f, 100f, 0.123456f);
        String normalDiagnosticString = point.formatRounded();
        assertEquals("0.12345 100.00000 0.12346", normalDiagnosticString);
    }

    public void testResultantLength() throws Exception {
        XYZf point = new XYZf(1f, 1f, 1f);
        float length = point.resultantLength();
        String lengthDiagnosticString = String.format("%.5f", length);
        String expected = String.format("%.5f", Math.sqrt(3.0));
        assertEquals(expected, lengthDiagnosticString);
    }

    public void testPlusMinus() throws Exception {
        XYZf a = new XYZf(1, 1, 1);
        XYZf b = new XYZf(2, 2, 2);
        assertEquals("3.00000 3.00000 3.00000", a.plus(b).formatRounded());
        assertEquals("-1.00000 -1.00000 -1.00000", a.minus(b).formatRounded());
    }

    public void testNormalised() throws Exception {
        XYZf a = new XYZf(2, 2, 2);
        assertEquals("0.57735 0.57735 0.57735", a.normalised().formatRounded());
    }

    public void testDotProduct() throws Exception {
        XYZf a = new XYZf(1.0f, 0, 0).normalised();
        XYZf b = new XYZf(1.0f, 1.0f, 0).normalised();
        float dot = a.dotProduct(b);
        assertEquals("0.707", String.format("%.3f", dot));
    }

    public void testCrossProduct() throws Exception {
        // The cross product between X & Y vectors of length Q should be a Z vector
        // of length Q*Q.
        XYZf x = new XYZf(3,0,0);
        XYZf y = new XYZf(0,3,0);
        XYZf cross = x.crossProduct(y);
        assertEquals("0.00000 0.00000 9.00000", cross.formatRounded());
    }

    public void testNormalisedCrossProduct() throws Exception {
        // The normalised cross product between X & Y vectors of length Q should be
        // the unit-length Z vector.
        XYZf x = new XYZf(3,0,0);
        XYZf y = new XYZf(0,3,0);
        XYZf cross = x.normalisedCrossProduct(y);
        assertEquals("0.00000 0.00000 1.00000", cross.formatRounded());
    }
}
