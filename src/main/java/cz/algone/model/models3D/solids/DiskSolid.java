package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderInterpolated;
import cz.algone.transforms.Col;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;

/**
 * Plochý disk (kruh) v rovině XZ demonstrující topologii TRIANGLE_FAN.
 * Vrchol 0 = střed, vrcholy 1..slices = okraj.
 * IB pro FAN: [0, 1, 2, ..., slices] → trojúhelníky (0,i, i+1 mod slices).
 */
public class DiskSolid extends Solid {

    public DiskSolid() {
        this(16, 0.7);
    }

    public DiskSolid(int slices, double radius) {
        Col centerColor = new Col(255, 255, 255);
        color = new ColorPair(centerColor, null);
        pivot = new Vec3D(0, 0, 0);

        // ─── Vertices ────────────────────────────────────────────────────────────
        // Index 0 – střed disku
        vb.add(new Vertex(0, 0, 0, centerColor, 0.5, 0.5));

        // Indexy 1..slices – okraj disku
        for (int i = 0; i < slices; i++) {
            double theta = 2 * Math.PI * i / slices;
            double x = radius * Math.cos(theta);
            double z = radius * Math.sin(theta);
            double u = (Math.cos(theta) + 1) / 2.0;
            double v = (Math.sin(theta) + 1) / 2.0;
            Col rimColor = posColor(x, 0, z, radius);
            vb.add(new Vertex(x, 0, z, rimColor, u, v));
        }

        // ─── LINES – okraj (drátový model) ───────────────────────────────────────
        int linesStart = 0;
        for (int i = 0; i < slices; i++) {
            addEdge(1 + i, 1 + (i + 1) % slices);
        }
        pb.add(new Part(TopologyType.LINES, linesStart, slices));

        // ─── TRIANGLE_FAN – vyplněný disk ────────────────────────────────────────
        int fanStart = ib.size();
        ib.add(0); // střed
        for (int i = 0; i < slices; i++) {
            ib.add(1 + i);
        }
        pb.add(new Part(TopologyType.TRIANGLE_FAN, fanStart, slices));

        shader = new ShaderInterpolated();
    }
}
