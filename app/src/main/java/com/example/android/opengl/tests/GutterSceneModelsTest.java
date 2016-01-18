package com.example.android.opengl.tests;

import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.example.android.opengl.application.OpenGLES20Activity;
import com.example.android.opengl.geom.BoundingBox;
import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.vr_content.GutterSceneModels;

import java.util.List;
import java.util.Set;

/**
 * Created by phoward on 18/01/2016.
 */
public class GutterSceneModelsTest extends ActivityInstrumentationTestCase2<OpenGLES20Activity> {

    public GutterSceneModelsTest() {
        super(OpenGLES20Activity.class);
    }

    public void testFactory() throws Exception {
        AssetManager assetManager = getActivity().getAssets();
        GutterSceneModels gutterSceneModels = GutterSceneModels.buildFromAssetFiles(assetManager);
        Mesh mesh = gutterSceneModels.getSilo("mainSilo");
        assertEquals(192, mesh.getTriangles().size());
    }

    public void testGetBoundingBox() throws Exception {
        AssetManager assetManager = getActivity().getAssets();
        GutterSceneModels gutterSceneModels = GutterSceneModels.buildFromAssetFiles(assetManager);
        BoundingBox boundingBox = gutterSceneModels.getBoundingBox();
        assertEquals("25.00000 25.00000 0.00000", boundingBox.getMinima().formatRounded());
        assertEquals("75.00000 75.00000 50.00000", boundingBox.getMaxima().formatRounded());
    }

    public void testGetEffectiveRadius() throws Exception {
        AssetManager assetManager = getActivity().getAssets();
        GutterSceneModels gutterSceneModels = GutterSceneModels.buildFromAssetFiles(assetManager);
        float radius = gutterSceneModels.getEffectiveRadius();
        assertEquals(25.0f, radius);
    }

    public void testGetOffsetFromOriginOfBoundingBoxCentre() throws Exception {
        AssetManager assetManager = getActivity().getAssets();
        GutterSceneModels gutterSceneModels = GutterSceneModels.buildFromAssetFiles(assetManager);
        XYZf offset = gutterSceneModels.getOffsetFromOriginOfBoundingBoxCentre();
        assertEquals("50.00000 50.00000 25.00000", offset.formatRounded());
    }
}
