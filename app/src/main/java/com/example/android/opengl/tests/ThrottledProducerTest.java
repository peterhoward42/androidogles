package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.producerconsumer.NumberOfResourcesInUseGetter;
import com.example.android.opengl.producerconsumer.SingleShotProducer;
import com.example.android.opengl.producerconsumer.ThrottledProducer;
import com.example.android.opengl.producerconsumer.Throttler;
import com.example.android.opengl.tests.mockresourcegetters.AlwaysZero;

import java.util.concurrent.Executor;

public class ThrottledProducerTest extends InstrumentationTestCase {

    // We launch a ThrottledProducer in a separate thread that produces 10 items per second
    // whilst it is in its resumed state. I.e. not paused. We allow it to run for 3 seconds,
    // but during the middle of these, we pause it. At the end, we make sure the
    // producer ony produced 2 seconds worth.
    public void testPauseAndResumeAndThrottleIntegrationWorks() throws Exception {
        // We use a producer that does nothing but increment an internal counter when it
        // receives a mandate to produce one item.
        final SingleShotterThatDoesNothingButCountCalls singleShotterThatDoesNothingButCountCalls = new
                SingleShotterThatDoesNothingButCountCalls();

        // We use a mock for the thing that can answer questions about the resources already
        // committed - that always says zero.
        NumberOfResourcesInUseGetter  resourceGetter = new AlwaysZero();

        final int unusedResourceCap = 1234;
        final long intervalTime = 100; // milli seconds

        // Hence we can make a throttler that only constrains on a timing basis.
        Throttler throttler = new Throttler(resourceGetter, unusedResourceCap, intervalTime);

        // And we DI all these things into the throttled producer
       final ThrottledProducer producer = new ThrottledProducer(
               singleShotterThatDoesNothingButCountCalls, new TrivialExecutor(), throttler);

        // And set it running (throttled producing) in a separate thread.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                producer.ProduceForever();
            }
        };
        new Thread(runnable).start();

        // Now we enter a loop in this thread so that we can send a pause and resume to the
        // producer at the one and two second marks.
        long timeNow = System.currentTimeMillis();
        long pauseAt = timeNow + 1000;
        long resumeAt = timeNow + 2000;
        long stopAt = timeNow + 3000;

        producer.Resume();
        boolean paused = false;
        for (;;) {
            timeNow = System.currentTimeMillis();
            if (timeNow >= stopAt)
                break;
            if (paused) {
                if (timeNow >= resumeAt) {
                    producer.Resume();
                    paused = false;
                }
            }
            else {
                if (timeNow >= pauseAt) {
                    producer.Pause();
                    paused = true;
                }
            }
        }

        // Finally we ensure the producer produced the correct number of items - corresponding to
        // its activity in the first and third second. (The tolerance on the count is because the
        // throttle time interval interacting with timing causes Fencepost-Problem type ambiguity.
        assertTrue(singleShotterThatDoesNothingButCountCalls.Counter >= 19);
        assertTrue(singleShotterThatDoesNothingButCountCalls.Counter <=21);

        // The thread variable is about to drop out of scope - but will its GC kill the thread?
        // Not sure. So let's be well mannered anyhow.
        producer.Pause();
    }

    private class SingleShotterThatDoesNothingButCountCalls implements SingleShotProducer {

        public int Counter;

        public SingleShotterThatDoesNothingButCountCalls() {
            this.Counter = 0;
        }

        @Override
        public void Produce() {
            Counter += 1;
        }
    }

    private class TrivialExecutor implements Executor {

        @Override
        public void execute(Runnable r) {
            r.run();
        }
    }
}
