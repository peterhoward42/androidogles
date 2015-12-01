package com.example.android.opengl;

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
public class SceneObjectSilos {

    private Map<String, Collection<Triangle>> mSilos;

    public SceneObjectSilos() {
        mSilos = new HashMap<String, Collection<Triangle>>();

        Collection<Triangle> mainSilo = new ArrayList<Triangle>();
        mainSilo.addAll(new MesherForCuboid(new XYZf(100, 100, 100)).getTriangles());
        mSilos.put("mainSilo", mainSilo);

        Collection<Triangle> auxSilo = new ArrayList<Triangle>();
        auxSilo.addAll(new MesherForCuboid(new XYZf(20, 20, 20)).getTriangles());
        mSilos.put("auxSilo", auxSilo);
    }

    public Collection<Triangle> getSilo(String siloName) {
        return mSilos.get(siloName);
    }

    public Set<String> getSiloNames() {
        return mSilos.keySet();
    }
}
