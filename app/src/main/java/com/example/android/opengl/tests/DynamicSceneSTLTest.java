package com.example.android.opengl.tests;

import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;

import com.example.android.opengl.application.OpenGLES20Activity;
import com.example.android.opengl.mesh.BoundingBox;
import com.example.android.opengl.primitives.XYZf;
import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.vr_content.DynamicSceneSTL;

/**
 * Created by phoward on 18/01/2016.
 */
public class DynamicSceneSTLTest extends ActivityInstrumentationTestCase2<OpenGLES20Activity> {

    public DynamicSceneSTLTest() {
        super(OpenGLES20Activity.class);
    }

    public void testGetBoundingBox() throws Exception {
        AssetManager assetManager = getActivity().getAssets();
        DynamicSceneSTL dynamicSceneSTL = DynamicSceneSTL.buildFromSTLFile(
                assetManager, "gutter.txt");
        BoundingBox boundingBox = dynamicSceneSTL.getBoundingBox();
        assertEquals("25.00000 25.00000 0.00000", boundingBox.getMinima().formatRounded());
        assertEquals("75.00000 75.00000 50.00000", boundingBox.getMaxima().formatRounded());
    }

    public void testGetOffsetFromOriginOfBoundingBoxCentre() throws Exception {
        AssetManager assetManager = getActivity().getAssets();
        DynamicSceneSTL dynamicSceneSTL = DynamicSceneSTL.buildFromSTLFile(
                assetManager, "gutter.txt");
        XYZf offset = dynamicSceneSTL.getBoundingBoxCentre();
        assertEquals("50.00000 50.00000 25.00000", offset.formatRounded());
    }

    public void testGetEffectiveSphere() throws Exception {
        AssetManager assetManager = getActivity().getAssets();
        DynamicSceneSTL dynamicSceneSTL = DynamicSceneSTL.buildFromSTLFile(
                assetManager, "gutter.txt");
        assertEquals("35.36", String.format("%.2f",
                dynamicSceneSTL.getCurrentEffectiveSphere().getRadius()));
    }

    public void testGetCurrentObjectToWorldTransform() {
        AssetManager assetManager = getActivity().getAssets();
        DynamicSceneSTL dynamicSceneSTL = DynamicSceneSTL.buildFromSTLFile(
                assetManager, "gutter.txt");
        float[] transform = dynamicSceneSTL.getCurrentObjectToWorldTransform("mainSilo");
        XYZf modelCentre = dynamicSceneSTL.getBoundingBoxCentre();
        XYZf worldCentre = TransformApply.point(transform, modelCentre);
        assertEquals("0.00000 0.00000 0.00000", worldCentre.formatRounded());

        XYZf rearOfModel = new XYZf(75.0f, 75.0f, -0.0f);
        XYZf worldRearPoint = TransformApply.point(transform, rearOfModel);
        assertEquals("25.00000 25.00000 -25.00000", worldRearPoint.formatRounded());
    }
}
