package com.example.android.opengl.vr_content;

import com.example.android.opengl.geom.MesherForCube;
import com.example.android.opengl.geom.Triangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A set of models that live in a scene, each in a named silo (person, engine...). The models are
 * defined with coordinates in their own private model space, and need to be shifted to their
 * proper dispositions to create a proper scene.
 */
public class SceneModels {

    private Map<String, Collection<Triangle>> mSilos;

    public SceneModels() {
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
