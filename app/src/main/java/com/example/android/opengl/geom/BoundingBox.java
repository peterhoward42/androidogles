package com.example.android.opengl.geom;

/**
 * Knows how to evaluate the bounding box of a {@Link Mesh}
 */
public class BoundingBox {

    private XYZf mMinima;
    private XYZf mMaxima;
    private XYZf mCentre;

    private final float HUGE = 1e12f;
    private final float TINY = 1e-12f;

    public BoundingBox(final Mesh mesh) {
        mMinima = new XYZf(HUGE, HUGE, HUGE);
        mMaxima = new XYZf(TINY, TINY, TINY);
        mCentre = new XYZf(0f, 0f, 0f);
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
        mCentre.overwriteX(0.5f * (mMinima.X() + mMaxima.X()));
        mCentre.overwriteY(0.5f * (mMinima.Y() + mMaxima.Y()));
        mCentre.overwriteZ(0.5f * (mMinima.Z() + mMaxima.Z()));
    }

    public final XYZf getCentre() {
        return mCentre;
    }

    public final float getLargestDimension() {
        float largest = TINY;
        largest = Math.max(largest, Math.abs(mMaxima.X() - mMinima.X()));
        largest = Math.max(largest, Math.abs(mMaxima.Y() - mMinima.Y()));
        largest = Math.max(largest, Math.abs(mMaxima.Z() - mMinima.Z()));
        return largest;
    }
}
