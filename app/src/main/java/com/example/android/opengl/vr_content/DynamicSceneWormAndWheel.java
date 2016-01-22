package com.example.android.opengl.vr_content;

import android.content.res.AssetManager;

import com.example.android.opengl.geom.BoundingBox;
import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.MeshFactoryFromSTLAscii;
import com.example.android.opengl.geom.MeshFactoryFromSTLBinary;
import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.math.MatrixCombiner;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.util.FileOperations;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of DynamicScene that presents a worm gear and enmeshing worm wheel, with
 * the pair animated to simulate their interaction.
 */
public class DynamicSceneWormAndWheel implements DynamicScene {

    private Map<String, Mesh> mSilos;

    private final String KEY_FOR_WORM = "WORM";
    private final String KEY_FOR_WHEEL = "WHEEL";
    private final String STL_FILENAME_WORM = "worm-gear.stl";
    private final String STL_FILENAME_WHEEL = "worm-wheel.stl";

    private float[] mStaticWorldTransformWorm;
    private float[] mStaticWorldTransformWheel;

    public DynamicSceneWormAndWheel(
            AssetManager assetManager) {
        // Deliberately violating the "don't do real work in constructors" guideline here.
        // A pragmatic compromise to provide an immediately-viable object. The exceptions thrown
        // result only from programming and build errors, not runtime conditions.
        mSilos = new HashMap<String, Mesh>();
        mSilos.put(KEY_FOR_WORM, makeMesh(assetManager, STL_FILENAME_WORM));
        mSilos.put(KEY_FOR_WHEEL, makeMesh(assetManager, STL_FILENAME_WHEEL));
        initStaticWorldTransformForWorm();
        initStaticWorldTransformForWheel();
    }

    public Mesh getSilo(String siloName) {
        return mSilos.get(siloName);
    }

    public Set<String> getSiloNames() {
        return mSilos.keySet();
    }

    private void initStaticWorldTransformForWorm() {
        Mesh wormInModelSpace = mSilos.get(KEY_FOR_WORM);
        BoundingBox boundingBoxOfWormInModelSpace = new BoundingBox(wormInModelSpace);

        // Translate it so that its centroid is at the origin
        XYZf wormCentre = boundingBoxOfWormInModelSpace.getCentre();
        float[] transformToCenter =
                TransformFactory.inverted(TransformFactory.translation(wormCentre));

        // Swivel it around to align it with the right axis
        float[] transformToAlignAxis = TransformFactory.yAxisRotation(0.0f); // no-op till we can see it

        // Offset it to near the circumference of the wheel so they mesh
        float[] transformToShiftToWheel = TransformFactory.translation(0, 0, 0); // no-op

        float[] resultantTransform = MatrixCombiner.combineThree(
                transformToShiftToWheel, transformToAlignAxis, transformToCenter);

        mStaticWorldTransformWorm = resultantTransform;
    }

    private void initStaticWorldTransformForWheel() {
        Mesh wheelInModelSpace = mSilos.get(KEY_FOR_WHEEL);
        BoundingBox boundingBoxOfWheelInModelSpace = new BoundingBox(wheelInModelSpace);

        // Translate it so that its centroid is at the origin
        XYZf wheelCentre = boundingBoxOfWheelInModelSpace.getCentre();
        float[] transformToCenter =
                TransformFactory.inverted(TransformFactory.translation(wheelCentre));

        // Swivel it around to align it with the right axis
        float[] transformToAlignAxis = TransformFactory.yAxisRotation(0.0f); // no-op till we can see it

        // Rotate it a tiny bit to align the pitch of the teeth
        float[] transformToAlignTeeth = TransformFactory.yAxisRotation(0.0f); // no op

        float[] resultantTransform = MatrixCombiner.combineThree(
                transformToAlignTeeth, transformToAlignAxis, transformToCenter);

        mStaticWorldTransformWheel = resultantTransform;
    }

    private Mesh makeMesh(AssetManager assetManager, final String assetFileName) {
        try {
            InputStream inputStream = assetManager.open(assetFileName);
            MeshFactoryFromSTLBinary factory = new MeshFactoryFromSTLBinary(inputStream);
            Mesh mesh = factory.makeMesh();
            inputStream.close();
            return mesh;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
