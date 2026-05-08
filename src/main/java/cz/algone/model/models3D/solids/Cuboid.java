package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.util.color.ColorPair;

public class Cuboid extends Solid {
    public Cuboid() {
        double h = 0.5;
        color = new ColorPair(new Col(173, 216, 230), null);

        Col c = color.primary();
        vb.add(new Vertex(-h,-h,-h, vc(-h,-h,-h,h), 0,0));
        vb.add(new Vertex( h,-h,-h, vc( h,-h,-h,h), 1,0));
        vb.add(new Vertex( h, h,-h, vc( h, h,-h,h), 1,1));
        vb.add(new Vertex(-h, h,-h, vc(-h, h,-h,h), 0,1));
        vb.add(new Vertex(-h,-h, h, vc(-h,-h, h,h), 0,0));
        vb.add(new Vertex( h,-h, h, vc( h,-h, h,h), 1,0));
        vb.add(new Vertex( h, h, h, vc( h, h, h,h), 1,1));
        vb.add(new Vertex(-h, h, h, vc(-h, h, h,h), 0,1));
        vb.add(new Vertex(-h,-h,-h, vc(-h,-h,-h,h), 0,0));
        vb.add(new Vertex( h,-h,-h, vc( h,-h,-h,h), 1,0));
        vb.add(new Vertex( h,-h, h, vc( h,-h, h,h), 1,1));
        vb.add(new Vertex(-h,-h, h, vc(-h,-h, h,h), 0,1));
        vb.add(new Vertex( h, h,-h, vc( h, h,-h,h), 0,0));
        vb.add(new Vertex(-h, h,-h, vc(-h, h,-h,h), 1,0));
        vb.add(new Vertex(-h, h, h, vc(-h, h, h,h), 1,1));
        vb.add(new Vertex( h, h, h, vc( h, h, h,h), 0,1));
        vb.add(new Vertex(-h,-h,-h, vc(-h,-h,-h,h), 0,0));
        vb.add(new Vertex(-h,-h, h, vc(-h,-h, h,h), 1,0));
        vb.add(new Vertex(-h, h, h, vc(-h, h, h,h), 1,1));
        vb.add(new Vertex(-h, h,-h, vc(-h, h,-h,h), 0,1));
        vb.add(new Vertex( h,-h,-h, vc( h,-h,-h,h), 0,0));
        vb.add(new Vertex( h, h,-h, vc( h, h,-h,h), 1,0));
        vb.add(new Vertex( h, h, h, vc( h, h, h,h), 1,1));
        vb.add(new Vertex( h,-h, h, vc( h,-h, h,h), 0,1));

        int edgeStart = 0;
        addEdge(0, 1); addEdge(1, 2); addEdge(2, 3); addEdge(3, 0);
        addEdge(4, 5); addEdge(5, 6); addEdge(6, 7); addEdge(7, 4);
        addEdge(0, 4); addEdge(1, 5); addEdge(2, 6); addEdge(3, 7);
        pb.add(new Part(TopologyType.LINES, edgeStart, 12));

        int triStart = ib.size();
        addTriangle( 0, 2, 1); addTriangle( 0, 3, 2);
        addTriangle( 4, 5, 6); addTriangle( 4, 6, 7);
        addTriangle( 8, 9,10); addTriangle( 8,10,11);
        addTriangle(12,13,14); addTriangle(12,14,15);
        addTriangle(16,17,18); addTriangle(16,18,19);
        addTriangle(20,21,22); addTriangle(20,22,23);
        pb.add(new Part(TopologyType.TRIANGLES, triStart, 12));

        shader = new ShaderConstant(c);
    }

    /** Vrací RGB barvu odvozenou z xyz souřadnice v rozsahu [-h, h]. */
    private static Col vc(double x, double y, double z, double h) {
        return new Col(
            (int) ((x / h + 1) * 0.5 * 255),
            (int) ((y / h + 1) * 0.5 * 255),
            (int) ((z / h + 1) * 0.5 * 255)
        );
    }
}
