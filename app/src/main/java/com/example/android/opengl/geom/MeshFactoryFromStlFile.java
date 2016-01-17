package com.example.android.opengl.geom;

import android.content.res.AssetManager;

import com.example.android.opengl.math.TransformFactory;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Capable of providing a @link com.example.android.opengl.geom.Mesh by parsing the contents
 * of an STL file.
 */
public class MeshFactoryFromStlFile {

    private String mFileContents;

    public MeshFactoryFromStlFile(final String fileContents) {
        mFileContents = fileContents;
    }

    public Mesh makeMesh() {
        Mesh mesh = new Mesh();
        String[] lines = mFileContents.split("[\\r\\n]+");

        // We ignore everything except the vertex lines and harvest a triangle, every time
        // we accumulate three of them.

        Deque<XYZf> vertices = new ArrayDeque<XYZf>();
        for (String line: lines) {
            line = line.trim();
            if (line.startsWith("vertex")) {
                String[] frags = line.split("[\\s]+");
                if (frags.length != 4) {
                    throw new RuntimeException(
                            String.format("Cannot read 4 fragments from this line: <%s>", line));
                }
                XYZf vertex = new XYZf(
                        Float.parseFloat(frags[1]),
                        Float.parseFloat(frags[2]),
                        Float.parseFloat(frags[3]));
                vertices.push(vertex);
                if (vertices.size() == 3) {
                    try {
                        Triangle triangle = new Triangle(
                                vertices.removeFirst(),
                                vertices.removeFirst(),
                                vertices.removeFirst());
                        mesh.addTriangle(triangle);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(
                                String.format("Cannot parse one of the numbers from this line: <%s>", line));
                    }
                }
            }
        }
        if (mesh.size() == 0) {
            throw new RuntimeException("No triangles found in STL file");
        }
        return mesh;
    }
}
