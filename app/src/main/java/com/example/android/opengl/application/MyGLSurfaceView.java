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

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.android.opengl.vr_content.IModelCollection;
import com.example.android.opengl.vr_content.ISceneAssembler;
import com.example.android.opengl.vr_content.SceneOptics;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(
            Context context,
            IModelCollection sceneModels,
            ISceneAssembler sceneAssembler,
            SceneOptics sceneOptics) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer(context.getAssets(), sceneModels,
                sceneAssembler, sceneOptics);
        setRenderer(mRenderer);
    }
}
