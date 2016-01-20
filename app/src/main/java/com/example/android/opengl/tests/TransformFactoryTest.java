package com.example.android.opengl.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.example.android.opengl.application.OpenGLES20Activity;
import com.example.android.opengl.math.MatrixFormatter;
import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.geom.XYZf;

/**
 * Created by phoward on 12/01/2016.
 */

public class TransformFactoryTest extends ActivityInstrumentationTestCase2<OpenGLES20Activity> {

    public TransformFactoryTest() {
        super(OpenGLES20Activity.class);
    }

    // These are in dependency order

    public void testIdentity() throws Exception {
        float[] t = TransformFactory.identity();
        XYZf p = new XYZf(2.0f, 3.0f, 4.0f);
        XYZf q = TransformApply.point(t, p);
        assertEquals("2.00000 3.00000 4.00000", q.formatRounded());
    }

    public void testInverted() throws Exception {
        float[] t = TransformFactory.translation(1f, 2f, 3f);
        float[] t2 = TransformFactory.inverted(t);
        XYZf p = new XYZf(0, 0, 0);
        XYZf q = TransformApply.point(t2, p);
        // pity about the minus zero :-(
        assertEquals("-1.00000 -2.00000 -3.00000", q.formatRounded());
    }

    public void testTransposed() throws Exception {
        float[] t = {
                1f, 2f, 3f, 4f,
                5f, 6f, 7f, 8f,
                9f, 10f, 11f, 12f,
                13f, 14f, 15f, 16f};
        float[] transposed = TransformFactory.transposed(t);
        assertEquals(
                "1.00,5.00,9.00,13.00,2.00,6.00,10.00,14.00,3.00,7.00,11.00,15.00,4.00,8.00,12.00,16.00",
                MatrixFormatter.fmtMatrix4x4(transposed));
    }

    public void testisolate3x3From4x4() throws Exception {
        float[] t = {
                1f, 2f, 3f, 4f,
                5f, 6f, 7f, 8f,
                9f, 10f, 11f, 12f,
                13f, 14f, 15f, 16f};
        float[] isolated = TransformFactory.isolate3x3From4x4(t);
        assertEquals(
                "1.00,2.00,3.00,5.00,6.00,7.00,9.00,10.00,11.00",
                MatrixFormatter.fmtMatrix3x3(isolated));
    }

    public void testExpand3x3To4x4WithZeros() throws Exception {
        float[] t = {
                1f, 2f, 3f,
                4f, 5f, 6f,
                7f, 8f, 9f,};
        float[] expanded = TransformFactory.expand3x3To4x4WithZeros(t);
        assertEquals(
                "1.00,2.00,3.00,0.00,4.00,5.00,6.00,0.00,7.00,8.00,9.00,0.00,0.00,0.00,0.00,0.00",
                MatrixFormatter.fmtMatrix4x4(expanded));
    }

    public void testTranslation() throws Exception {
        float[] t = TransformFactory.translation(5.0f, 6.0f, 7.0f);
        XYZf p = new XYZf(2.0f, 3.0f, 4.0f);
        XYZf q = TransformApply.point(t, p);
        assertEquals("7.00000 9.00000 11.00000", q.formatRounded());
    }

    public void testTranslationWithXYZf() throws Exception {
        float[] t = TransformFactory.translation(new XYZf(5.0f, 6.0f, 7.0f));
        XYZf p = new XYZf(2.0f, 3.0f, 4.0f);
        XYZf q = TransformApply.point(t, p);
        assertEquals("7.00000 9.00000 11.00000", q.formatRounded());
    }

    public void testYAxisRotation() throws Exception {
        float[] t = TransformFactory.yAxisRotation(90f);
        XYZf p = new XYZf(100.0f, 0, 0);
        XYZf q = TransformApply.point(t, p);
        // pity about the minus zero :-(
        assertEquals("-0.00000 0.00000 -100.00000", q.formatRounded());
    }

    public void testZAxisRotation() throws Exception {
        float[] t = TransformFactory.zAxisRotation(90f);
        XYZf p = new XYZf(100.0f, 0, 0);
        XYZf q = TransformApply.point(t, p);
        // pity about the minus zero :-(
        assertEquals("-0.00000 100.00000 0.00000", q.formatRounded());
    }

    public void testDirectionTransformFromVertexTransform() throws Exception {
        // Derive a direction from the vector between two points in space.
        // Transform the points into a different space.
        // Derive the direction in the new space.
        // Make sure the transformation offered to transform directions thus provides
        // the same answer.
        XYZf a = new XYZf(0, 0, 0);
        XYZf b = new XYZf(100, 0, 0);
        XYZf direction = b.minus(a);
        float[] vertexTransform = TransformFactory.yAxisRotation(90);
        XYZf aDash = TransformApply.point(vertexTransform, a);
        XYZf bDash = TransformApply.point(vertexTransform, b);
        XYZf directionDash = bDash.minus(aDash);

        float[] directionTransform =
                TransformFactory.directionTransformFromVertexTransform(vertexTransform);
        XYZf transformedDirection = TransformApply.direction(directionTransform, direction);
        assertEquals(directionDash.formatRounded(), transformedDirection.formatRounded());
    }

    // This is  just a regression test that uses a set of parameters that have been shown to work
    // visually in the real app.
    public void testPerspective() throws Exception {
        float fov = 90;
        float aspect = 0.6f;
        float near = 120;
        float far = 280;
        float[] perspective = TransformFactory.perspective(fov, aspect, near, far);
        assertEquals(
                "1.67,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0.00,0.00,-2.50,-1.00,0.00,0.00,-420.00,0.00",
                MatrixFormatter.fmtMatrix4x4(perspective));
    }
}
