package com.example.android.opengl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by phoward on 13/11/2015.
 */
public class SceneModel implements ISceneModel {

    public Collection<TriangleWorldModel> getTriangles() {
        Collection<TriangleWorldModel> triangles = new ArrayList<TriangleWorldModel>();

        XYZf topLeft = new XYZf(-0.5f,  0.5f, 0.0f);
        XYZf bottomLeft = new XYZf(-0.5f, -0.5f, 0.0f);
        XYZf bottomRight = new XYZf(0.5f, -0.5f, 0.0f);
        XYZf topRight = new XYZf(0.5f,  0.5f, 0.0f);

        triangles.add(new TriangleWorldModel(topLeft, bottomLeft, bottomRight));
        triangles.add(new TriangleWorldModel(topLeft, bottomRight, topRight));

        return triangles;
    }
}
