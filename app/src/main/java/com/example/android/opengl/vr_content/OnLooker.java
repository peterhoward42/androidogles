package com.example.android.opengl.vr_content;

/**
 * An interface for a thing that knows the current position a viewer has in a scene, and in which
 * direction they are looking. Note that the direction parameter is a directional vector, and NOT
 * a look-at point.
 */
public interface OnLooker {

    public Viewpoint getCurrentViewpoint();
}