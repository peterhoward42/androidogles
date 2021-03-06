package com.example.android.opengl.mesh;

import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.primitives.Triangle;
import com.example.android.opengl.primitives.XYZf;

/**
 * Capable of providing a @link com.example.android.opengl.mesh.Mesh that wraps the surfaces of
 * a given cuboid.
 */
public class MeshFactorySimpleCubes {

    private float mHalfWidth;

    public MeshFactorySimpleCubes(float acrossFlats) {
        mHalfWidth = 0.5f * acrossFlats;
    }

    public Mesh makeMesh() {
        Mesh mesh = new Mesh();

        // Make the two triangles that comprise the right-facing square as a reference model
        Mesh refMesh = makeReferenceMesh();

        // Now make transformed copies for each of the 6 faces

        Mesh meshToReturn = new Mesh();

        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.rotationAboutY(0), refMesh));
        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.rotationAboutY(90), refMesh));
        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.rotationAboutY(180), refMesh));
        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.rotationAboutY(270), refMesh));

        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.rotationAboutZ(90), refMesh));
        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.rotationAboutZ(270), refMesh));

        return meshToReturn;
    }

    private Mesh makeReferenceMesh() {
        // We use the right facing (+X facing) face as our reference model.
        Mesh twoTriangles = new Mesh();
        twoTriangles.addPrimitiveTriangle(new Triangle(
                new XYZf(+mHalfWidth, -mHalfWidth, -mHalfWidth),
                new XYZf(+mHalfWidth, +mHalfWidth, +mHalfWidth),
                new XYZf(+mHalfWidth, -mHalfWidth, +mHalfWidth)));

        twoTriangles.addPrimitiveTriangle(new Triangle(
                new XYZf(+mHalfWidth, +mHalfWidth, +mHalfWidth),
                new XYZf(+mHalfWidth, -mHalfWidth, -mHalfWidth),
                new XYZf(+mHalfWidth, +mHalfWidth, -mHalfWidth)));

        return twoTriangles;
    }
}
