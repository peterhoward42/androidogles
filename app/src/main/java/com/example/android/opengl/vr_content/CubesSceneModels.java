package com.example.android.opengl.vr_content;

import com.example.android.opengl.geom.MesherForCube;
import com.example.android.opengl.geom.Triangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A trivial implementation of ISceneModels for test and development purposes, with content
 * comprising just a 100mm cube and a 20mm cube.
 */
public class CubesSceneModels implements ISceneModels {

    private Map<String, Collection<Triangle>> mSilos;

    public CubesSceneModels() {
        mSilos = new HashMap<String, Collection<Triangle>>();

        Collection<Triangle> mainSilo = new ArrayList<Triangle>();
        mainSilo.addAll(new MesherForCube(100).getTriangles());
        mSilos.put("mainSilo", mainSilo);

        Collection<Triangle> auxSilo = new ArrayList<Triangle>();
        auxSilo.addAll(new MesherForCube(20).getTriangles());
        mSilos.put("auxSilo", auxSilo);
    }

    public Collection<Triangle> getSilo(String siloName) {
        return mSilos.get(siloName);
    }

    public Set<String> getSiloNames() {
        return mSilos.keySet();
    }

    public int getNumberOfTrianglesInSilo(String siloName) { return mSilos.get(siloName).size(); }
}
