package com.example.android.opengl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Capable of providing a set of @link com.example.android.opengl.Triangle Triangles
 * that wrap the surfaces of a given cuboid.
 */
public class MesherForCuboid {

    private XYZf mSizes;
    private XYZf mHalved;

    public MesherForCuboid(XYZf sizes) {
        mSizes = sizes;
        mHalved = new XYZf(0.5f * mSizes.X(), 0.5f * mSizes.Y(), 0.5f * mSizes.Z());
    }

    public Collection<Triangle> getTriangles() {
        Collection<Triangle> triangles = new ArrayList<Triangle>();

        // front
        addTrianglesForFace(triangles, new XYZf(0, 0, mHalved.Z()),
                new XYZf(0, mHalved.Y(), 0), new XYZf(mHalved.X(), 0, 0));
        // top
        addTrianglesForFace(triangles, new XYZf(0, mHalved.Y(), 0),
                new XYZf(0, 0, -mHalved.Z()), new XYZf(mHalved.X(), 0, 0));
        // back
        addTrianglesForFace(triangles, new XYZf(0, 0, -mHalved.Z()),
                new XYZf(0, mHalved.Y(), 0), new XYZf(-mHalved.X(), 0, 0));
        // bottom
        addTrianglesForFace(triangles, new XYZf(0, -mHalved.Y(), 0),
                new XYZf(0, 0, mHalved.Z()), new XYZf(-mHalved.X(), 0, 0));
        // left end
        addTrianglesForFace(triangles, new XYZf(mHalved.X(), 0, 0),
                new XYZf(0, mHalved.Y(), 0), new XYZf(0, 0, mHalved.Z()));
        // right end
        addTrianglesForFace(triangles, new XYZf(-mHalved.X(), 0, 0),
                new XYZf(0, mHalved.Y(), 0), new XYZf(0, 0, -mHalved.Z()));
        return triangles;
    }

    private void addTrianglesForFace(Collection<Triangle> container,
            XYZf faceCentre, XYZf firstLeg, XYZf secondLeg) {
        // Comments describe making two triangles to fit a "front" face, facing to the rear,
        // i.e. +Z, where first leg is in Y and second is in X.

        XYZf rightTop = faceCentre.plus(firstLeg).plus(secondLeg);
        XYZf leftTop = faceCentre.plus(firstLeg).minus(secondLeg);
        XYZf rightBottom = faceCentre.minus(firstLeg).plus(secondLeg);
        XYZf leftBottom = faceCentre.minus(firstLeg).minus(secondLeg);

        container.add(new Triangle(rightBottom, leftTop, rightTop));
        container.add(new Triangle(leftTop, rightBottom, leftBottom));
    }
}
