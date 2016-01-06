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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.content.res.AssetManager;
import android.opengl.GLES20;

/**
 * Capable of rendering into OpenGL-ES, collections of world-space triangles using a single
 * shader program. You provide the triangles in named silos, and these names are used to
 * access differing transform matrices for each silo. Uses OpenGL's drawElements() function call
 * under the hood. I.e. the paradigm that consumes a linear array of "uniqued" vertices, and a
 * drawing-order sequence alongside.
 */
public class TrianglesRenderer {

    private static final String TAG = "TrianglesRenderer";

    private final int mProgram;
    private int mPositionHandle; // Don't know why official examples make this a field but retain just in case?
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // This map shares keys (silo names) with the SceneObjectSilos provided to the constructor.
    private Map<String, FloatBuffer> mVertexBuffers; // todo improve name like below
    private Map<String, Integer> mNumberOfVerticesInSilo;
    private final int vertexStride = 3 * SystemConstants.BYTES_IN_FLOAT;
    private float color[] = {0.2f, 0.709803922f, 0.898039216f, 1.0f};

    /**
     * Constructor.
     *
     * @Param assetManager Used to dependency-inject the shader source code to be used.
     * @Param sceneTriangles The sets of triangles you wish to be repeatedly transformed then
     * rendered.
     */
    public TrianglesRenderer(AssetManager assetManager, SceneObjectSilos sceneObjectSilos) {
        // Convert the world scene model representation into the packed form required later for
        // the draw() method.
        mVertexBuffers = new HashMap<String, FloatBuffer>();
        mNumberOfVerticesInSilo = new HashMap<String, Integer>();
        for (String siloName : sceneObjectSilos.getSiloNames()) {
            mNumberOfVerticesInSilo.put(siloName,
                    3 * sceneObjectSilos.getNumberOfTrianglesInSilo(siloName));
            Collection<Triangle> triangles = sceneObjectSilos.getSilo(siloName);
            mVertexBuffers.put(siloName,
                    makeVertexBufferForSilo(sceneObjectSilos.getSilo(siloName)));
        }

        String vertexShaderSource = getShaderFromAsset(assetManager, "vertex-shader.txt");
        String fragmentShaderSource = getShaderFromAsset(assetManager, "frag-shader.txt");

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderSource);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderSource);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    public void draw(Map<String, float[]> mvpMatrices) {
        GLES20.glUseProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Iterate to draw each silo of triangles separately - each with its own dedicated
        // transform.
        for (String siloName : mVertexBuffers.keySet()) {
            FloatBuffer vertexBuffer = mVertexBuffers.get(siloName);
            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                    vertexStride, vertexBuffer);
            float[] mvpMatrix = mvpMatrices.get(siloName);
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
            MyGLRenderer.checkGlError("glUniformMatrix4fv");
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mNumberOfVerticesInSilo.get(siloName));
        }
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    private FloatBuffer makeVertexBufferForSilo(Collection<Triangle> triangles) {
        int numberOfVertices = 3 * triangles.size();
        int numberOfFloats = 3 * numberOfVertices;
        int numberOfBytesRequired = numberOfFloats * SystemConstants.BYTES_IN_FLOAT;
        ByteBuffer vertexBytes = ByteBuffer.allocateDirect(numberOfBytesRequired);
        vertexBytes.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = vertexBytes.asFloatBuffer();
        vertexBuffer.put(new TriangleSerializer(triangles).serializeToContiguousFloats());
        vertexBuffer.position(0);
        return vertexBuffer;
    }

    private String getShaderFromAsset(AssetManager assetManager, String assetFileName) {
        try {
            InputStream is = assetManager.open(assetFileName);
            return FileOperations.SlurpInputStreamToString(is);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}