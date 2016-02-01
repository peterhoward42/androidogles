package com.example.android.opengl.mesh;

import com.example.android.opengl.primitives.Triangle;
import com.example.android.opengl.primitives.XYZf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Capable of examining a {@link Mesh}, to deduce which of the adjacent constituent triangles
 * are probably neighbouring facets on a continuously curved surface, and whose rendering would
 * be improved by tweaking the local vertex normals at those points to represent the underlying
 * curvature. You construct one of these by passing the mesh you want to tweak as a parameter, and
 * this class tweaks it in in-situ. The algorithm assumes that the mesh is of an engineering type
 * geometry rather than being unstructured and irregular. I.e. certain deductions are reasonable.
 */
public class MeshVertexSmoother {

    /* We need to be able to work out which vertices we are treating as being equivalent, despite
       them having tiny numerical differences in their vertex coordinates. We reconcile these
       differences by using an identification scheme using a hash string for vertices that
       represents - its numerically rounded position.

       We also wish to avoid nested iterations and searches over the data, so we build lookup
       tables to help us, at the cost of a single pass through the data at the start.
     */

    private Mesh theMesh;

    // Lookup tables

    Map<String, List<XYZf>> hash2Vertices;
    Map<String, Set<MeshTriangle>> hash2Triangles;

    public MeshVertexSmoother(Mesh theMesh) {
        this.theMesh = theMesh;

        this.hash2Vertices = new HashMap<String, List<XYZf>>();
        this.hash2Triangles = new HashMap<String, Set<MeshTriangle>>();
    }

    public void doSmoothing() {
        // First build the quick-access lookup tables to avoid nested search loops.
        buildLookupTables();

        // Now we consider all the vertices that are shared by two or more triangles
        for (String vertexHash: hash2Triangles.keySet()) {
            Set<MeshTriangle> touchingTriangles = hash2Triangles.get(vertexHash);
            if (touchingTriangles.size() < 2)
                continue;
            // So there are at least two triangles that share this vertex.
            doSmoothingAtVertex(vertexHash, touchingTriangles);
        }
    }

    private void doSmoothingAtVertex(final String vertexHash, Set<MeshTriangle> touchingTriangles) {
        // We know only that we have been given a set of triangles that touch at a vertex
        // in common. We will extract sucessive batches of triangles from this set to work on, on
        // the basis of them having roughly the same surface normal.

        // Until we have exhausted the candidates...
        while (touchingTriangles.size() != 0) {
            MeshTriangle seedTriangle = touchingTriangles.iterator().next();
            final XYZf seedTrianglesNormal = seedTriangle.getPrimitiveTriangle().getNormal();
            Set<MeshTriangle> trianglesToSmooth = isolateApproxMatchNormals(
                    touchingTriangles, seedTrianglesNormal);
            // Deplete the candidate set as we go.
            touchingTriangles.removeAll(trianglesToSmooth);
            // If this set comprises only one triangle, we move on
            if (trianglesToSmooth.size() == 1)
                continue;
            final XYZf smoothedNormal = calcAverageOfSurfaceNormals(trianglesToSmooth);
            for (MeshTriangle triangleToSmooth: trianglesToSmooth) {
                overrideVertexNormal(triangleToSmooth, smoothedNormal, vertexHash);
            }
        }
    }

    private XYZf calcAverageOfSurfaceNormals(final Set<MeshTriangle> triangles) {
        XYZf cumulativeVector = new XYZf(0,0,0);
        for (MeshTriangle triangle: triangles) {
            cumulativeVector = cumulativeVector.plus(triangle.getPrimitiveTriangle().getNormal());
        }
        return cumulativeVector.normalised();
    }

    private void overrideVertexNormal(
            MeshTriangle triangle,
            final XYZf newNormal,
            final String hashOfVertexToAlter) {
        XYZf[] triangleVertices = triangle.getPrimitiveTriangle().vertices();
        // Bit ugly to search for the right vertex to fiddle with, but it's only a max of three
        // iterations per triangle, and no inner loops.
        for (int i = 0; i < 3; i++) {
            String vertexHash = triangleVertices[i].hashAfterNumericalRounding();
            if (vertexHash == hashOfVertexToAlter) {
                // Overwrite the vertex normal.
                triangle.getVertexNormals()[i] = newNormal;
                return; // return statement executing in-loop signals success
            }
        }
        throw new RuntimeException("Cannot override a vertex normal - because none found with matching hash");
    }

    private Set<MeshTriangle> isolateApproxMatchNormals(
            Set<MeshTriangle> triangles, final XYZf normalToApproxMatch) {
        Set<MeshTriangle> found = new HashSet<MeshTriangle>();
        for (MeshTriangle triangle : triangles) {
            if (normalsAreSimilarEnough(
                    normalToApproxMatch,
                    triangle.getPrimitiveTriangle().getNormal())) {
                found.add(triangle);
            }
        }
        return found;
    }

    private boolean normalsAreSimilarEnough(final XYZf normalA, final XYZf normalB) {
        // Measure of parallelism is the cosine of the angular difference
        final float measureOfParallelism = normalA.dotProduct(normalB);
        final float SMALL_ANGLE_DEGREES = 10.0f;
        final float SUFFICIENTLY_PARALLEL = (float)Math.cos(Math.toRadians(SMALL_ANGLE_DEGREES));
        if (measureOfParallelism >= SUFFICIENTLY_PARALLEL)
            return true;
        else
            return false;
    }

    private void buildLookupTables() {
        for (MeshTriangle triangle: theMesh.getTriangles()) {
            for (XYZf vertex: triangle.getPrimitiveTriangle().vertices()) {
                String hash = vertex.hashAfterNumericalRounding();
                if (hash2Vertices.containsKey(hash) == false) {
                    hash2Vertices.put(hash, new ArrayList<XYZf>());
                    hash2Triangles.put(hash, new HashSet<MeshTriangle>());
                }
                hash2Vertices.get(hash).add(vertex);
                hash2Triangles.get(hash).add(triangle);
            }
        }
    }
}
