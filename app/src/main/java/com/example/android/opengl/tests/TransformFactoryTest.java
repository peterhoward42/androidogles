package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.MatrixCombiner;
import com.example.android.opengl.MatrixFormatter;
import com.example.android.opengl.TransformFactory;

/**
 * Created by phoward on 12/01/2016.
 */
public class TransformFactoryTest extends InstrumentationTestCase {

    // These are in dependency order

    public void testIdentity() throws Exception {
        float[] t = TransformFactory.identity();
        assertEquals(
                "1.00,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00",
                MatrixFormatter.fmtMatrix4x4(t));
    }

    public void testTranslation() throws Exception {
        float[] t = TransformFactory.translation(1.0f, 2.0f, 3.0f);
        assertEquals(
                "1.00,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00,0.00,1.00,2.00,3.00,1.00",
                MatrixFormatter.fmtMatrix4x4(t));
    }

    public void testYAxisRotation() throws Exception {
        float[] t = TransformFactory.yAxisRotation(30);
        assertEquals(
                "0.87,0.00,-0.50,0.00,0.00,1.00,0.00,0.00,0.50,0.00,0.87,0.00,0.00,0.00,0.00,1.00",
                MatrixFormatter.fmtMatrix4x4(t));
    }

    public void testZAxisRotation() throws Exception {
        float[] t = TransformFactory.zAxisRotation(30);
        assertEquals(
                "0.87,0.50,0.00,0.00,-0.50,0.87,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00",
                MatrixFormatter.fmtMatrix4x4(t));
    }

    public void testInverted() throws Exception {
        float[] toInvert = TransformFactory.translation(3.0f, 0.0f, 0.0f);
        float[] t = TransformFactory.inverted(toInvert);
        assertEquals(
                "1.00,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00,0.00,-3.00,0.00,0.00,1.00",
                MatrixFormatter.fmtMatrix4x4(t));
    }

    public void testTransposed() throws Exception {
        float[] toTranspose = TransformFactory.translation(1f, 0f, 0f);
        float[] t = TransformFactory.transposed(toTranspose);
        assertEquals(
                "1.00,0.00,0.00,1.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00",
                MatrixFormatter.fmtMatrix4x4(t));
    }
}
