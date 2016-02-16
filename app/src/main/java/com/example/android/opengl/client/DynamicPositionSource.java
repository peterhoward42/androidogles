package com.example.android.opengl.client;

import com.example.android.opengl.threadsafe.ConcurrentXYZf;

/**
 * A thing that offers to continuously poll the application server, to fetch its
 * latest mandate for a user's virtual world position, and to post this fetched data
 * into the {@link com.example.android.opengl.threadsafe.ConcurrentXYZf} you provide at
 * construction time.
 */
public class DynamicPositionSource {

    private ConcurrentXYZf positionSink;

    public DynamicPositionSource(ConcurrentXYZf positionSink) {
        this.positionSink = positionSink;
    }
}