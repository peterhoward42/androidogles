package com.example.android.opengl.geom;

/**
 * Knows how to evaluate the bounding box of a {@Link Mesh}
 */
public class BoundingBox {

    private final float HUGE = Float.MAX_VALUE;
    private final float TINY = Float.MIN_VALUE;
    private XYZf mMinima;
    private XYZf mMaxima;
    private XYZf mCentre;

    public BoundingBox(final Mesh mesh) {
        mMinima = new XYZf(HUGE, HUGE, HUGE);
        mMaxima = new XYZf(TINY, TINY, TINY);
        mCentre = null;
        for (Triangle triangle: mesh.getTriangles()) {
            for (XYZf vertex: triangle.vertices()) {
                mMinima.overwriteX(Math.min(mMinima.X(), vertex.X()));
                mMinima.overwriteY(Math.min(mMinima.Y(), vertex.Y()));
                mMinima.overwriteZ(Math.min(mMinima.Z(), vertex.Z()));

                mMaxima.overwriteX(Math.max(mMaxima.X(), vertex.X()));
                mMaxima.overwriteY(Math.max(mMaxima.Y(), vertex.Y()));
                mMaxima.overwriteZ(Math.max(mMaxima.Z(), vertex.Z()));
            }
        }
        mCentre = new XYZf(
                0.5f * (mMinima.X() + mMaxima.X()),
                0.5f * (mMinima.Y() + mMaxima.Y()),
                0.5f * (mMinima.Z() + mMaxima.Z()));
    }

    public XYZf getMinima() {
        return mMinima;
    }
    public XYZf getMaxima() {
        return mMaxima;
    }

    public final XYZf getCentre() {
        return mCentre;
    }

    public final float getLargestDimension() {
        float largest = Float.MIN_VALUE;
        largest = Math.max(largest, Math.abs(mMaxima.X() - mMinima.X()));
        largest = Math.max(largest, Math.abs(mMaxima.Y() - mMinima.Y()));
        largest = Math.max(largest, Math.abs(mMaxima.Z() - mMinima.Z()));
        return largest;
    }
}
