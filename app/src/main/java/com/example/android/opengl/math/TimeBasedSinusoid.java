package com.example.android.opengl.math;

import android.os.SystemClock;

/**
 * Models a sinusoid function with an amplitude and frequency of your choice, and provides a method
 * to evaluate the function at the current system time.
 */
public class TimeBasedSinusoid {

    private float mAmplitude;
    private float mPeriodSeconds;

    public TimeBasedSinusoid(float amplitude, float periodSeconds) {
        mAmplitude = amplitude;
        mPeriodSeconds = periodSeconds;
    }

    public float evaluateAtTimeNow() {
        float timeNow = SystemClock.uptimeMillis() / 1000.0f;
        return mAmplitude * (float)Math.sin(2.0 * Math.PI * timeNow / mPeriodSeconds);
    }
}