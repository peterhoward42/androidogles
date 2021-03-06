/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.opengl.application;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.android.opengl.vr_content.DynamicScene;
import com.example.android.opengl.vr_content.Cameraman;
import com.example.android.opengl.vr_content.RendererForMeshCollection;
import com.example.android.opengl.vr_content.SceneLighting;
import com.example.android.opengl.vr_content.ViewProjectionCalculator;
import com.example.android.opengl.vr_content.TransformPipeLineForModel;
import com.example.android.opengl.math.MatrixCombiner;
import com.example.android.opengl.math.TransformFactory;
import com.example.android.opengl.vr_content.ViewingAxis;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 * <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 * <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 * <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

    private RendererForMeshCollection mRendererForMeshCollection;

    private AssetManager mAssetManager;
    private DynamicScene mDynamicScene;
    private Cameraman mCameraman;
    private SceneLighting mSceneLighting;
    private ViewProjectionCalculator mViewProjectionCalculator;
    private float mScreenAspect;

    public MyGLRenderer(
            AssetManager assetManager, DynamicScene dynamicScene, Cameraman cameraman) {
        super();
        mAssetManager = assetManager;
        mDynamicScene = dynamicScene;
        mCameraman = cameraman;
        mSceneLighting = new SceneLighting();
        mViewProjectionCalculator = new ViewProjectionCalculator();
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // We pass the sceneObjectSilos into the constructor for the renderer, because
        // the coordinate data in these models is immutable, and can be packed into
        // the right vertex / bytes format for open gl as a one-shot activity.

        // The real-time rendering of frames later on, takes a transform matrix for
        // each silo, and the application of these transforms to the scene object
        // coordinates is delegated by the lower level renderer onto the hardware.
        mRendererForMeshCollection = new RendererForMeshCollection(mAssetManager, mDynamicScene);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Build the transform lookup table for the triangles renderer
        Map<String, TransformPipeLineForModel> siloTransformPipelines =
                buildSiloTransformPipelines();
        mRendererForMeshCollection.draw(siloTransformPipelines, mSceneLighting.getDirectionTowardsLight());
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mScreenAspect = (float) width / height;
    }

    /** This works out what transforms must be applied to render each of the scene's silos
     * correctly. It is called for every frame refresh - and must therefor consider speed of
     * execution.
     */
    private Map<String, TransformPipeLineForModel> buildSiloTransformPipelines() {
        // For each silo we generate a model-view-projection transform by combining three
        // transforms: ObjectToWorld, WorldToCamera, and Projection. Only the first of which
        // differs per silo. Then we add an auxiliary transform for transforming direction vectors
        // from object to world space.

        final ViewingAxis viewingAxis = mCameraman.getCurrentViewpoint();
        float[] worldToCameraTransform = viewingAxis.worldToCameraTransform();
        float[] projectionTransform = ViewProjectionCalculator.calculateProjectionTransform(
                viewingAxis, mDynamicScene.getCurrentEffectiveSphere(), mScreenAspect);
        // Correctly position each silo in the scene, and combine the three transforms into one
        // for each silo.
        Map<String, TransformPipeLineForModel> mapToReturn = new HashMap<String, TransformPipeLineForModel>();
        for (String siloName : mDynamicScene.getSiloNames()) {
            float[] objectToWorldForVertices =
                    mDynamicScene.getCurrentObjectToWorldTransform(siloName);
            float[] objectToWorldForDirections =
                    TransformFactory.directionTransformFromVertexTransform(objectToWorldForVertices);
            float[] modelViewProjection = MatrixCombiner.combineThree(
                    projectionTransform, worldToCameraTransform, objectToWorldForVertices);
            TransformPipeLineForModel transformPipeLineForModel =
                    new TransformPipeLineForModel(modelViewProjection, objectToWorldForDirections);
            mapToReturn.put(siloName, transformPipeLineForModel);
        }
        return mapToReturn;
    }

    /**
     * Utility method for compiling a OpenGL shader.
     * <p/>
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type       - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode) {

        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);

        // compile it - noting differing error discovery - what a pain!
        GLES20.glCompileShader(shader);
        // Found this error discovery trick using get-internal-value on StackOverflow
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            String msg = "load shader" + ": " + GLES20.glGetShaderInfoLog(shader);
            GLES20.glDeleteShader(shader);
            Log.e(TAG, msg);
            throw new RuntimeException("Could not compile program: " + msg);
        }
        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <p/>
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String contextProvidedByCaller) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, contextProvidedByCaller + ": glError " + error);
            throw new RuntimeException(contextProvidedByCaller + ": glError " + error);
        }
    }
}