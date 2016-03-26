package com.example.android.opengl.producerconsumer;

/**
 * A thing that can help any kind of producer or sender to keep within some given maximum
 * production rate criteria. (For example a polling http request sender). You specify a minimum
 * time interval between successive outputs, and a maximum resource deployment (like number of
 * threads). Then, every time you want to send something, you ask for a throttling judgement  by
 * calling the DecideIfMustThrottle() method.
 */
public class Throttler {
    public enum ThrottleOrSend {Throttle, Send};

    private long MinimumIntervalBetweenTransmissions;
    private NumberOfResourcesInUseGetter numberOfResourcesInUseGetter;
    private int ResourcesCap;
    private long MostRecentSend;

    /**
     * Constructor.
     *
     * @param numberOfResourcesInUseGetter        - a dependency-injected, abstract thing, that the throttler
     *                                            will ask what level of resources are currently deployed at
     *                                            the time of a throttling decision. (how may threads maybe)
     * @param resourcesCap                        - the maximum level of resource that must not be exceeded (5 threads
     *                                            maybe)
     * @param minimumIntervalBetweenTransmissions - The throttler will not advise a "send" when
     *                                            less than this time has elapsed since it last
     *                                            advised a "send". (milli seconds)
     */
    public Throttler(
            NumberOfResourcesInUseGetter numberOfResourcesInUseGetter,
            int resourcesCap,
            long minimumIntervalBetweenTransmissions) {
        this.numberOfResourcesInUseGetter = numberOfResourcesInUseGetter;
        ResourcesCap = resourcesCap;
        MinimumIntervalBetweenTransmissions = minimumIntervalBetweenTransmissions;
        MostRecentSend = -1;
    }

    public ThrottleOrSend DecideIfMustThrottle() {
        long timeNow = System.currentTimeMillis();
        if (TooSoon(timeNow))
            return ThrottleOrSend.Throttle;
        if (NoMoreResourcesAvailable())
            return ThrottleOrSend.Throttle;
        MostRecentSend = timeNow;
        return ThrottleOrSend.Send;
    }

    private boolean TooSoon(final long timeNow) {
        if (MostRecentSend == -1)
            return false;
        return (timeNow - MostRecentSend) < MinimumIntervalBetweenTransmissions;
    }

    private boolean NoMoreResourcesAvailable() {
        return numberOfResourcesInUseGetter.Get() >= ResourcesCap;
    }
}