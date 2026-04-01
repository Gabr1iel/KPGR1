package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.model.models3D.cubic.IParametricSurface;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;

import java.util.Optional;

/**
 * Osvětlené těleso z kubické plochy – generuje TRIANGLES s per-vertex normálami.
 * Normály jsou počítány numericky jako cross(dU, dV).
 */
public class LitSurfaceSolid extends Solid {

    private static final double EPS = 1e-4;

    public LitSurfaceSolid(IParametricSurface surface) {
        this(surface, 20, 20);
    }

    public LitSurfaceSolid(IParametricSurface surface, int uSteps, int vSteps) {
        Col c = new Col(80, 160, 220);
        color = new ColorPair(c, null);
        pivot = new Vec3D(0, 0, 0);

        int cols = uSteps + 1;
        int rows = vSteps + 1;

        // ─── Vertices s normálami a UV ────────────────────────────────────────────
        for (int j = 0; j < rows; j++) {
            double v = j / (double) vSteps;
            for (int i = 0; i < cols; i++) {
                double u = i / (double) uSteps;
                Vec3D p = surface.evaluation(u, v);
                Vec3D n = computeNormal(surface, u, v);
                double texU = u;
                double texV = v;
                vb.add(new Vertex(p.getX(), p.getY(), p.getZ(), c, texU, texV,
                        n.getX(), n.getY(), n.getZ()));
            }
        }

        // ─── LINES (drátový model) ────────────────────────────────────────────────
        int linesStart = 0, linesCount = 0;
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols - 1; i++) {
                addEdge(j * cols + i, j * cols + i + 1);
                linesCount++;
            }
        }
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows - 1; j++) {
                addEdge(j * cols + i, (j + 1) * cols + i);
                linesCount++;
            }
        }
        pb.add(new Part(TopologyType.LINES, linesStart, linesCount));

        // ─── TRIANGLES (dvě na každý quad) ───────────────────────────────────────
        int triStart = ib.size(), triCount = 0;
        for (int j = 0; j < vSteps; j++) {
            for (int i = 0; i < uSteps; i++) {
                int a = j * cols + i;
                int b = j * cols + i + 1;
                int d = (j + 1) * cols + i;
                int e = (j + 1) * cols + i + 1;
                addTriangle(a, b, d);
                addTriangle(b, e, d);
                triCount += 2;
            }
        }
        pb.add(new Part(TopologyType.TRIANGLES, triStart, triCount));

        shader = new ShaderConstant(c);
    }

    private Vec3D computeNormal(IParametricSurface surface, double u, double v) {
        double u0 = Math.max(0, u - EPS), u1 = Math.min(1, u + EPS);
        double v0 = Math.max(0, v - EPS), v1 = Math.min(1, v + EPS);
        Vec3D dU = surface.evaluation(u1, v).sub(surface.evaluation(u0, v));
        Vec3D dV = surface.evaluation(u, v1).sub(surface.evaluation(u, v0));
        Vec3D cross = dU.cross(dV);
        Optional<Vec3D> n = cross.normalized();
        return n.orElse(new Vec3D(0, 1, 0));
    }
}
