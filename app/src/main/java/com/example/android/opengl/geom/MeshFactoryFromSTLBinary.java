package com.example.android.opengl.geom;

import com.google.common.io.LittleEndianDataInputStream;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Capable of providing a @link com.example.android.opengl.geom.Mesh by parsing the contents
 * of a binary STL file.
 */
public class MeshFactoryFromSTLBinary {

    private InputStream mInputStream;
    private Mesh mMesh;
    private List<XYZf> mThreeVertices; // we read them in blocks of three

    private final int BYTES_IN_HEADER = 80;

    public MeshFactoryFromSTLBinary(InputStream inputStream) {
        mInputStream = inputStream;
        mMesh = new Mesh();
    }

    public Mesh makeMesh() throws FileNotFoundException, IOException {
        DataInput dataSource = createDataInputStream();
        dataSource.skipBytes(BYTES_IN_HEADER);
        int numberOfTriangles = readNumberOfTriangles(dataSource);
        readAllTriangles(dataSource, numberOfTriangles);
        return mMesh;
    }

    private int readNumberOfTriangles(DataInput dataSource) throws IOException {
        // Spec says 32 bit int, which readInt() corresponds to.
        Integer nTriangles = dataSource.readInt();
        return nTriangles;
    }

    private DataInput createDataInputStream() throws FileNotFoundException {
        // Note the replacement of Java's standard DataInputStream, with this special
        // variant from Google's Guava library.
        DataInput openedStream = new LittleEndianDataInputStream(new BufferedInputStream(mInputStream));
        return openedStream;
    }

    private void readAllTriangles(
            DataInput dataSource, final int numberOfTriangles) throws IOException {
        for (int i = 0; i < numberOfTriangles; i++) {
            XYZf statedNormal = makeXYZfByReadingThreeFloats(dataSource);
            Triangle newTriangle = new Triangle(
                    makeXYZfByReadingThreeFloats(dataSource),
                    makeXYZfByReadingThreeFloats(dataSource),
                    makeXYZfByReadingThreeFloats(dataSource));
            newTriangle = reconcileTriangleToStatedFacetNormal(newTriangle, statedNormal);
            mMesh.addTriangle(newTriangle);
        }
    }

    private XYZf makeXYZfByReadingThreeFloats(DataInput dataSource) throws IOException {
        return new XYZf(
                dataSource.readFloat(),
                dataSource.readFloat(),
                dataSource.readFloat());
    }

    private Triangle reconcileTriangleToStatedFacetNormal(
            Triangle triangle, final XYZf statedNormal) {
        if (calculatedNormalIsOppositeToStatedNormal(triangle, statedNormal)) {
            return TriangleManipulator.toggleWindingOrder(triangle);
        }
        return triangle;
    }

    private boolean calculatedNormalIsOppositeToStatedNormal(
            final Triangle triangle, final XYZf statedNormal) {
        return (triangle.getNormal().dotProduct(statedNormal) < 0);
    }
}
