package com.example.android.opengl.primitives;

/**
 * Trivial bundling of a centre and radius.
 */
public class Sphere {

    private XYZf centre;
    private float radius;

    public Sphere(final XYZf centre, final float radius) {
        this.centre = centre;
        this.radius = radius;
    }

    public XYZf getCentre() {
        return centre;
    }

    public float getRadius() {
        return radius;
    }
}
