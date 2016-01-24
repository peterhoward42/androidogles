package com.example.android.opengl.geom;

/**
 * Encapsulates an orthogonal 3d bounding box. You build it incrementally by giving
 * it new vertices it should encompass. Plus a few convenience methods like asking for its
 * centre, or combining it with another bounding box.
 */
public class BoundingBox {

    private XYZf mMinima;
    private XYZf mMaxima;

    public BoundingBox() {
        XYZf minima = null;
        XYZf maxima = null;
        init(minima, maxima);
    }

    public BoundingBox (XYZf minima, XYZf maxima) {
        init(minima, maxima);
    }

    public void addVertex(final XYZf vertex) {
        // Special case when it is not yet initialised
        if (mMinima == null) {
            init(vertex, vertex);
            return;
        }
        // General case
        mMinima = mMinima.minimizedTo(vertex);
        mMaxima = mMaxima.maximizedTo(vertex);
    }

    public XYZf getMinima() {
        return mMinima;
    }

    public XYZf getMaxima() {
        return mMaxima;
    }

    public final XYZf getCentre() {
        return new XYZf(
                0.5f * (mMinima.X() + mMaxima.X()),
                0.5f * (mMinima.Y() + mMaxima.Y()),
                0.5f * (mMinima.Z() + mMaxima.Z()));
    }

    public final float getLargestDimension() {
        float largest = Float.MIN_VALUE;
        largest = Math.max(largest, Math.abs(mMaxima.X() - mMinima.X()));
        largest = Math.max(largest, Math.abs(mMaxima.Y() - mMinima.Y()));
        largest = Math.max(largest, Math.abs(mMaxima.Z() - mMinima.Z()));
        return largest;
    }

    public BoundingBox combinedWith(final BoundingBox otherBox) {
        return new BoundingBox(
                new XYZf(Math.min(mMinima.X(), otherBox.mMinima.X()),
                        Math.min(mMinima.Y(), otherBox.mMinima.Y()),
                        Math.min(mMinima.Z(), otherBox.mMinima.Z())),
                new XYZf(Math.max(mMaxima.X(), otherBox.mMaxima.X()),
                        Math.max(mMaxima.Y(), otherBox.mMaxima.Y()),
                        Math.max(mMaxima.Z(), otherBox.mMaxima.Z())));
    }

    private void init(XYZf minima, XYZf maxima) {
        mMinima = minima;
        mMaxima = maxima;
    }
}
