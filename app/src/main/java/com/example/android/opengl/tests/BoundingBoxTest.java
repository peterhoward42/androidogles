package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.geom.BoundingBox;
import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.MeshFactorySimpleCubes;
import com.example.android.opengl.geom.MeshTransformer;
import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.math.MatrixCombiner;
import com.example.android.opengl.math.MatrixFormatter;
import com.example.android.opengl.math.TransformFactory;

/**
 * Created by phoward on 12/01/2016.
 */
public class BoundingBoxTest extends InstrumentationTestCase {

    public void testMinima() throws Exception {
        Mesh mesh = makeTestMesh();
        BoundingBox boundingBox = new BoundingBox(mesh);
        assertEquals("-50.00000 -50.00000 -50.00000", boundingBox.getMinima().formatRounded());
    }

    public void testMaxima() throws Exception {
        Mesh mesh = makeTestMesh();
        BoundingBox boundingBox = new BoundingBox(mesh);
        assertEquals("150.00000 50.00000 50.00000", boundingBox.getMaxima().formatRounded());
    }

    public void testGetCentre() throws Exception {
        Mesh mesh = makeTestMesh();
        BoundingBox boundingBox = new BoundingBox(mesh);
        assertEquals("50.00000 0.00000 0.00000", boundingBox.getCentre().formatRounded());
    }

    public void testGetLargest() throws Exception {
        Mesh mesh = makeTestMesh();
        BoundingBox boundingBox = new BoundingBox(mesh);
        assertEquals("200.00", String.format("%.2f", boundingBox.getLargestDimension()));
    }

    public void testMakeFromGivenMinimaAndMaxima() {
        XYZf minima = new XYZf(-5, +6, +7);
        XYZf maxima = new XYZf(11, 33, 99);
        BoundingBox box = BoundingBox.makeFromGivenMinimaAndMaxima(minima, maxima);
        assertEquals("-5.00000 6.00000 7.00000", box.getMinima().formatRounded());
        assertEquals("11.00000 33.00000 99.00000", box.getMaxima().formatRounded());
        assertEquals("3.00000 19.50000 53.00000", box.getCentre().formatRounded());
    }

    public void testCombinedWith() {
        BoundingBox boxA = BoundingBox.makeFromGivenMinimaAndMaxima(
                new XYZf(0,0,0), new XYZf(1,1,1));
        BoundingBox boxB = BoundingBox.makeFromGivenMinimaAndMaxima(
                new XYZf(3,3,3), new XYZf(4,4,4));
        BoundingBox combined = boxA.combinedWith(boxB);
        assertEquals("0.00000 0.00000 0.00000", combined.getMinima().formatRounded());
        assertEquals("4.00000 4.00000 4.00000", combined.getMaxima().formatRounded());
        assertEquals("2.00000 2.00000 2.00000", combined.getCentre().formatRounded());
    }

    private Mesh makeTestMesh() {
        Mesh meshToReturn = new MeshFactorySimpleCubes(100).makeMesh();
        meshToReturn.addAllTriangles(
                MeshTransformer.transformedCopy(
                        TransformFactory.translation(100f, 0, 0),
                        new MeshFactorySimpleCubes(100).makeMesh()));
        return meshToReturn;
    }
}
