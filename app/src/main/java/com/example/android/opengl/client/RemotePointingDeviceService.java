package com.example.android.opengl.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemotePointingDeviceService {

    private Object SyncLock;
    private Boolean IsPaused;
    private ExecutorService ExecutorService;

    private final int MaxThreads = 10;

    public RemotePointingDeviceService() {
        this.SyncLock = new Object();
        this.IsPaused = true;
        this.ExecutorService = Executors.newCachedThreadPool();
    }

    public void FetchForever() {
        for (; ; ) {
            synchronized (SyncLock) {
                while (IsPaused == true) {
                    try {
                        SyncLock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(
                                "RemotePointingDeviceService thread was in wait state, but " +
                                        "got interrupted");
                    }
                }
            }
            // do real fetch here
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
}
