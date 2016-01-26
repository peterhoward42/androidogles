package com.example.android.opengl.vr_content;

import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.primitives.XYZf;

/**
 * Created by phoward on 26/01/2016.
 */
public class Viewpoint {
    private XYZf viewersPosition;
    private XYZf lineOfSightVector; // normalised on receipt in constructor

    public Viewpoint(XYZf lineOfSightVector, XYZf viewersPosition) {
        this.lineOfSightVector = lineOfSightVector.normalised();
        this.viewersPosition = viewersPosition;
    }

    public XYZf getViewersPosition() {
        return viewersPosition;
    }

    public XYZf getLineOfSightVector() {
        return lineOfSightVector;
    }

    public XYZf getASamplePointInDirectLineOfSight() {
        float[] translateAlongLineOfSight = TransformFactory.translation(
                lineOfSightVector.X(), lineOfSightVector.Y(), lineOfSightVector.Z());
        XYZf samplePoint = TransformApply.point(translateAlongLineOfSight, viewersPosition);
        return samplePoint;
    }
}
