package com.example.android.opengl.producerconsumer;

import java.util.concurrent.Executor;

/**
 * A thing that will take responsibility for continuously doing something (producing), but which
 * you can throttle, pause and resume safely. (For example polling a URL). You must invoke its
 * ProduceForever() method in a dedicated thread. You can pause it safely (and put the thread to
 * sleep) by calling Pause() from another thread. And of course then wake it up again with Resume().
 * It is not tied to any particular type of producer; the client injects this. The client also
 * injects the throttler they want to be applied. The client can also control how the producer
 * they provide is called - by injecting an execution context - e.g. (but not necessarily) a
 * ThreadPoolExecutor.
 */
public class ThrottledProducer {

    private SingleShotProducer singleShotter;
    private Object SyncLock;
    private Boolean IsPaused;
    private Executor executor;
    private Throttler throttler;

    /**
     * Constructor
     * @param singleShotter - This will be called in a throttled loop.
     * @param executor - The execution environment to which you want the production
     *                        operation to be delegated.
     * @param throttler - The thing you want to govern the production rate.
     */
    public ThrottledProducer(
            SingleShotProducer singleShotter,
            Executor executor,
            Throttler throttler) {
        this.singleShotter = singleShotter;
        this.SyncLock = new Object();
        this.IsPaused = true;
        this.executor = executor;
        this.throttler = throttler;
    }

    public void ProduceForever() {
        for (; ; ) {
            // Force this thread into a dormant wait-state mid-loop, if someone paused us.
            synchronized (SyncLock) {
                while (IsPaused == true) {
                    try {
                        SyncLock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(
                                "Throttled Producer thread was in wait state, but " +
                                        "got interrupted");
                    }
                }
            }
            // We've been woken up, i.e. resumed, so do one produce operation,
            // then iterate the outer loop to continue doing so.

            if (throttler.DecideIfMustThrottle() == Throttler.ThrottleOrSend.Send) {
                ProduceOneItem();
            }
        }
    }

    public void Pause() {
        synchronized (SyncLock) {
            IsPaused = true;
        }
    }

    public void Resume() {
        synchronized (SyncLock) {
            IsPaused = false;
            SyncLock.notifyAll();
        }
    }

    private void ProduceOneItem() {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                singleShotter.Produce();
            }
        });
    }
}
