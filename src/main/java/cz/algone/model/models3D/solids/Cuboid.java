package cz.algone.model.models3D.wiredSolids.solids;

import cz.algone.model.models3D.wiredSolids.Solid;
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
        // 24 vrcholů (4 na stěnu) – každá stěna má vlastní UV 0→1
        // Back (z=-h)
        vb.add(new Vertex(-h,-h,-h, vc(-h,-h,-h,h), 0,0)); // 0
        vb.add(new Vertex( h,-h,-h, vc( h,-h,-h,h), 1,0)); // 1
        vb.add(new Vertex( h, h,-h, vc( h, h,-h,h), 1,1)); // 2
        vb.add(new Vertex(-h, h,-h, vc(-h, h,-h,h), 0,1)); // 3
        // Front (z=+h)
        vb.add(new Vertex(-h,-h, h, vc(-h,-h, h,h), 0,0)); // 4
        vb.add(new Vertex( h,-h, h, vc( h,-h, h,h), 1,0)); // 5
        vb.add(new Vertex( h, h, h, vc( h, h, h,h), 1,1)); // 6
        vb.add(new Vertex(-h, h, h, vc(-h, h, h,h), 0,1)); // 7
        // Bottom (y=-h)
        vb.add(new Vertex(-h,-h,-h, vc(-h,-h,-h,h), 0,0)); // 8
        vb.add(new Vertex( h,-h,-h, vc( h,-h,-h,h), 1,0)); // 9
        vb.add(new Vertex( h,-h, h, vc( h,-h, h,h), 1,1)); // 10
        vb.add(new Vertex(-h,-h, h, vc(-h,-h, h,h), 0,1)); // 11
        // Top (y=+h)
        vb.add(new Vertex( h, h,-h, vc( h, h,-h,h), 0,0)); // 12
        vb.add(new Vertex(-h, h,-h, vc(-h, h,-h,h), 1,0)); // 13
        vb.add(new Vertex(-h, h, h, vc(-h, h, h,h), 1,1)); // 14
        vb.add(new Vertex( h, h, h, vc( h, h, h,h), 0,1)); // 15
        // Left (x=-h)
        vb.add(new Vertex(-h,-h,-h, vc(-h,-h,-h,h), 0,0)); // 16
        vb.add(new Vertex(-h,-h, h, vc(-h,-h, h,h), 1,0)); // 17
        vb.add(new Vertex(-h, h, h, vc(-h, h, h,h), 1,1)); // 18
        vb.add(new Vertex(-h, h,-h, vc(-h, h,-h,h), 0,1)); // 19
        // Right (x=+h)
        vb.add(new Vertex( h,-h,-h, vc( h,-h,-h,h), 0,0)); // 20
        vb.add(new Vertex( h, h,-h, vc( h, h,-h,h), 1,0)); // 21
        vb.add(new Vertex( h, h, h, vc( h, h, h,h), 1,1)); // 22
        vb.add(new Vertex( h,-h, h, vc( h,-h, h,h), 0,1)); // 23

        // LINES – hrany (12 hran) – používá prvních 8 vrcholů (back+front pokrývají 8 rohů)
        int edgeStart = 0;
        addEdge(0, 1); addEdge(1, 2); addEdge(2, 3); addEdge(3, 0); // zadní
        addEdge(4, 5); addEdge(5, 6); addEdge(6, 7); addEdge(7, 4); // přední
        addEdge(0, 4); addEdge(1, 5); addEdge(2, 6); addEdge(3, 7); // svislé
        pb.add(new Part(TopologyType.LINES, edgeStart, 12));

        // TRIANGLES – plochy (6 stěn × 2 trojúhelníky = 12 trojúhelníků)
        int triStart = ib.size();
        addTriangle( 0, 2, 1); addTriangle( 0, 3, 2); // back
        addTriangle( 4, 5, 6); addTriangle( 4, 6, 7); // front
        addTriangle( 8, 9,10); addTriangle( 8,10,11); // bottom
        addTriangle(12,13,14); addTriangle(12,14,15); // top
        addTriangle(16,17,18); addTriangle(16,18,19); // left
        addTriangle(20,21,22); addTriangle(20,22,23); // right
        pb.add(new Part(TopologyType.TRIANGLES, triStart, 12));

        shader = new ShaderConstant(c);
    }

    /** Mapuje signed [-h, h] souřadnici na [0, 255] RGB kanál. */
    private static Col vc(double x, double y, double z, double h) {
        return new Col(
            (int) ((x / h + 1) * 0.5 * 255),
            (int) ((y / h + 1) * 0.5 * 255),
            (int) ((z / h + 1) * 0.5 * 255)
        );
    }
}
