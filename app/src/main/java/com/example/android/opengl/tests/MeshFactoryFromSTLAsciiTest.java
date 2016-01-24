package com.example.android.opengl.tests;
import android.test.InstrumentationTestCase;

import com.example.android.opengl.mesh.Mesh;
import com.example.android.opengl.mesh.MeshFactoryFromSTLAscii;

/**
 * Created by phoward on 12/01/2016.
 */
public class MeshFactoryFromSTLAsciiTest extends InstrumentationTestCase {

    public void testSimple() throws Exception {

        String fileContent = makeFileContent();
        MeshFactoryFromSTLAscii meshFactory = new MeshFactoryFromSTLAscii(fileContent);
        Mesh mesh = meshFactory.makeMesh();
        assertNotNull(mesh);
        assertEquals(2, mesh.getNumberOfTriangles());
    }

    private final String makeFileContent() {
        StringBuilder sb = new StringBuilder();

        sb.append("solid\n");
        sb.append("  facet normal +0.0000000E+00 -1.0000000E+00 +0.0000000E+00\n");
        sb.append("    outer loop      \n");
        sb.append("      vertex   +1.0000000E+02 +0.0000000E+00 +0.0000000E+00\n");
        sb.append("      vertex   +1.0000000E+02 +0.0000000E+00 +1.0000000E+02\n");
        sb.append("      vertex   +0.0000000E+00 +0.0000000E+00 +1.0000000E+02\n");
        sb.append("    endloop\n");
        sb.append("  endfacet\n");
        sb.append("  facet normal +0.0000000E+00 -1.0000000E+00 +0.0000000E+00\n");
        sb.append("    outer loop      \n");
        sb.append("      vertex   +1.0000000E+02 +0.0000000E+00 +0.0000000E+00\n");
        sb.append("      vertex   +0.0000000E+00 +0.0000000E+00 +1.0000000E+02\n");
        sb.append("      vertex   +0.0000000E+00 +0.0000000E+00 +0.0000000E+00\n");
        sb.append("    endloop\n");
        sb.append("  endfacet\n");
        sb.append("endsolid\n");


        return sb.toString();
    }
}
