package com.example.android.opengl.producerconsumer;

import java.util.concurrent.ExecutorService;

/**
 * A thing that will take responsibility for continuously doing something (producing), but which
 * you can throttle, pause and resume safely. (For example polling a URL). You must invoke its
 * ProduceForever() method in a dedicated thread. You can pause it safely (and put the thread to
 * sleep) by calling Pause() from another thread. And of course then wake it up again with Resume().
 * The client specifies the throttling strategy and the execution context (for example a
 * ThreadPoolExecutor) by injecting them at construction time.
 */
public class ThrottledProducer {

    private Runnable thingThatCanProduceOneItem;
    private Object SyncLock;
    private Boolean IsPaused;
    private ExecutorService executorService;
    private Throttler throttler;

    /**
     * Constructor
     * @param thingThatCanProduceOneItem - This will be called in a throttled loop.
     * @param executorService - The execution environment to which you want the production
     *                        operation to be delegated.
     * @param throttler - The thing you want to govern the production rate.
     */
    public ThrottledProducer(
            Runnable thingThatCanProduceOneItem,
            ExecutorService executorService,
            Throttler throttler) {
        this.thingThatCanProduceOneItem = thingThatCanProduceOneItem;
        this.SyncLock = new Object();
        this.IsPaused = true;
        this.executorService = executorService;
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
        this.executorService.submit(this.thingThatCanProduceOneItem);
    }
}
