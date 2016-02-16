package com.example.android.opengl.threadsafe;

import com.example.android.opengl.primitives.XYZf;

/**
 * A trivial wrapper around a {@Link XYZf} that limits access to it to just synchronized get and
 * set methods.
 */
public class ConcurrentXYZf {

    public ConcurrentXYZf(XYZf xyz) {
        this.xyz = xyz;
    }

    private XYZf xyz;

    public synchronized XYZf synchronizedGet() {
        return xyz;
    }

    public synchronized void synchronizedSet(XYZf xyz) {
        this.xyz = xyz;
    }
}
