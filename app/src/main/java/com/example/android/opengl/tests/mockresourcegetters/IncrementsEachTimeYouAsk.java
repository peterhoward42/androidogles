package com.example.android.opengl.tests.mockresourcegetters;

import com.example.android.opengl.producerconsumer.NumberOfResourcesInUseGetter;

/**
 * Created by phoward on 25/03/2016.
 */
public class IncrementsEachTimeYouAsk implements NumberOfResourcesInUseGetter {

    private int NumberOfResourcesCommitted;

    public IncrementsEachTimeYouAsk() {
        this.NumberOfResourcesCommitted = 0;
    }

    @Override
    public int Get() {
        int response = NumberOfResourcesCommitted;
        NumberOfResourcesCommitted += 1;
        return response;
    }
}