package com.example.android.opengl.math;

import android.os.SystemClock;

/**
 * Created by phoward on 01/12/2015.
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
        float freq = 1 / mPeriodSeconds;
        return mAmplitude * (float)Math.sin(timeNow / mPeriodSeconds);
    }
}
