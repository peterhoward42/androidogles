package com.example.android.opengl.tests.producerconsumer;

import com.example.android.opengl.producerconsumer.NumberOfResourcesInUseGetter;


public class JumpsFromZeroToFiveAfter2Seconds implements NumberOfResourcesInUseGetter {

    private int NumberOfResourcesCommitted;
    private long timeToJump;

    public JumpsFromZeroToFiveAfter2Seconds() {
        this.NumberOfResourcesCommitted = 0;
        timeToJump = System.currentTimeMillis() + 2000;
    }

    @Override
    public int Get() {
        return (System.currentTimeMillis() >= timeToJump) ? 5: 0;
    }
}
