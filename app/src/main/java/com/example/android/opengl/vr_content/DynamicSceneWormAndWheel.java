package com.example.android.opengl.vr_content;

import android.content.res.AssetManager;
import android.os.SystemClock;

import com.example.android.opengl.geom.BoundingBox;
import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.MeshFactoryFromSTLBinary;
import com.example.android.opengl.math.MatrixCombiner;
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
    private final float SCENE_RADIUS_FACTOR = 1.5f;

    public DynamicSceneWormAndWheel(
            AssetManager assetManager) {
        // Deliberately violating the "don't do real work in constructors" guideline here.
        // A pragmatic compromise to provide an immediately-viable object. The exceptions thrown
        // result only from programming and build errors, not runtime conditions.
        mSilos = new HashMap<String, Mesh>();
        mSilos.put(KEY_FOR_WORM, makeMesh(assetManager, STL_FILENAME_WORM));
        mSilos.put(KEY_FOR_WHEEL, makeMesh(assetManager, STL_FILENAME_WHEEL));
    }

    public Mesh getSilo(String siloName) {
        return mSilos.get(siloName);
    }

    public Set<String> getSiloNames() {
        return mSilos.keySet();
    }

    public float getEffectiveRadius() {
        return SCENE_RADIUS_FACTOR * mSilos.get(KEY_FOR_WHEEL).getBoundingBox().getLargestDimension();
    }

    public float[] getCurrentObjectToWorldTransform(String siloName) {
        double wormThetaRadians = WORM_SPEED_OF_ROTATION * SystemClock.uptimeMillis() / 1000.0f;
        if (siloName == KEY_FOR_WORM) {
            Mesh worm = mSilos.get(KEY_FOR_WORM);
            // Animate the worm model's rotation about its own axis.
            float[] t1 = TransformFactory.translation(0,0,0); // no op
            // Translate its centroid to the world centre
            float[] t2 = TransformFactory.inverted(
                    TransformFactory.translation(worm.getBoundingBox().getCentre()));
            // Rotate it so that its axis is oriented how we want it
            float[] t3 = TransformFactory.translation(0,0,0); // no op
            // Translate it out to the edge of the wheel so the teeth mesh
            float[] t4 = TransformFactory.translation(0,0,0); // no op
            return MatrixCombiner.combineFour(t4, t3, t2, t1);
        }
        else {
            Mesh wheel = mSilos.get(KEY_FOR_WHEEL);
            // Animate the wheel model's rotation about its own axis.
            float[] t1 = TransformFactory.translation(0,0,0); // no op
            // Translate its centroid to the world centre
            float[] t2 = TransformFactory.inverted(
                    TransformFactory.translation(wheel.getBoundingBox().getCentre()));
            // Rotate it so that its axis is oriented how we want it
            float[] t3 = TransformFactory.translation(0,0,0); // no op
            return MatrixCombiner.combineThree(t3, t2, t1);
        }
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
