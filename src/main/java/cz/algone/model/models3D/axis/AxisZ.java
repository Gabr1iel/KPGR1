package cz.algone.model.models3D.axis;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.util.color.ColorPair;

public class AxisZ extends Solid {
    public AxisZ() {
        color = new ColorPair(new Col(0, 0, 255), null);
        Col c = color.primary();

        vb.add(new Vertex(0,    0,     0,    c));
        vb.add(new Vertex(0,    0,     0.8,  c));
        vb.add(new Vertex(0.08, 0,     0.8,  c));
        vb.add(new Vertex(-0.08,0,     0.8,  c));
        vb.add(new Vertex(0,    0,     1.0,  c));

        addEdge(0, 1);
        pb.add(new Part(TopologyType.LINES, 0, 1));

        int triStart = ib.size();
        addTriangle(2, 3, 4);
        pb.add(new Part(TopologyType.TRIANGLES, triStart, 1));

        shader = new ShaderConstant(c);
    }
}
