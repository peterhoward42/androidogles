package com.example.android.opengl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by phoward on 13/11/2015.
 */
public class SceneModel implements ISceneModel {

    private final XYZf mCentrePoint = new XYZf(0, 0, 0);

    public Collection<TriangleWorldModel> getTriangles() {
        Collection<TriangleWorldModel> triangles = new ArrayList<TriangleWorldModel>();

        float top = 500f;
        float bottom = -500f;
        float left = -500;
        float right = 500f;
        float origin = 0f;
        float topPlus = top + 100;
        float rightPlus = right + 100;

        XYZf leftTop = new XYZf(left, top, origin);
        XYZf leftBottom = new XYZf(left, bottom, origin);
        XYZf rightTop = new XYZf(right, top, origin);
        XYZf rightBottom = new XYZf(right, bottom, origin);
        XYZf aboveRightTop = new XYZf(right, topPlus, origin);
        XYZf rightOfTopRight = new XYZf(rightPlus, top, origin);

        // The two that fill in the Z-plane square
        triangles.add(new TriangleWorldModel(leftTop, leftBottom, rightBottom));
        triangles.add(new TriangleWorldModel(leftTop, rightBottom, rightTop));

        // Single triangle like ear on right top corner
        triangles.add(new TriangleWorldModel(aboveRightTop, rightTop, rightOfTopRight));

        // Single triangle poking rearward from right edge
        triangles.add(new TriangleWorldModel(rightTop, rightBottom, new XYZf(500, 500, -1500)));

        return triangles;
    }

    public XYZf CentrePoint() {
        return mCentrePoint;
    }

    public float SphereRadius() {
        return 500;
    }
}
