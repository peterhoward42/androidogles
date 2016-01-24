package com.example.android.opengl.tests;

import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;

import com.example.android.opengl.application.OpenGLES20Activity;
import com.example.android.opengl.mesh.Mesh;
import com.example.android.opengl.mesh.MeshFactoryFromSTLBinary;

import java.io.InputStream;

/**
 * Created by phoward on 18/01/2016.
 */
public class MeshFactoryFromSTLBinaryTest extends ActivityInstrumentationTestCase2<OpenGLES20Activity> {

    private final String BINARY_STL_FILENAME = "testcube_10mm.stl";

    public MeshFactoryFromSTLBinaryTest() {
        super(OpenGLES20Activity.class);
    }

    public void testMakeMesh() throws Exception {
        AssetManager assetManager = getActivity().getAssets();
        InputStream is = assetManager.open(BINARY_STL_FILENAME);
        MeshFactoryFromSTLBinary meshFactory = new MeshFactoryFromSTLBinary(is);
        Mesh mesh = meshFactory.makeMesh();
        assertNotNull(mesh);
        assertEquals(12, mesh.getNumberOfTriangles());
        assertEquals("10.00", String.format("%.2f", mesh.getBoundingBox().getLargestDimension()));
    }
}
