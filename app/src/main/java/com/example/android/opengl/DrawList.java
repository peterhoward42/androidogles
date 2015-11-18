package com.example.android.opengl;

/**
 * A container for the contiguous arrays of floats and shorts that you need in
 * creating the pipeline towards the OpenGL-ES glDrawElements rendering method. We are obliged to
 * pack our data thus, to use later on to populate the Java FloatBuffer and ShortBuffer, which in
 * due course are the prerequisites for passing data down to OpenGL-ES.
 */
public class DrawList {

    // Packed with 3 floats per vertex, 3 vertices per triangle. Then repeat.
    public float[] vertexFloats;

    // The order to draw vertices so as to make the triangles intended.
    public short[] drawListShorts;
}