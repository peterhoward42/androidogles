package com.example.android.opengl;

import java.util.Collection;

/**
 * Geometry definition of triangle in model space.
 */
public class Triangle {

    private XYZf[]   mXYZ;

    public Triangle(XYZf a, XYZf b, XYZf c) {
        mXYZ = new XYZf[3];
        mXYZ[0] = a;
        mXYZ[1] = b;
        mXYZ[2] = c;
    }

    public final XYZf[] vertices() {
        return mXYZ;
    }
}
