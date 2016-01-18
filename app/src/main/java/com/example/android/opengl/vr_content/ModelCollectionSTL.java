package com.example.android.opengl.vr_content;

import android.content.res.AssetManager;

import com.example.android.opengl.geom.BoundingBox;
import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.MeshFactoryFromStlFile;
import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.util.FileOperations;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of IModelCollection comprising just one single model, derived from
 * a given STL file.
 */
public class ModelCollectionSTL implements IModelCollection {

    private Map<String, Mesh> mSilos;
    private BoundingBox mBoundingBox;

    // We provide only a private constructor, to force use of the factory function below.
    private ModelCollectionSTL() {
        mSilos = new HashMap<String, Mesh>();
        mBoundingBox = null;
    }

    /** Factory method - the only way to make one of these.
     */
    public static ModelCollectionSTL buildFromAssetFiles(
            AssetManager assetManager,
            final String assetFileName) {
        ModelCollectionSTL modelCollectionSTL = new ModelCollectionSTL();
        Mesh mesh = modelCollectionSTL.makeMeshFromGutterSTLFile(assetManager, assetFileName);
        modelCollectionSTL.mSilos.put("mainSilo", mesh);
        modelCollectionSTL.mBoundingBox = new BoundingBox(mesh);
        return modelCollectionSTL;
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
        float orthogonalComponent =  0.5f * mBoundingBox.getLargestDimension();
        return (float)Math.hypot(orthogonalComponent, orthogonalComponent);
    }

    public final XYZf getBoundingBoxCentre() {
        return mBoundingBox.getCentre();
    }

    private Mesh makeMeshFromGutterSTLFile(AssetManager assetManager, final String assetFileName) {
        String fileContents = getSTLContentFromAsset(assetManager, assetFileName);
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
