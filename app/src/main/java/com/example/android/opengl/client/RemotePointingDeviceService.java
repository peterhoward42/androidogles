package com.example.android.opengl.client;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Takes responsibility for fetching remote pointing device inputs (mouse movements) from
 * a remote networked source in a fast forever-loop. You should run it in a dedicated thread.
 * It runs forever but you can call its methods to pause and resume from another thread.
 * Under the hood, it emits http requests interleaved and in parallel in worker threads,
 * controlled using a throttling strategy to reconcile a fast sampling rate with the speed the
 * server can reply.
 */
public class RemotePointingDeviceService {

    private Object SyncLock;
    private Boolean IsPaused;
    private ThreadPoolExecutor ThreadPoolExecutor;
    private LinkedBlockingDeque<Runnable> TaskQueue;
    private long TimeOfMostRecentlySent;
    private Context Context;

    // Url throttling parameters.
    private final String RemotePointerUrl = "http://cadboardserver.appspot.com/mouseposnquery";
    private final int MaxWorkerThreads = 5;
    private final long KeepAliveTime = 5; // seconds
    private final long MinimumIntervalBetweenRequests = 500L; // milli seconds

    public RemotePointingDeviceService(Context context) {
        this.SyncLock = new Object();
        this.IsPaused = true;
        this.TaskQueue = new LinkedBlockingDeque<Runnable>();
        this.Context = context;

        // We want this many threads working for us in the steady state, so match up the
        // core size and the max size to be that many.
        int corePoolSize = MaxWorkerThreads;
        int maxPoolSize = MaxWorkerThreads;
        this.ThreadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, maxPoolSize, KeepAliveTime,
                TimeUnit.SECONDS, TaskQueue);
        this.TimeOfMostRecentlySent = -1;
    }

    public void FetchForever() {
        for (; ; ) {
            // Force this thread into a dormant wait-state mid-loop, if someone paused us.
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
            // We've been woken up, i.e. resumed, so do one fetch operation,
            // then iterate the outer loop to continue doing so.

            if (sendingNowIsConsistentWithThrottlingStrategy()) {
                TimeOfMostRecentlySent = System.currentTimeMillis();
                LaunchOneFetchRequest();
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

    private boolean sendingNowIsConsistentWithThrottlingStrategy() {
        if (TooSoonAfterLastOne())
            return false;
        if (TooManyRequestsAreBufferingUp())
            return false;
        return true;
    }

    private void LaunchOneFetchRequest() {
        Log.i("XXX", "launching fetch");
        this.ThreadPoolExecutor.submit(new UrlFetcher(this.Context, RemotePointerUrl));
    }

    private boolean TooSoonAfterLastOne() {
        if (TimeOfMostRecentlySent == -1)
            return false;
        long interval = System.currentTimeMillis() - TimeOfMostRecentlySent;
        if (interval < MinimumIntervalBetweenRequests) {
            Log.i("XXX", String.format("XXX too soon (%d)", interval));
            return true;
        }
        return false;
    }

    private boolean TooManyRequestsAreBufferingUp() {
        int buffered = TaskQueue.size();
        boolean tooMany = buffered >= MaxWorkerThreads;
        if (tooMany)
            Log.i("XXX", String.format("XXX too many (%d)", buffered));
        return tooMany;
    }
}
