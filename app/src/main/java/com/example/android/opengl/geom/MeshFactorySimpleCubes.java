package com.example.android.opengl.geom;

import com.example.android.opengl.math.TransformFactory;

/**
 * Capable of providing a @link com.example.android.opengl.geom.Mesh that wraps the surfaces of
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
                TransformFactory.yAxisRotation(0), refMesh));
        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.yAxisRotation(90), refMesh));
        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.yAxisRotation(180), refMesh));
        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.yAxisRotation(270), refMesh));

        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.zAxisRotation(90), refMesh));
        meshToReturn.appendAllTrianglesFromMesh(MeshTransformer.transformedCopy(
                TransformFactory.zAxisRotation(270), refMesh));

        return meshToReturn;
    }

    private Mesh makeReferenceMesh() {
        // We use the right facing (+X facing) face as our reference model.
        Mesh twoTriangles = new Mesh();
        twoTriangles.addTriangle(new Triangle(
                new XYZf(+mHalfWidth, -mHalfWidth, -mHalfWidth),
                new XYZf(+mHalfWidth, +mHalfWidth, +mHalfWidth),
                new XYZf(+mHalfWidth, -mHalfWidth, +mHalfWidth)));

        twoTriangles.addTriangle(new Triangle(
                new XYZf(+mHalfWidth, +mHalfWidth, +mHalfWidth),
                new XYZf(+mHalfWidth, -mHalfWidth, -mHalfWidth),
                new XYZf(+mHalfWidth, +mHalfWidth, -mHalfWidth)));

        return twoTriangles;
    }
}
