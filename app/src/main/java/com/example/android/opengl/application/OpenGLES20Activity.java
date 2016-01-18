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

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.android.opengl.vr_content.ModelCollectionSTL;
import com.example.android.opengl.vr_content.SceneAssemblerCubes;
import com.example.android.opengl.vr_content.IModelCollection;
import com.example.android.opengl.vr_content.ModelCollectionCubes;
import com.example.android.opengl.vr_content.SceneAssemblerSTL;
import com.example.android.opengl.vr_content.ISceneAssembler;
import com.example.android.opengl.vr_content.SceneOptics;

public class OpenGLES20Activity extends Activity {

    private GLSurfaceView mGLView;

    private final int CUBES = 1;
    private final int GUTTER = 2;
    private final int BLADE = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        ISceneAssembler sceneAssembler;
        IModelCollection sceneModels;

        // We dial in which scene we want to use here.
        final int choice = BLADE;
        switch (choice) {
            case CUBES:
                sceneModels = new ModelCollectionCubes();
                sceneAssembler = new SceneAssemblerCubes();
                break;
            case GUTTER:
                ModelCollectionSTL gutter = ModelCollectionSTL.buildFromAssetFiles(
                        getApplicationContext().getAssets(), "gutter.txt");
                sceneModels = gutter;
                sceneAssembler = new SceneAssemblerSTL(gutter);
                break;
            case BLADE:
                ModelCollectionSTL blade = ModelCollectionSTL.buildFromAssetFiles(
                        getApplicationContext().getAssets(), "blade.txt");
                sceneModels = blade;
                sceneAssembler = new SceneAssemblerSTL(blade);
                break;
            default:
                sceneModels = null;
                sceneAssembler = null;
        }

        mGLView = new MyGLSurfaceView(
                this,
                sceneModels,
                sceneAssembler,
                new SceneOptics(sceneAssembler.getEffectiveRadius()));
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}