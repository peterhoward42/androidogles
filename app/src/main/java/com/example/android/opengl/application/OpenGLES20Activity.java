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

import com.example.android.opengl.client.NetworkPositionFeeder;
import com.example.android.opengl.vr_content.Cameraman;
import com.example.android.opengl.vr_content.CameramanForWormAndWheel;
import com.example.android.opengl.vr_content.CameramanNetworked;
import com.example.android.opengl.vr_content.CameramanOrbiting;
import com.example.android.opengl.vr_content.CameramanStatic;
import com.example.android.opengl.vr_content.DynamicScene;
import com.example.android.opengl.vr_content.DynamicSceneCubes;
import com.example.android.opengl.vr_content.DynamicSceneSTL;
import com.example.android.opengl.vr_content.DynamicSceneWormAndWheel;

public class OpenGLES20Activity extends Activity {

    private GLSurfaceView mGLView;

    private final int CUBES_PROGRAMMATIC_GENERATED = 1;
    private final int GUTTER_AS_SINGLE_MODEL_FROM_STL_FILE = 2;
    private final int WORM_AND_WHEEL_ANIMATED = 3;
    private final int STOP_VALVE = 4;
    private final int DIFF = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DynamicScene dynamicScene;
        Cameraman cameraman;

        // CHOOSE SCENE HERE
        final int choice = STOP_VALVE;

        switch (choice) {
            case CUBES_PROGRAMMATIC_GENERATED:
                dynamicScene = new DynamicSceneCubes();
                cameraman = new CameramanStatic(dynamicScene.getCurrentEffectiveSphere());
                break;
            case GUTTER_AS_SINGLE_MODEL_FROM_STL_FILE:
                dynamicScene = DynamicSceneSTL.buildFromSTLFile(
                        getApplicationContext().getAssets(), "gutter.txt");
                cameraman = new CameramanOrbiting(dynamicScene.getCurrentEffectiveSphere());
                break;
            case WORM_AND_WHEEL_ANIMATED:
                dynamicScene = new DynamicSceneWormAndWheel(getApplicationContext().getAssets());
                cameraman = new CameramanForWormAndWheel();
                break;
            case STOP_VALVE:
                dynamicScene = DynamicSceneSTL.buildFromSTLFile(
                        getApplicationContext().getAssets(), "stop-valve.stl");
                CameramanNetworked cameramanNetworked = new CameramanNetworked(dynamicScene
                        .getCurrentEffectiveSphere());
                cameraman = cameramanNetworked;
                NetworkPositionFeeder networkPositionFeeder =
                        new NetworkPositionFeeder(cameramanNetworked.injectedPosition);
                break;
            case DIFF:
                dynamicScene = DynamicSceneSTL.buildFromSTLFile(
                        getApplicationContext().getAssets(), "diff.stl");
                cameraman = new CameramanOrbiting(dynamicScene.getCurrentEffectiveSphere());
                break;
            default:
                dynamicScene = null;
                cameraman = null;
        }

        mGLView = new MyGLSurfaceView(this, dynamicScene, cameraman);
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