package com.example.android.opengl;

import java.util.Collection;

/**
 * Created by phoward on 13/11/2015.
 */
public class TriangleWorldModel {

    private XYZf[]   mXYZ;

    public TriangleWorldModel(XYZf a, XYZf b, XYZf c) {
        mXYZ = new XYZf[3];
        mXYZ[0] = a;
        mXYZ[1] = b;
        mXYZ[2] = c;
    }

    public final XYZf[] vertices() {
        return mXYZ;
    }
}
