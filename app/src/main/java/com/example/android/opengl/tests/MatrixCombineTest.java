package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.MatrixCombiner;
import com.example.android.opengl.MatrixFormatter;
import com.example.android.opengl.TransformFactory;

/**
 * Created by phoward on 12/01/2016.
 */
public class MatrixCombineTest extends InstrumentationTestCase {

    // These are in dependency order

    public void testCombineTwo() throws Exception {
        // We combine two matrices that should cancel each other out, and ensure we end
        // up with the identity matrix.
        float[] a = TransformFactory.translation(1, 2, 3);
        float[] b = TransformFactory.translation(-1, -2, -3);
        float[] combined = MatrixCombiner.combineTwo(b, a);
        String diagnostic = MatrixFormatter.fmtMatrix4x4(combined);
        assertEquals(
                "1.00,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00",
                diagnostic);
    }

    public void testCombineThree() throws Exception {
        // We combine three matrices that should cancel each other out, and ensure we end
        // up with the identity matrix.
        float[] a = TransformFactory.translation(2, 4, 6);
        float[] b = TransformFactory.translation(-1, -2, -3);
        float[] c = TransformFactory.translation(-1, -2, -3);
        float[] combined = MatrixCombiner.combineThree(c, b, a);
        String diagnostic = MatrixFormatter.fmtMatrix4x4(combined);
        assertEquals(
                "1.00,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,1.00",
                diagnostic);
    }
}
