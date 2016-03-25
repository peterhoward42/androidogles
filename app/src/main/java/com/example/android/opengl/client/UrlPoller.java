package com.example.android.opengl.client;

import android.content.Context;

import com.example.android.opengl.producerconsumer.ThrottledProducer;
import com.example.android.opengl.producerconsumer.Throttler;

import java.util.concurrent.ExecutorService;

/**
 * A thing that will take responsibility for continuously polling a URL, implemented as a thin
 * wrapper around {@link ThrottledProducer}. See that class for more details.
 */
public class UrlPoller {

    private ThrottledProducer throttledProducer;

    /**
     * Constructor
     * @param url - The url you want to poll.
     * @param context - Needed to access network services.
     * @param executorService - The thing to which you want URL fetch ops to be delegated.
     * @param throttler - The thing you want to govern the URL fetch polling rate.
     */
    public UrlPoller(final String url, Context context, ExecutorService executorService,
                     Throttler throttler) {
        this.throttledProducer = new ThrottledProducer(new UrlFetcher(context, url),
                executorService, throttler);
    }

    public void FetchForever() {
        this.throttledProducer.ProduceForever();
    }

    public void Pause() {
        this.throttledProducer.Pause();
    }

    public void Resume() {
        this.throttledProducer.Resume();
    }
}
