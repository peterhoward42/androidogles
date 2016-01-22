package com.example.android.opengl.vr_content;

import android.content.res.AssetManager;

import com.example.android.opengl.geom.BoundingBox;
import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.MeshFactoryFromSTLAscii;
import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.util.FileOperations;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of DynamicScene comprising just one single model, derived from
 * a given STL file.
 */
public class DynamicSceneSTL implements DynamicScene {

    private Map<String, Mesh> mSilos;
    private BoundingBox mBoundingBox;

    // We provide only a private constructor, to force use of the factory function below.
    private DynamicSceneSTL() {
        mSilos = new HashMap<String, Mesh>();
        mBoundingBox = null;
    }

    /** Factory method - the only way to make one of these.
     */
    public static DynamicSceneSTL buildFromAssetFiles(
            AssetManager assetManager,
            final String assetFileName) {
        DynamicSceneSTL sceneMeshCollectionSTL = new DynamicSceneSTL();
        Mesh mesh = sceneMeshCollectionSTL.makeMeshFromSTLFile(assetManager, assetFileName);
        sceneMeshCollectionSTL.mSilos.put("mainSilo", mesh);
        sceneMeshCollectionSTL.mBoundingBox = new BoundingBox(mesh);
        return sceneMeshCollectionSTL;
    }

    public Mesh getSilo(String siloName) {
        return mSilos.get(siloName);
    }

    public Set<String> getSiloNames() {
        return mSilos.keySet();
    }

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

    public float[] getCurrentObjectToWorldTransform(String siloName) {
        XYZf offsetOfCentre = getBoundingBoxCentre();
        float[] transform = TransformFactory.translation(offsetOfCentre);
        return TransformFactory.inverted(transform);
    }

    private Mesh makeMeshFromSTLFile(AssetManager assetManager, final String assetFileName) {
        String fileContents = getSTLContentFromAsset(assetManager, assetFileName);
        Mesh mesh = new MeshFactoryFromSTLAscii(fileContents).makeMesh();
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
