package com.example.android.opengl.geom;

import android.content.res.AssetManager;

import com.example.android.opengl.math.TransformFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Capable of providing a @link com.example.android.opengl.geom.Mesh by parsing the contents
 * of an STL file.
 */
public class MeshFactoryFromSTLAscii {

    private String mFileContents;
    private Mesh mMesh;
    private List<XYZf> mThreeVertices; // we accumulate them until we have three, then start over
    private XYZf mCurrentNormal; // most recently seen facet normal line

    public MeshFactoryFromSTLAscii(final String fileContents) {
        mFileContents = fileContents;
        mMesh = new Mesh();
        mThreeVertices = new ArrayList<XYZf>();
        mCurrentNormal = null;
    }

    public Mesh makeMesh() {
        // We ignore everything except the vertex lines and the facet-normal lines.
        // We harvest a triangle, every time we accumulate three vertices. The triangle vertices
        // are re-ordered if the facet normal implied by a CCW winding order contradicts the
        // facet normal given in the file.
        String[] lines = mFileContents.split("[\\r\\n]+");
        for (String line : lines) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            processNonEmptyLine(line);
        }
        return mMesh;
    }

    private void processNonEmptyLine(final String line) {
        if (line.startsWith("vertex")) {
            processVertexLine(line);
        }
        if (line.startsWith("facet normal")) {
            processFacetNormalLine(line);
        }
    }

    private void processVertexLine(final String line) {
        // vertex   +2.5120381E+01 +4.7549571E+01 +5.0000000E+01
        final String[] frags = line.split("[\\s]+");
        if (frags.length != 4) {
            throw new RuntimeException(
                    String.format("Cannot read 4 fragments from this line: <%s>", line));
        }
        XYZf vertex = new XYZf(
                Float.parseFloat(frags[1]),
                Float.parseFloat(frags[2]),
                Float.parseFloat(frags[3]));
        mThreeVertices.add(vertex);
        if (mThreeVertices.size() == 3) {
            // Note construction of the triangle, also empties mThreeVertices.
            Triangle triangle = new Triangle(
                    mThreeVertices.remove(0),
                    mThreeVertices.remove(0),
                    mThreeVertices.remove(0));
            triangle = reconcileTriangleToCurrentFacetNormal(triangle);
            mMesh.addTriangle(triangle);
        }
    }

    private void processFacetNormalLine(final String line) {
        // facet normal +9.8917650E-01 +1.4673047E-01 +0.0000000E+00
        final String[] frags = line.split("[\\s]+");
        if (frags.length != 5) {
            throw new RuntimeException(
                    String.format("Cannot read 5 fragments from this line: <%s>", line));
        }
        mCurrentNormal = new XYZf(
                Float.parseFloat(frags[2]),
                Float.parseFloat(frags[3]),
                Float.parseFloat(frags[4])).normalised();
    }

    private Triangle reconcileTriangleToCurrentFacetNormal(Triangle triangle) {
        if (calculatedNormalIsOppositeToCurrentFacetNormal(triangle)) {
            return TriangleManipulator.toggleWindingOrder(triangle);
        }
        return triangle;
    }

    private boolean calculatedNormalIsOppositeToCurrentFacetNormal(final Triangle triangle) {
        return (triangle.getNormal().dotProduct(mCurrentNormal) < 0);
    }
}
