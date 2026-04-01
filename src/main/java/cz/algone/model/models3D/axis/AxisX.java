package cz.algone.model.models3D.axis;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.util.color.ColorPair;

public class AxisX extends Solid {
    public AxisX() {
        color = new ColorPair(new Col(255, 0, 0), null);
        Col c = color.primary();

        vb.add(new Vertex(0,    0,     0,    c)); // 0 – počátek
        vb.add(new Vertex(0.8,  0,     0,    c)); // 1 – konec dříku
        vb.add(new Vertex(0.8,  0.08,  0,    c)); // 2 – základna šipky
        vb.add(new Vertex(0.8, -0.08,  0,    c)); // 3 – základna šipky
        vb.add(new Vertex(1.0,  0,     0,    c)); // 4 – špička šipky

        // LINES – 1 hrana (dřík)
        addEdge(0, 1);
        pb.add(new Part(TopologyType.LINES, 0, 1));

        // TRIANGLES – 1 trojúhelník (šipka)
        int triStart = ib.size();
        addTriangle(2, 3, 4);
        pb.add(new Part(TopologyType.TRIANGLES, triStart, 1));

        shader = new ShaderConstant(c);
    }
}
