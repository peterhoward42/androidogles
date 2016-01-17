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
package com.example.android.opengl.vr_content;

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

import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.util.FileOperations;
import com.example.android.opengl.util.SystemConstants;
import com.example.android.opengl.geom.MeshSerializer;
import com.example.android.opengl.application.MyGLRenderer;
import com.example.android.opengl.geom.Triangle;
import com.example.android.opengl.geom.XYZf;

/**
 * Capable of rendering into OpenGL-ES, collections of world-space triangles using a single
 * shader program. You provide the triangles in named silos, and these names are used to
 * access differing transform matrices for each silo. Uses OpenGL's drawArrays() function call
 * under the hood.
 */
public class TrianglesRenderer {

    private static final String TAG = "TrianglesRenderer";
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;

    private static final int COMPONENTS_PER_VERTEX =
            POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT;

    private final static int VERTEX_ARRAY_STRIDE_IN_BYTES =
            SystemConstants.BYTES_IN_FLOAT * COMPONENTS_PER_VERTEX;

    private final int mProgram;

    // This map shares keys (silo names) with the ISceneModels provided to the constructor.
    private Map<String, FloatBuffer> mVertexBuffers;
    private Map<String, Integer> mNumberOfVerticesInSilo;

    /**
     * Constructor.
     *
     * @Param assetManager Used to dependency-inject the shader source code to be used.
     * @Param sceneModels The sets of triangles you wish to be repeatedly transformed then
     * rendered.
     */
    public TrianglesRenderer(AssetManager assetManager, ISceneModels sceneModels) {
        // Convert the world scene model representation into the packed form required later for
        // the draw() method.
        mVertexBuffers = new HashMap<String, FloatBuffer>();
        mNumberOfVerticesInSilo = new HashMap<String, Integer>();
        for (String siloName : sceneModels.getSiloNames()) {
            mNumberOfVerticesInSilo.put(siloName,
                    3 * sceneModels.getNumberOfTrianglesInSilo(siloName));
            Mesh mesh = sceneModels.getSilo(siloName);
            mVertexBuffers.put(siloName,
                    makeVertexBufferForSilo(sceneModels.getSilo(siloName)));
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

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        MyGLRenderer.checkGlError("Building GLES20 program");
    }

    public void draw(Map<String, TransformPipelines> siloTransformPipelines,
                     final XYZf towardsLight) {
        GLES20.glUseProgram(mProgram);
        GLES20.glFrontFace(GLES20.GL_CCW);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        int positionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        GLES20.glEnableVertexAttribArray(positionHandle);

        int normalHandle = GLES20.glGetAttribLocation(mProgram, "normal");
        GLES20.glEnableVertexAttribArray(normalHandle);

        int mvpTransformHandle = GLES20.glGetUniformLocation(mProgram, "mvpTransform");

        int modelToWorldDirectionTransformHandle = GLES20.glGetUniformLocation(
                mProgram, "modelToWorldDirectionTransform");

        int towardsLightHandle = GLES20.glGetUniformLocation(mProgram, "towardsLight");
        GLES20.glUniform3fv(towardsLightHandle, 1, towardsLight.asFloatArray(), 0);
        MyGLRenderer.checkGlError("Common part of GLES20 draw() call.");

        // Iterate to draw each silo of triangles separately - each with its own dedicated
        // transform.
        for (String siloName : mVertexBuffers.keySet()) {
            FloatBuffer vertexBuffer = mVertexBuffers.get(siloName);

            // Point GLES20 at position attribute
            vertexBuffer.position(0);
            GLES20.glVertexAttribPointer(positionHandle, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false,
                    VERTEX_ARRAY_STRIDE_IN_BYTES, vertexBuffer);

            // Point GLES20 at normal attribute
            // Note the starting position is offset to be just beyond the position data, and
            // thus at the start of the normal data.
            vertexBuffer.position(POSITION_COMPONENT_COUNT);
            GLES20.glVertexAttribPointer(normalHandle, NORMAL_COMPONENT_COUNT, GLES20.GL_FLOAT, false,
                    VERTEX_ARRAY_STRIDE_IN_BYTES, vertexBuffer);

            TransformPipelines transformPipelines = siloTransformPipelines.get(siloName);
            GLES20.glUniformMatrix4fv(mvpTransformHandle, 1, false, transformPipelines.getMvpForVertices(), 0);

            GLES20.glUniformMatrix3fv(modelToWorldDirectionTransformHandle, 1, false,
                    transformPipelines.getModelToWorldForDirections(), 0);
            MyGLRenderer.checkGlError("End of silo-specific loop in GLES20 draw() call.");

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mNumberOfVerticesInSilo.get(siloName));
        }
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(normalHandle);
    }

    private FloatBuffer makeVertexBufferForSilo(Mesh mesh) {
        int numberOfVertices = 3 * mesh.size();
        int numberOfBytesRequired = VERTEX_ARRAY_STRIDE_IN_BYTES * numberOfVertices;
        ByteBuffer vertexBytes = ByteBuffer.allocateDirect(numberOfBytesRequired);
        vertexBytes.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = vertexBytes.asFloatBuffer();
        vertexBuffer.put(new MeshSerializer(mesh).serializeToContiguousFloats());
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