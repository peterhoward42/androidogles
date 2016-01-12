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

    public final XYZf firstVertex() { return mVertices[0]; }
    public final XYZf secondVertex() { return mVertices[1]; }
    public final XYZf thirdVertex() { return mVertices[2]; }

    public XYZf getNormal() { return mNormal; }

    public String formatRounded() {
        return String.format("%s, %s, %s, %s",
                mVertices[0].formatRounded(),
                mVertices[1].formatRounded(),
                mVertices[2].formatRounded(),
                mNormal.formatRounded()
                );
    }

    private XYZf calculateNormal() {
        XYZf edgeOne = mVertices[0].minus(mVertices[1]);
        XYZf edgeTwo = mVertices[2].minus(mVertices[1]);
        XYZf crossProduct = edgeTwo.crossProduct(edgeOne);
        return crossProduct.normalised();
    }
}
