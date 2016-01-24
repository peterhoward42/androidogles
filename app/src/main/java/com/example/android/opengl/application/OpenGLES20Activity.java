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

import com.example.android.opengl.vr_content.DynamicScene;
import com.example.android.opengl.vr_content.DynamicSceneCubes;
import com.example.android.opengl.vr_content.DynamicSceneSTL;
import com.example.android.opengl.vr_content.DynamicSceneWormAndWheel;
import com.example.android.opengl.vr_content.SceneOptics;

public class OpenGLES20Activity extends Activity {

    private GLSurfaceView mGLView;

    private final int CUBES_PROGRAMMATIC_GENERATED = 1;
    private final int CUTTER_AS_SINGLE_MODEL_FROM_STL_FILE = 2;
    private final int WORM_AND_WHEEL_ANIMATED = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        DynamicScene dynamicScene;

        // We dial in which scene we want to use here.
        final int choice = WORM_AND_WHEEL_ANIMATED;
        switch (choice) {
            case CUBES_PROGRAMMATIC_GENERATED:
                dynamicScene = new DynamicSceneCubes();
                break;
            case CUTTER_AS_SINGLE_MODEL_FROM_STL_FILE:
                dynamicScene = DynamicSceneSTL.buildFromSTLFile(
                        getApplicationContext().getAssets(), "gutter.txt");
                break;
            case WORM_AND_WHEEL_ANIMATED:
                dynamicScene = new DynamicSceneWormAndWheel(getApplicationContext().getAssets());
                break;
            default:
                dynamicScene = null;
        }

        mGLView = new MyGLSurfaceView(
                this,
                dynamicScene,
                new SceneOptics(dynamicScene.getEffectiveRadius()));
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