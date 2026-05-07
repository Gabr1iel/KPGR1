package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;

/** Komolý kužel se spodní kružnicí v z=0 a horní v z=height. */
public class TruncatedCone extends Solid {
    public TruncatedCone(int segments, double bottomRadius, double topRadius, double height) {
        pivot = new Vec3D(0, 0, height / 2.0);
        color = new ColorPair(new Col(255, 215, 0), null);
        Col c = color.primary();

        int S1 = segments + 1;
        double dr = bottomRadius - topRadius;
        double slopeLen = Math.sqrt(dr * dr + height * height);
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double cx = Math.cos(angle), cy = Math.sin(angle);
            double x = bottomRadius * cx;
            double y = bottomRadius * cy;
            double u = (double) i / segments;
            double nnx = cx * height / slopeLen, nny = cy * height / slopeLen, nnz = dr / slopeLen;
            vb.add(new Vertex(x, y, 0, posColor(x, y, 0, bottomRadius), u, 0.0, nnx, nny, nnz));
        }
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double cx = Math.cos(angle), cy = Math.sin(angle);
            double x = topRadius * cx;
            double y = topRadius * cy;
            double u = (double) i / segments;
            double nnx = cx * height / slopeLen, nny = cy * height / slopeLen, nnz = dr / slopeLen;
            vb.add(new Vertex(x, y, height, posColor(x, y, height, topRadius), u, 1.0, nnx, nny, nnz));
        }

        int bottomCenter = vb.size();
        vb.add(new Vertex(0, 0, 0, new Col(0, 0, 0), 0.5, 0.5, 0, 0, -1));
        int bottomRim = vb.size();
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double x = bottomRadius * Math.cos(angle), y = bottomRadius * Math.sin(angle);
            vb.add(new Vertex(x, y, 0, posColor(x, y, 0, bottomRadius),
                    0.5 + 0.5 * Math.cos(angle), 0.5 + 0.5 * Math.sin(angle),
                    0, 0, -1));
        }

        int topCenter = vb.size();
        vb.add(new Vertex(0, 0, height, new Col(200, 200, 0), 0.5, 0.5, 0, 0, 1));
        int topRim = vb.size();
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double x = topRadius * Math.cos(angle), y = topRadius * Math.sin(angle);
            vb.add(new Vertex(x, y, height, posColor(x, y, height, topRadius),
                    0.5 + 0.5 * Math.cos(angle), 0.5 + 0.5 * Math.sin(angle),
                    0, 0, 1));
        }

        int edgeStart = 0, edgeCount = 0;
        for (int i = 0; i < segments; i++) {
            addEdge(i, i + 1);
            addEdge(S1 + i, S1 + i + 1);
            addEdge(i, S1 + i);
            edgeCount += 3;
        }
        pb.add(new Part(TopologyType.LINES, edgeStart, edgeCount));

        int triStart = ib.size(), triCount = 0;
        for (int i = 0; i < segments; i++) {
            int next = (i + 1) % segments;
            addTriangle(bottomCenter, bottomRim + next, bottomRim + i);
            addTriangle(topCenter, topRim + i, topRim + next);
            addTriangle(i, i + 1, S1 + i);
            addTriangle(i + 1, S1 + i + 1, S1 + i);
            triCount += 4;
        }
        pb.add(new Part(TopologyType.TRIANGLES, triStart, triCount));

        shader = new ShaderConstant(c);
    }
}
