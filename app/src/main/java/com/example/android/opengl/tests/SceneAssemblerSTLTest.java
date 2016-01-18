package com.example.android.opengl.tests;

import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;

import com.example.android.opengl.application.OpenGLES20Activity;
import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.vr_content.ModelCollectionSTL;
import com.example.android.opengl.vr_content.SceneAssemblerSTL;

/**
 * Created by phoward on 18/01/2016.
 */
public class SceneAssemblerSTLTest extends ActivityInstrumentationTestCase2<OpenGLES20Activity> {

    public SceneAssemblerSTLTest() {
        super(OpenGLES20Activity.class);
    }

    public void testGetEffectiveRadius() throws Exception {
        AssetManager assetManager = getActivity().getAssets();
        ModelCollectionSTL modelCollectionSTL = ModelCollectionSTL.buildFromAssetFiles(
                assetManager, "gutter.txt");
        SceneAssemblerSTL assembler = new SceneAssemblerSTL(modelCollectionSTL);
        assertEquals("35.36", String.format("%.2f", assembler.getEffectiveRadius()));
    }

    public void testGetCurrentObjectToWorldTransform() {
        AssetManager assetManager = getActivity().getAssets();
        ModelCollectionSTL modelCollectionSTL = ModelCollectionSTL.buildFromAssetFiles(
                assetManager, "gutter.txt");
        SceneAssemblerSTL assembler = new SceneAssemblerSTL(modelCollectionSTL);
        float[] transform = assembler.getCurrentObjectToWorldTransform("mainSilo");
        XYZf modelCentre = modelCollectionSTL.getBoundingBoxCentre();
        XYZf worldCentre = TransformApply.point(transform, modelCentre);
        assertEquals("0.00000 0.00000 0.00000", worldCentre.formatRounded());

        XYZf rearOfModel = new XYZf(75.0f, 75.0f, -0.0f);
        XYZf worldRearPoint = TransformApply.point(transform, rearOfModel);
        assertEquals("25.00000 25.00000 -25.00000", worldRearPoint.formatRounded());
    }
}
