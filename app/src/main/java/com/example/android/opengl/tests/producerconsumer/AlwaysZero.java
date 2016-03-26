package com.example.android.opengl.tests.producerconsumer;

import com.example.android.opengl.producerconsumer.NumberOfResourcesInUseGetter;

public class AlwaysZero implements NumberOfResourcesInUseGetter{
    @Override
    public int Get() {
        return 0;
    }
}
