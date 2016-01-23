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

    /**
     * Constructor - deducing sizes by interrrogating a given {@link com.example.android.opengl.geom.Mesh}
     *
     * @param mesh The mesh to interrogate.
     */
    public BoundingBox(final Mesh mesh) {
        mMinima = new XYZf(HUGE, HUGE, HUGE);
        mMaxima = new XYZf(TINY, TINY, TINY);
        mCentre = null;
        for (Triangle triangle : mesh.getTriangles()) {
            for (XYZf vertex : triangle.vertices()) {
                mMinima.overwriteX(Math.min(mMinima.X(), vertex.X()));
                mMinima.overwriteY(Math.min(mMinima.Y(), vertex.Y()));
                mMinima.overwriteZ(Math.min(mMinima.Z(), vertex.Z()));

                mMaxima.overwriteX(Math.max(mMaxima.X(), vertex.X()));
                mMaxima.overwriteY(Math.max(mMaxima.Y(), vertex.Y()));
                mMaxima.overwriteZ(Math.max(mMaxima.Z(), vertex.Z()));
            }
        }
        initCentreFromMinimaAndMaxima();
    }

    /**
     * Factory - based on pre-calculated vertices for the minima vertex and the
     * maxima vertex.
     *
     * @return
     */
    public static BoundingBox makeFromGivenMinimaAndMaxima(final XYZf minima, final XYZf maxima) {
        BoundingBox box = new BoundingBox();
        box.mMinima = minima;
        box.mMaxima = maxima;
        box.initCentreFromMinimaAndMaxima();
        return box;
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

    public BoundingBox combinedWith(final BoundingBox otherBox) {
        return makeFromGivenMinimaAndMaxima(
                new XYZf(Math.min(mMinima.X(), otherBox.mMinima.X()),
                        Math.min(mMinima.Y(), otherBox.mMinima.Y()),
                        Math.min(mMinima.Z(), otherBox.mMinima.Z())),
                new XYZf(Math.max(mMaxima.X(), otherBox.mMaxima.X()),
                        Math.max(mMaxima.Y(), otherBox.mMaxima.Y()),
                        Math.max(mMaxima.Z(), otherBox.mMaxima.Z())));
    }

    private BoundingBox() {
        // For use by factory methods only.
    }

    private void initCentreFromMinimaAndMaxima() {
        mCentre = new XYZf(
                0.5f * (mMinima.X() + mMaxima.X()),
                0.5f * (mMinima.Y() + mMaxima.Y()),
                0.5f * (mMinima.Z() + mMaxima.Z()));
    }
}
