package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;

public class Cylinder extends Solid {
    public Cylinder(int segments) {
        double h = 1.0;
        pivot = new Vec3D(0, 0, 0.5);
        color = new ColorPair(new Col(173, 255, 47), null);

        Col c = color.primary();

        // ── Boční vrcholy: i*2=dole, i*2+1=nahoře (u=angle, v=height) ──
        // segments+1 párů – poslední duplikuje první s u=1.0 (UV šev)
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double x = Math.cos(angle);
            double y = Math.sin(angle);
            double u = (double) i / segments;
            Col vc = posColor(x, y, 0.0, 1.0);
            vb.add(new Vertex(x, y, 0, vc, u, 0.0, x, y, 0));  // i*2   – dole
            vb.add(new Vertex(x, y, h, vc, u, 1.0, x, y, 0));  // i*2+1 – nahoře
        }

        // ── Víčka: center + rim s kruhovým UV ──
        int bottomCenter = vb.size();
        vb.add(new Vertex(0, 0, 0, posColor(0, 0, 0, 1.0), 0.5, 0.5, 0, 0, -1));
        int bottomRim = vb.size();
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double x = Math.cos(angle), y = Math.sin(angle);
            vb.add(new Vertex(x, y, 0, posColor(x, y, 0, 1.0),
                    0.5 + 0.5 * Math.cos(angle), 0.5 + 0.5 * Math.sin(angle),
                    0, 0, -1));
        }

        int topCenter = vb.size();
        vb.add(new Vertex(0, 0, h, posColor(0, 0, h, 1.0), 0.5, 0.5, 0, 0, 1));
        int topRim = vb.size();
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double x = Math.cos(angle), y = Math.sin(angle);
            vb.add(new Vertex(x, y, h, posColor(x, y, h, 1.0),
                    0.5 + 0.5 * Math.cos(angle), 0.5 + 0.5 * Math.sin(angle),
                    0, 0, 1));
        }

        // LINES – 3*segments hran (boční vrcholy, seam uzavírá kruh)
        int edgeStart = 0;
        for (int i = 0; i < segments; i++) {
            addEdge(i * 2,     (i + 1) * 2);     // spodní kruh
            addEdge(i * 2 + 1, (i + 1) * 2 + 1); // horní kruh
            addEdge(i * 2,     i * 2 + 1);        // svislá hrana
        }
        pb.add(new Part(TopologyType.LINES, edgeStart, 3 * segments));

        // TRIANGLES
        int triStart = ib.size();

        // spodní víčko (fan od bottomCenter)
        for (int i = 0; i < segments; i++) {
            int next = (i + 1) % segments;
            addTriangle(bottomCenter, bottomRim + next, bottomRim + i);
        }

        // horní víčko (fan od topCenter)
        for (int i = 0; i < segments; i++) {
            int next = (i + 1) % segments;
            addTriangle(topCenter, topRim + i, topRim + next);
        }

        // boční plocha (seam vertex zajistí správný UV přechod)
        for (int i = 0; i < segments; i++) {
            int a = i * 2, b = (i + 1) * 2, c2 = i * 2 + 1, d = (i + 1) * 2 + 1;
            addTriangle(a, b, c2);
            addTriangle(b, d, c2);
        }
        pb.add(new Part(TopologyType.TRIANGLES, triStart, 4 * segments));

        shader = new ShaderConstant(c);
    }
}
