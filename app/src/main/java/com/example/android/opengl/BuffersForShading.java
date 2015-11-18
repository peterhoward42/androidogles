package com.example.android.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Encapsulates a pair of byte buffers to be consumed by shaders. One packs triangle vertex
 * coordinates as contiguous floats, while the other pack the sequence of indices that when
 * traversed will visit the offset into the array where each vertex starts.
 */
public class BuffersForShading {
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public int numberOfVertices;

    public BuffersForShading(DrawList drawList) {
        numberOfVertices = drawList.drawListShorts.length;
        ByteBuffer vertexBytes = ByteBuffer.allocateDirect(
                drawList.vertexFloats.length * SystemConstants.BYTES_IN_FLOAT);
        vertexBytes.order(ByteOrder.nativeOrder());
        vertexBuffer = vertexBytes.asFloatBuffer();
        vertexBuffer.put(drawList.vertexFloats);
        vertexBuffer.position(0);

        // 2 bytes per short
        ByteBuffer drawOrderBytes = ByteBuffer.allocateDirect(
                drawList.drawListShorts.length * SystemConstants.BYTES_IN_SHORT);
        drawOrderBytes.order(ByteOrder.nativeOrder());
        drawListBuffer = drawOrderBytes.asShortBuffer();
        drawListBuffer.put(drawList.drawListShorts);
        drawListBuffer.position(0);
    }
}
