package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.mesh.BoundingBox;
import com.example.android.opengl.primitives.XYZf;

/**
 * Created by phoward on 12/01/2016.
 */
public class BoundingBoxTest extends InstrumentationTestCase {

    public void testConstructorVariants() throws Exception {
        BoundingBox box = new BoundingBox(new XYZf(0,0,0), new XYZf(1,1,1));
        assertEquals("0.00000 0.00000 0.00000", box.getMinima().formatRounded());
        assertEquals("1.00000 1.00000 1.00000", box.getMaxima().formatRounded());
    }

    public void testAddVertex() throws Exception {
        BoundingBox box = new BoundingBox();
        box.addVertex(new XYZf(1,1,1));
        box.addVertex(new XYZf(2, 2, 2));
        assertEquals("1.00000 1.00000 1.00000", box.getMinima().formatRounded());
        assertEquals("2.00000 2.00000 2.00000", box.getMaxima().formatRounded());
    }

    public void testGetCentre() throws Exception {
        BoundingBox box = new BoundingBox();
        box.addVertex(new XYZf(1, 1, 1));
        box.addVertex(new XYZf(2, 2, 2));
        assertEquals("1.50000 1.50000 1.50000", box.getCentre().formatRounded());
    }

    public void testGetLargest() throws Exception {
        BoundingBox box = new BoundingBox();
        box.addVertex(new XYZf(1,1,1));
        box.addVertex(new XYZf(2, 2, 3));
        assertEquals("2.00", String.format("%.2f", box.getLargestDimension()));
    }

    public void testCombinedWith() throws Exception {
        BoundingBox boxA = new BoundingBox(new XYZf(0,0,0), new XYZf(1,1,1));
        BoundingBox boxB = new BoundingBox(new XYZf(10,10,10), new XYZf(11,11,11));
        BoundingBox combined = boxA.combinedWith(boxB);
        assertEquals("0.00000 0.00000 0.00000", combined.getMinima().formatRounded());
        assertEquals("11.00000 11.00000 11.00000", combined.getMaxima().formatRounded());
    }
}
