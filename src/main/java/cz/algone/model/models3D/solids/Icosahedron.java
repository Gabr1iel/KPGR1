package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.util.color.ColorPair;

import java.util.HashSet;
import java.util.Set;

/** Ikosaédr (D20) – 12 vrcholů, 30 hran, 20 trojúhelníkových stěn. */
public class Icosahedron extends Solid {
    public Icosahedron() {
        color = new ColorPair(new Col(255, 215, 0), null);
        Col c = color.primary();

        double phi = (1 + Math.sqrt(5)) / 2.0;
        double len = Math.sqrt(1 + phi * phi);
        double p = phi / len;
        double q = 1.0 / len;

        double[][] positions = {
            { 0,  q,  p}, { 0, -q,  p}, { 0,  q, -p}, { 0, -q, -p},
            { q,  p,  0}, {-q,  p,  0}, { q, -p,  0}, {-q, -p,  0},
            { p,  0,  q}, {-p,  0,  q}, { p,  0, -q}, {-p,  0, -q}
        };

        int[][] faces = {
            {0,8,4}, {0,4,5}, {0,5,9}, {0,9,1}, {0,1,8},
            {1,8,6}, {8,4,10}, {4,5,2}, {5,9,11}, {9,1,7},
            {6,8,10}, {10,4,2}, {2,5,11}, {11,9,7}, {7,1,6},
            {3,10,6}, {3,6,7}, {3,7,11}, {3,11,2}, {3,2,10}
        };

        double[][] uv = {{0,0}, {1,0}, {0.5, 1}};
        for (int[] face : faces) {
            for (int k = 0; k < 3; k++) {
                double[] pos = positions[face[k]];
                Col vc = posColor(pos[0], pos[1], pos[2], 1.0);
                vb.add(new Vertex(pos[0], pos[1], pos[2], vc, uv[k][0], uv[k][1]));
            }
        }

        int edgeStart = 0;
        Set<Long> edgeSet = new HashSet<>();
        int edgeCount = 0;
        for (int f = 0; f < faces.length; f++) {
            int base = f * 3;
            for (int k = 0; k < 3; k++) {
                int posA = faces[f][k];
                int posB = faces[f][(k + 1) % 3];
                long key = (long) Math.min(posA, posB) * 100 + Math.max(posA, posB);
                if (edgeSet.add(key)) {
                    addEdge(base + k, base + (k + 1) % 3);
                    edgeCount++;
                }
            }
        }
        pb.add(new Part(TopologyType.LINES, edgeStart, edgeCount));

        int triStart = ib.size();
        for (int f = 0; f < faces.length; f++) {
            int base = f * 3;
            addTriangle(base, base + 1, base + 2);
        }
        pb.add(new Part(TopologyType.TRIANGLES, triStart, faces.length));

        shader = new ShaderConstant(c);
    }
}
