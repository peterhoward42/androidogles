package com.example.android.opengl.tests;

import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;

import com.example.android.opengl.application.OpenGLES20Activity;
import com.example.android.opengl.mesh.Mesh;
import com.example.android.opengl.mesh.MeshFactoryFromSTLAscii;
import com.example.android.opengl.mesh.MeshVertexSmoother;
import com.example.android.opengl.primitives.XYZf;
import com.example.android.opengl.util.FileOperations;

import java.io.InputStream;

/**
 * Created by phoward on 18/01/2016.
 */
public class MeshSmootherTest extends ActivityInstrumentationTestCase2<OpenGLES20Activity> {

    private final String TEST_SPHERE_FILENAME = "sphere.txt";

    public MeshSmootherTest() {
        super(OpenGLES20Activity.class);
    }

    public void testSmoothsSimpleSphere() throws Exception {
        AssetManager assetManager = getActivity().getAssets();
        InputStream is = assetManager.open(TEST_SPHERE_FILENAME);
        final String fileContents = FileOperations.SlurpInputStreamToString(is);
        MeshFactoryFromSTLAscii meshFactory = new MeshFactoryFromSTLAscii(fileContents);
        Mesh mesh = meshFactory.makeMesh();

        final XYZf sampleNormalBeforeSmoothing = mesh.getTriangles().iterator().next()
                .getVertexNormals()[0];

        MeshVertexSmoother smoother = new MeshVertexSmoother(mesh);
        smoother.doSmoothing();

        final XYZf sampleNormalAfterSmoothing = mesh.getTriangles().iterator().next()
                .getVertexNormals()[0];

        // These normals should differ by a few degrees
        final double angularDifference = Math.toDegrees(
                Math.acos((double) (sampleNormalBeforeSmoothing
                        .dotProduct
                                (sampleNormalAfterSmoothing))));

        assertEquals("6.99535", String.format("%.5f", angularDifference));
    }
}
