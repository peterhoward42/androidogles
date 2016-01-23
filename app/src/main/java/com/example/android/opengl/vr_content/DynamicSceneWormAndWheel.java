package com.example.android.opengl.vr_content;

import android.content.res.AssetManager;
import android.os.SystemClock;

import com.example.android.opengl.geom.BoundingBox;
import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.MeshFactoryFromSTLBinary;
import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.math.MatrixCombiner;
import com.example.android.opengl.math.TransformApply;
import com.example.android.opengl.math.TransformFactory;

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

    private final double WORM_SPEED_OF_ROTATION = 2.0f; // radians/s
    private final double GEARING_FACTOR = 1.0 / 20.0;

    private float[] mStaticWorldTransformWorm;
    private float[] mStaticWorldTransformWheel;
    private BoundingBox mStaticWormBoundingBox;
    private BoundingBox mStaticWheelBoundingBox;
    private BoundingBox mStaticAssemblyBoundingBox;


    public DynamicSceneWormAndWheel(
            AssetManager assetManager) {
        // Deliberately violating the "don't do real work in constructors" guideline here.
        // A pragmatic compromise to provide an immediately-viable object. The exceptions thrown
        // result only from programming and build errors, not runtime conditions.
        mSilos = new HashMap<String, Mesh>();
        mSilos.put(KEY_FOR_WORM, makeMesh(assetManager, STL_FILENAME_WORM));
        mSilos.put(KEY_FOR_WHEEL, makeMesh(assetManager, STL_FILENAME_WHEEL));
        initMetaDataForWorm();
        initMetaDataForWheel();
        initAssemblyStaticBoundingBox();
    }

    public Mesh getSilo(String siloName) {
        return mSilos.get(siloName);
    }

    public Set<String> getSiloNames() {
        return mSilos.keySet();
    }

    public float getEffectiveRadius() {
        float halfBox = 0.5f * mStaticAssemblyBoundingBox.getLargestDimension();
        return (float)Math.hypot((double)halfBox, (double)halfBox);
    }

    public float[] getCurrentObjectToWorldTransform(String siloName) {
        // Rotatative animations are a function of angular velocity and elapsed time
        double wormThetaRadians = WORM_SPEED_OF_ROTATION * SystemClock.uptimeMillis() / 1000.0f;
        if (siloName == KEY_FOR_WORM) {
            // We take the static transform that places the worm into the world in the right
            // place, and add a rotational animation to it
            float[] wormAnimTransformation =
                    TransformFactory.yAxisRotation((float)Math.toDegrees(wormThetaRadians));
            return MatrixCombiner.combineTwo(wormAnimTransformation, mStaticWorldTransformWorm);
        }
        else {
            double wheelThetaRadians = GEARING_FACTOR * wormThetaRadians;
            float[] wheelAnimTransformation =
                    TransformFactory.zAxisRotation((float)Math.toDegrees(wheelThetaRadians));
            return MatrixCombiner.combineTwo(wheelAnimTransformation, mStaticWorldTransformWheel);
        }
    }

    private void initMetaDataForWorm() {
        Mesh wormInModelSpace = mSilos.get(KEY_FOR_WORM);
        mStaticWormBoundingBox = new BoundingBox(wormInModelSpace);

        // Translate it so that its centroid is at the origin
        XYZf wormCentre = mStaticWormBoundingBox.getCentre();
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

    private void initMetaDataForWheel() {
        Mesh wheelInModelSpace = mSilos.get(KEY_FOR_WHEEL);
        mStaticWormBoundingBox = new BoundingBox(wheelInModelSpace);

        // Translate it so that its centroid is at the origin
        XYZf wheelCentre = mStaticWormBoundingBox.getCentre();
        float[] transformToCenter =
                TransformFactory.inverted(TransformFactory.translation(wheelCentre));

        // Swivel it around to align it with the right axis
        float[] transformToAlignAxis = TransformFactory.yAxisRotation(0.0f); // no-op till we can see it

        // Rotate it a tiny bit to align the pitch of the teeth with those on the worm
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

    private void initAssemblyStaticBoundingBox() {
        BoundingBox staticWormBoxInWorld =
                TransformApply.boundingBox(mStaticWorldTransformWorm, mStaticWormBoundingBox);
        BoundingBox staticWheelBoxInWorld =
                TransformApply.boundingBox(mStaticWorldTransformWheel, mStaticWheelBoundingBox);
        mStaticAssemblyBoundingBox = BoundingBox.combine(staticWormBoxInWorld, staticWheelBoxInWorld);
    }
}
