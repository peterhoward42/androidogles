package com.example.android.opengl.tests;

import android.test.InstrumentationTestCase;

import com.example.android.opengl.producerconsumer.NumberOfResourcesInUseGetter;
import com.example.android.opengl.producerconsumer.Throttler;
import com.example.android.opengl.tests.mockresourcegetters.AlwaysZero;
import com.example.android.opengl.tests.mockresourcegetters.IncrementsEachTimeYouAsk;
import com.example.android.opengl.tests.mockresourcegetters.JumpsFromZeroToFiveAfter2Seconds;

/**
 * Created by phoward on 12/01/2016.
 */
public class ThrottlerTest extends InstrumentationTestCase {

    private final long quarterSecond = 250;
    private final long halfSecond = 500;
    private final long twoSeconds = 2000;
    private final long threeSeconds = 3000;

    public void testTimeAloneThrottleWorks() throws Exception {
        // We make a throttler which has just the single constraint, of half a second
        // between productions, and apply it to a producer that goes very much faster. Then
        // ensure that the number of productions in a given time period is what we expect
        // from the throttling.
        NumberOfResourcesInUseGetter NilResourceDemand = new AlwaysZero();
        final int UnusedResourceCap = 1; // arbitrary - will not make any difference in this case
        Throttler throttler = new Throttler(NilResourceDemand, UnusedResourceCap, halfSecond);
        // Run our producer for 3 seconds
        long timeNow = System.currentTimeMillis();
        long timeToStop = timeNow + threeSeconds;
        int numberProduced = 0;
        while (System.currentTimeMillis() < timeToStop) {
            if (throttler.DecideIfMustThrottle() == Throttler.ThrottleOrSend.Send)
                numberProduced++;
        }
        assertTrue(numberProduced >= 5);
        assertTrue(numberProduced <= 7);
    }

    public void testResourceAloneThrottleWorks() throws Exception {
        // We make a throttler which has just the single constraint, of not being allowed
        // to commit more than 5 resources at any one time, and arrange for each production
        // to require one new resource which is never recovered. Then we ensure our producer
        // despite running for a little while, never produces more than 5 things.
        NumberOfResourcesInUseGetter escalatingResourceCounter = new
                IncrementsEachTimeYouAsk();
        final int ResourceCap = 5;
        final long ZeroMinimumInterval = 0;
        Throttler throttler = new Throttler(
                escalatingResourceCounter, ResourceCap, ZeroMinimumInterval);
        // Run our producer for 3 seconds
        long timeNow = System.currentTimeMillis();
        long timeToStop = timeNow + threeSeconds;
        int numberProduced = 0;
        while (System.currentTimeMillis() < timeToStop) {
            if (throttler.DecideIfMustThrottle() == Throttler.ThrottleOrSend.Send)
                numberProduced++;
        }
        assertEquals(5, numberProduced);
    }

    public void testBothConstraintsCanWorkTogether() throws Exception {
        Throttler throttler = new Throttler(
                new JumpsFromZeroToFiveAfter2Seconds(), 5, quarterSecond);
        // Run our producer for 3 seconds
        long timeNow = System.currentTimeMillis();
        long timeToStop = timeNow + threeSeconds;
        int numberProduced = 0;
        while (System.currentTimeMillis() < timeToStop) {
            if (throttler.DecideIfMustThrottle() == Throttler.ThrottleOrSend.Send)
                numberProduced++;
        }
        assertEquals(8, numberProduced);
    }
}
