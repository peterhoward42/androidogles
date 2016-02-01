package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.mesh.MeshVertex;
import com.example.android.opengl.primitives.XYZf;

/**
 * Created by phoward on 12/01/2016.
 */
public class MeshVertexTest extends InstrumentationTestCase {

    public void testHashAfterNumericalRounding() throws Exception {
        XYZf position = new XYZf(1,2,3);
        XYZf normal = new XYZf(4,5,6).normalised();
        MeshVertex meshVertex = new MeshVertex(position, normal);
        String hashStringAfterRounding = meshVertex.hashAfterNumericalRounding();
        assertEquals("1.000002.000003.000000.455840.569800.68376", hashStringAfterRounding);
    }
}
