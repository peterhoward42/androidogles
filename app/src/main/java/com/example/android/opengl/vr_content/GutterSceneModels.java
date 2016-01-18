package com.example.android.opengl.vr_content;

import android.content.res.AssetManager;

import com.example.android.opengl.geom.BoundingBox;
import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.MeshFactoryFromStlFile;
import com.example.android.opengl.geom.MeshFactorySimpleCubes;
import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.util.FileOperations;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of ISceneModels for test and development purposes, with content
 * comprising a single object, that looks like a half-tube. It is read from an STL file.
 */
public class GutterSceneModels implements ISceneModels {

    private Map<String, Mesh> mSilos;
    private final String GUTTER_FILE_NAME = "gutter.stl";
    private BoundingBox mBoundingBox;

    // We provide only a private constructor, to force use of the factory function below.
    private GutterSceneModels() {
        mSilos = new HashMap<String, Mesh>();
        mBoundingBox = null;
    }

    /** Factory method - the only way to make one of these.
     */
    public static GutterSceneModels buildFromAssetFiles(AssetManager assetManager) {
        GutterSceneModels models = new GutterSceneModels();
        Mesh mesh = models.makeMeshFromGutterSTLFile(assetManager);
        models.mSilos.put("mainSilo", mesh);
        models.mBoundingBox = new BoundingBox(mesh);
        return models;
    }

    public Mesh getSilo(String siloName) {
        return mSilos.get(siloName);
    }

    public Set<String> getSiloNames() {
        return mSilos.keySet();
    }

    public int getNumberOfTrianglesInSilo(String siloName) { return mSilos.get(siloName).size(); }

    public BoundingBox getBoundingBox() {
        return mBoundingBox;
    }

    public final float getEffectiveRadius() {
        return 0.5f * mBoundingBox.getLargestDimension();
    }

    public final XYZf getOffsetFromOriginOfBoundingBoxCentre() {
        return mBoundingBox.getCentre();
    }

    private Mesh makeMeshFromGutterSTLFile(AssetManager assetManager) {
        String fileContents = getSTLContentFromAsset(assetManager, GUTTER_FILE_NAME);
        Mesh mesh = new MeshFactoryFromStlFile(fileContents).makeMesh();
        return mesh;
    }

    private String getSTLContentFromAsset(AssetManager assetManager, String assetFileName) {
        try {
            InputStream is = assetManager.open(assetFileName);
            return FileOperations.SlurpInputStreamToString(is);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
