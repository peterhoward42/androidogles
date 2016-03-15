package com.example.android.opengl.vr_content;

import android.content.res.AssetManager;

import com.example.android.opengl.mesh.BoundingBox;
import com.example.android.opengl.mesh.Mesh;
import com.example.android.opengl.mesh.MeshFactoryFromSTLAscii;
import com.example.android.opengl.mesh.MeshFactoryFromSTLBinary;
import com.example.android.opengl.mesh.MeshVertexSmoother;
import com.example.android.opengl.primitives.Sphere;
import com.example.android.opengl.primitives.XYZf;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.util.FileOperations;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of DynamicScene comprising just one single model, derived from
 * a given STL file.
 */
public class DynamicSceneSTL implements DynamicScene {

    private Mesh mTheSingleMesh;

    private static final String UNUSED_NAME = "unused_name";

    // We provide only a private constructor, to force use of the factory function below.
    private DynamicSceneSTL() {
        mTheSingleMesh = null;
    }

    /**
     * Factory method - the only way to make one of these.
     */
    public static DynamicSceneSTL buildFromSTLFile(
            AssetManager assetManager,
            final String assetFileName) {
        DynamicSceneSTL dynamicSceneSTL = new DynamicSceneSTL();
        dynamicSceneSTL.mTheSingleMesh =
                dynamicSceneSTL.makeMeshFromSTLFile(assetManager, assetFileName);
        return dynamicSceneSTL;
    }

    public Mesh getSilo(String siloName) {
        // There is only the one mesh, so we ignore the name lookup
        return mTheSingleMesh;
    }

    public Set<String> getSiloNames() {
        return new HashSet<String>(Arrays.asList(new String[]{UNUSED_NAME}));
    }

    public BoundingBox getBoundingBox() {
        return mTheSingleMesh.getBoundingBox();
    }

    public Sphere getCurrentEffectiveSphere() {
        final float orthogonalComponent = 0.5f * getBoundingBox().getLargestDimension();
        final float radius = (float) Math.hypot(orthogonalComponent, orthogonalComponent);
        return new Sphere(new XYZf(0, 0, 0), radius);
    }

    public final XYZf getBoundingBoxCentre() {
        return getBoundingBox().getCentre();
    }

    public float[] getCurrentObjectToWorldTransform(String siloName) {
        XYZf offsetOfCentre = getBoundingBoxCentre();
        float[] transform = TransformFactory.translation(offsetOfCentre);
        return TransformFactory.inverted(transform);
    }

    private Mesh makeMeshFromSTLFile(AssetManager assetManager, final String assetFileName) {
        Mesh mesh = null;
        if (assetFileName.endsWith(".txt")) {
            String fileContents = getSTLContentFromAsset(assetManager, assetFileName);
            mesh = new MeshFactoryFromSTLAscii(fileContents).makeMesh();
        } else if (assetFileName.endsWith(".stl")) {
            try {
                InputStream inputStream = assetManager.open(assetFileName);
                MeshFactoryFromSTLBinary factory = new MeshFactoryFromSTLBinary(inputStream);
                mesh = factory.makeMesh();
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        MeshVertexSmoother smoother = new MeshVertexSmoother(mesh);
        // Defeat smoothing operation for load speed.
        // smoother.doSmoothing();
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
