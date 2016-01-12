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
package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

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
    private final XYZf towardsLightInWorldSpace = new XYZf(0f, .2f, 1.0f).normalised();

    private TrianglesRenderer mTrianglesRenderer;

    private AssetManager mAssetManager;
    private SceneObjectSilos mSceneObjectSilos;
    private SceneDirector mSceneDirector;
    private CameraLookAt mCameraLookAt;
    private float mScreenAspect;

    public MyGLRenderer(AssetManager assetManager,
                        SceneObjectSilos sceneObjectSilos, SceneDirector sceneDirector) {
        super();
        mAssetManager = assetManager;
        mSceneObjectSilos = sceneObjectSilos;
        mSceneDirector = sceneDirector;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        final XYZf cameraLookAtPoint = new XYZf(0, 0, 0);
        mCameraLookAt = new CameraLookAt(cameraLookAtPoint);

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // We pass the sceneObjectSilos into the constructor for the renderer, because
        // the coordinate data in these models is immutable, and can be packed into
        // the right vertex / bytes format for open gl as a one-shot activity.

        // The real-time rendering of frames later on, takes a transform matrix for
        // each silo, and the application of these transforms to the scene object
        // coordinates is delegated by the lower level renderer onto the hardware.
        mTrianglesRenderer = new TrianglesRenderer(mAssetManager, mSceneObjectSilos);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set camera distance back from the action towards the viewer enough for perspective
        // transform to nearly fill the screen. OpenGL has a RH coordinate system, so
        // the Z axis grows from rear to front of the phone.
        XYZf cameraPosition = animatedPosition();

        // Build the transform lookup table for the triangles renderer
        // Map<String, float[]> siloRenderingMatrices = buildSiloRenderingMatrices(cameraPosition);
        Map<String, RenderingTransforms> siloRenderingMatrices =
                buildSiloRenderingMatrices(cameraPosition);
        mTrianglesRenderer.draw(siloRenderingMatrices, towardsLightInWorldSpace);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mScreenAspect = (float) width / height;
    }

    private XYZf animatedPosition() {
        // Centred over Greenwich, oscillating with SHM East and West.
        final float period = 2; // seconds
        final float amplitude = 35; // degrees
        final float latitude = 51.4f; // degrees
        final float orbitHeightFromCentre = 200; // scene linear dimensions
        float[] equatorAtMeridian = new float[]{0, 0, orbitHeightFromCentre, 1};

        float longitude = new TimeBasedSinusoid(amplitude, period).evaluateAtTimeNow();

        float[] eulerRotationsMatrix = new float[16];
        Matrix.setRotateEulerM(eulerRotationsMatrix, 0, latitude, longitude, 0);
        float[] positionVect = new float[4];
        Matrix.multiplyMV(positionVect, 0, eulerRotationsMatrix, 0, equatorAtMeridian, 0);
        return new XYZf(positionVect[0], positionVect[1], positionVect[2]);
    }

    private Map<String, RenderingTransforms> buildSiloRenderingMatrices(XYZf cameraPosition) {
        // For each silo we generate a model-view-projection transform by combining three
        // transforms: ObjectToWorld, WorldToCamera, and Projection. Only the first of which
        // differs per silo. Then we add an auxiliary transform for transforming direction vectors
        // from object to world space.

        // World to Camera
        float[] worldToCameraTransform = mCameraLookAt.worldToCameraTransform(cameraPosition);

        // Projection
        final float FOV = 90;
        final float near = 120;
        final float far = 280;
        float[] projectionTransform = new float[16];
        Matrix.perspectiveM(projectionTransform, 0, FOV, mScreenAspect, near, far);

        // Correctly position each silo in the scene, and combine the three transforms into one
        // for each silo.
        Map<String, RenderingTransforms> mapToReturn = new HashMap<String, RenderingTransforms>();
        for (String siloName : mSceneDirector.getSiloNames()) {
            float[] objectToWorldForVertices =
                    mSceneDirector.getCurrentObjectToWorldTransform(siloName);
            float[] objectToWorldForDirections =
                    TransformFactory.directionTransformFromVertexTransform(objectToWorldForVertices);
            float[] mvp = MatrixCombiner.combineThree(
                    projectionTransform, worldToCameraTransform, objectToWorldForVertices);
            RenderingTransforms renderingTransforms =
                    new RenderingTransforms(mvp, objectToWorldForDirections);
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

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        MyGLRenderer.checkGlError("load shader a");

        // add the source code to the shader
        GLES20.glShaderSource(shader, shaderCode);
        MyGLRenderer.checkGlError("load shader b");

        // compile it - noting differing error discovery - what a pain!
        GLES20.glCompileShader(shader);
        // Found this error discovery trick using get-internal-value on StackOverflow
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            String msg = "load shader c" + ": " + GLES20.glGetShaderInfoLog(shader);
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