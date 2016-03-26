package com.example.android.opengl.producerconsumer;

/**
 * Consider that the Producer pattern produces a succession of items. This SingleShotInterface then
 * represents a thing that produces just one in the succession.
 */
public interface SingleShotProducer {

    public void Produce();
}
