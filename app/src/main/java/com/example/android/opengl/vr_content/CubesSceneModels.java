package com.example.android.opengl.vr_content;

import com.example.android.opengl.geom.Mesh;
import com.example.android.opengl.geom.MeshFactorySimpleCubes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A trivial implementation of ISceneModels for test and development purposes, with content
 * comprising just a 100mm cube and a 20mm cube.
 */
public class CubesSceneModels implements ISceneModels {

    private Map<String, Mesh> mSilos;

    public CubesSceneModels() {
        mSilos = new HashMap<String, Mesh>();

        Mesh mainSilo = new MeshFactorySimpleCubes(100).makeMesh();
        mSilos.put("mainSilo", mainSilo);

        Mesh auxSilo = new MeshFactorySimpleCubes(20).makeMesh();
        mSilos.put("auxSilo", auxSilo);
    }

    public Mesh getSilo(String siloName) {
        return mSilos.get(siloName);
    }

    public Set<String> getSiloNames() {
        return mSilos.keySet();
    }

    public int getNumberOfTrianglesInSilo(String siloName) { return mSilos.get(siloName).size(); }
}
