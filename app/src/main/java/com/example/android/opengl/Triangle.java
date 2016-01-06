package com.example.android.opengl;

import android.opengl.Matrix;

import java.util.Collection;

/**
 * Geometry definition of triangle in model space. In CCW winding order.
 */
public class Triangle {

    private XYZf[] mVertices; // held in CCW winding order
    private XYZf mNormal; // is normalised

    /**
     * @param a first vertex in CCW winding order
     * @param b second vertex in CCW winding order
     * @param c third vertex in CCW winding order
     */
    public Triangle(XYZf a, XYZf b, XYZf c) {
        mVertices = new XYZf[3];
        mVertices[0] = a;
        mVertices[1] = b;
        mVertices[2] = c;

        mNormal = calculateNormal();
    }

    /**
     * @return vertices in CCW winding order
     */
    public final XYZf[] vertices() {
        return mVertices;
    }

    private XYZf calculateNormal() {
        XYZf edgeOne = mVertices[0].minus(mVertices[1]);
        XYZf edgeTwo = mVertices[2].minus(mVertices[1]);
        XYZf crossProduct = edgeOne.crossProduct(edgeTwo);
        return crossProduct.normalised();
    }
}
