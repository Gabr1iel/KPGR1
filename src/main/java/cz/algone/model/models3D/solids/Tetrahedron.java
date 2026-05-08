package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.util.color.ColorPair;

public class Tetrahedron extends Solid {
    public Tetrahedron() {
        color = new ColorPair(new Col(255, 165, 0), null);

        Col c = color.primary();

        double[][] pos = {
            {0,     0,     0    },
            {1,     0,     0    },
            {0.5,   0.866, 0    },
            {0.5,   0.288, 0.816},
        };
        Col[] cols = {
            new Col(  0,   0,   0),
            new Col(255,   0,   0),
            new Col(127, 220,   0),
            new Col(127,  73, 208),
        };

        int[][] faces = {{0,1,2}, {0,3,1}, {1,3,2}, {2,3,0}};
        double[][] uv = {{0,0}, {1,0}, {0.5, 1}};
        for (int[] face : faces) {
            for (int k = 0; k < 3; k++) {
                int vi = face[k];
                vb.add(new Vertex(pos[vi][0], pos[vi][1], pos[vi][2],
                        cols[vi], uv[k][0], uv[k][1]));
            }
        }

        int edgeStart = 0;
        addEdge(0, 1); addEdge(1, 2); addEdge(2, 0);
        addEdge(3, 4); addEdge(6, 7); addEdge(9, 10);
        pb.add(new Part(TopologyType.LINES, edgeStart, 6));

        int triStart = ib.size();
        addTriangle(0, 1, 2);
        addTriangle(3, 4, 5);
        addTriangle(6, 7, 8);
        addTriangle(9, 10, 11);
        pb.add(new Part(TopologyType.TRIANGLES, triStart, 4));

        shader = new ShaderConstant(c);
    }
}
