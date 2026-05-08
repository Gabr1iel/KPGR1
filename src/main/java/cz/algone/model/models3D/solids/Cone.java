package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;

/** Kužel se základnou v z=0 a vrcholem v z=height. */
public class Cone extends Solid {
    public Cone(int segments) {
        double h = 1.0;
        pivot = new Vec3D(0, 0, 0.5);
        color = new ColorPair(new Col(255, 99, 71), null);
        Col c = color.primary();

        double slopeLen = Math.sqrt(1.0 + h * h);
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double x = Math.cos(angle);
            double y = Math.sin(angle);
            double u = (double) i / segments;
            Col vc = posColor(x, y, 0.0, 1.0);
            double nnx = x * h / slopeLen, nny = y * h / slopeLen, nnz = 1.0 / slopeLen;
            vb.add(new Vertex(x, y, 0, vc, u, 0.0, nnx, nny, nnz));
        }
        int apexIdx = vb.size();
        vb.add(new Vertex(0, 0, h, new Col(255, 128, 0), 0.5, 1.0, 0, 0, 1));

        int capCenter = vb.size();
        vb.add(new Vertex(0, 0, 0, new Col(0, 0, 0), 0.5, 0.5, 0, 0, -1));
        int capRim = vb.size();
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double x = Math.cos(angle), y = Math.sin(angle);
            vb.add(new Vertex(x, y, 0, posColor(x, y, 0, 1.0),
                    0.5 + 0.5 * Math.cos(angle), 0.5 + 0.5 * Math.sin(angle),
                    0, 0, -1));
        }

        int edgeStart = 0, edgeCount = 0;
        for (int i = 0; i < segments; i++) {
            addEdge(i, i + 1);
            addEdge(i, apexIdx);
            edgeCount += 2;
        }
        pb.add(new Part(TopologyType.LINES, edgeStart, edgeCount));

        int triStart = ib.size(), triCount = 0;
        for (int i = 0; i < segments; i++) {
            addTriangle(capCenter, capRim + (i + 1) % segments, capRim + i);
            triCount++;
        }
        for (int i = 0; i < segments; i++) {
            addTriangle(i, i + 1, apexIdx);
            triCount++;
        }
        pb.add(new Part(TopologyType.TRIANGLES, triStart, triCount));

        shader = new ShaderConstant(c);
    }
}
