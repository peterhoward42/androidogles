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
import android.opengl.Matrix;
import android.util.Log;

import com.example.android.opengl.vr_content.ISceneAssembler;
import com.example.android.opengl.vr_content.ISceneModels;
import com.example.android.opengl.vr_content.RenderingTransforms;
import com.example.android.opengl.vr_content.SceneOptics;
import com.example.android.opengl.vr_content.TrianglesRenderer;
import com.example.android.opengl.geom.XYZf;
import com.example.android.opengl.math.MatrixCombiner;
import com.example.android.opengl.math.TransformFactory;

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
    private final XYZf towardsLightInWorldSpace = new XYZf(15f, 8f, 10f).normalised();

    private TrianglesRenderer mTrianglesRenderer;

    private AssetManager mAssetManager;
    private ISceneModels mSceneModels;
    private ISceneAssembler mSceneAssembler;
    private SceneOptics mSceneOptics;
    private float mScreenAspect;

    public MyGLRenderer(AssetManager assetManager, ISceneModels sceneModels,
                        ISceneAssembler sceneAssembler, SceneOptics sceneOptics) {
        super();
        mAssetManager = assetManager;
        mSceneModels = sceneModels;
        mSceneAssembler = sceneAssembler;
        mSceneOptics = sceneOptics;
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
        mTrianglesRenderer = new TrianglesRenderer(mAssetManager, mSceneModels);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Build the transform lookup table for the triangles renderer
        Map<String, RenderingTransforms> siloRenderingMatrices =
                buildSiloRenderingMatrices();
        mTrianglesRenderer.draw(siloRenderingMatrices, towardsLightInWorldSpace);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mScreenAspect = (float) width / height;
    }

    private Map<String, RenderingTransforms> buildSiloRenderingMatrices() {
        // For each silo we generate a model-view-projection transform by combining three
        // transforms: ObjectToWorld, WorldToCamera, and Projection. Only the first of which
        // differs per silo. Then we add an auxiliary transform for transforming direction vectors
        // from object to world space.

        // World to Camera
        float[] worldToCameraTransform = mSceneOptics.calculateWorldToCameraTransform();
        float[] projectionTransform = mSceneOptics.calculateProjectionTransform(mScreenAspect);

        // Correctly position each silo in the scene, and combine the three transforms into one
        // for each silo.
        Map<String, RenderingTransforms> mapToReturn = new HashMap<String, RenderingTransforms>();
        for (String siloName : mSceneAssembler.getSiloNames()) {
            float[] objectToWorldForVertices =
                    mSceneAssembler.getCurrentObjectToWorldTransform(siloName);
            float[] objectToWorldForDirections =
                    TransformFactory.directionTransformFromVertexTransform(objectToWorldForVertices);
            float[] modelViewProjection = MatrixCombiner.combineThree(
                    projectionTransform, worldToCameraTransform, objectToWorldForVertices);
            RenderingTransforms renderingTransforms =
                    new RenderingTransforms(modelViewProjection, objectToWorldForDirections);
            mapToReturn.put(siloName, renderingTransforms);
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