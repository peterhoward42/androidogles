package com.example.android.opengl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by phoward on 13/11/2015.
 */
public class DrawListFactory {

    /**
     * Converts triangle geometry data that is stored in a data model that is convenient for
     * applications in the outside world, into the uniqued and flattened intermediate form that
     * is used to feed an OpenGL-ES pipeline.
     * <p></p>
     * The exam question is to end up with a contiguous array of floats in which each batch of
     * three represent the X,Y,Z components of a vertex. And vertices are packed one after the
     * other, without including any duplicates despite their implied presence in the input data
     * we are transforming. Additionally we build a list of indices into to this array which
     * when traversed will visit the vertices of each of the discrete triangles in the input
     * model in turn.
     * @param triangles The triangles to be converted.
     * @return The transformed data expressed as a {@link DrawList}
     */
    public DrawList buildFrom(Collection<TriangleWorldModel> triangles) {
        // We augment a linear array of vertices as each new and unique one is encountered,
        // and maintain a lookup table of their indices used as we go. We ensure that vertices
        // that are numerically equivalent (approximately) are treated as identical, by
        // using a rounding hashing function.
        ArrayList<XYZf> verticesStoredLinear = new ArrayList<XYZf>();
        ArrayList<Integer> drawingSequence = new ArrayList<Integer>();
        // The following map is keyed on a vertex's roundingHash()
        Map<Object, Integer> indicesOfVertices = new HashMap<Object, Integer>();
        for (TriangleWorldModel triangle : triangles) {
            for (XYZf vertex : triangle.vertices()) {
                Object roundingHash = vertex.roundingHash();
                int index;
                if (indicesOfVertices.containsKey(roundingHash)) {
                    index = indicesOfVertices.get(roundingHash);
                } else {
                    verticesStoredLinear.add(vertex);
                    index = verticesStoredLinear.size() - 1;
                    indicesOfVertices.put(roundingHash, index);
                }
                drawingSequence.add(index);
            }
        }
        // Now we have the linear store of vertices we can build the required
        // data structure to return.
        DrawList dataSource = new DrawList();
        dataSource.vertexFloats = new float[verticesStoredLinear.size() * 3];
        int coordIdx = 0;
        for (int vertexIdx = 0; vertexIdx < verticesStoredLinear.size(); vertexIdx++) {
            dataSource.vertexFloats[coordIdx++] = verticesStoredLinear.get(vertexIdx).X();
            dataSource.vertexFloats[coordIdx++] = verticesStoredLinear.get(vertexIdx).Y();
            dataSource.vertexFloats[coordIdx++] = verticesStoredLinear.get(vertexIdx).Z();
        }
        dataSource.drawListShorts = new short[drawingSequence.size()];
        for (int i = 0; i < drawingSequence.size(); i++)
            dataSource.drawListShorts[i] = (short)((int)(drawingSequence.get(i)));
        return dataSource;
    }
}
