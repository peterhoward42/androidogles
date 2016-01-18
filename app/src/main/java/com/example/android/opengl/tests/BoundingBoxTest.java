package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.geom.BoundingBox;
import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.MeshFactorySimpleCubes;
import com.example.android.opengl.geom.MeshTransformer;
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

    private Mesh makeTestMesh() {
        Mesh meshToReturn = new MeshFactorySimpleCubes(100).makeMesh();
        meshToReturn.addAllTriangles(
                MeshTransformer.transformedCopy(
                        TransformFactory.translation(100f, 0, 0),
                        new MeshFactorySimpleCubes(100).makeMesh()));
        return meshToReturn;
    }
}
