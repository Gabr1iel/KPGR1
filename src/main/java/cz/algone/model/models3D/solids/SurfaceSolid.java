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

public class SurfaceSolid extends Solid {
    private IParametricSurface surface;
    private int uSteps = 25;
    private int vSteps = 25;

    public SurfaceSolid(IParametricSurface surface) {
        this.surface = surface;
        color = new ColorPair(new Col(128, 0, 128), null);
        shader = new ShaderConstant(color.primary());
        rebuild();
    }

    public void rebuild() {
        vb.clear();
        ib.clear();
        pb.clear();

        int cols = uSteps + 1;
        int rows = vSteps + 1;
        Col c = color.primary();

        for (int j = 0; j < rows; j++) {
            double v = j / (double) vSteps;
            for (int i = 0; i < cols; i++) {
                double u = i / (double) uSteps;
                Vec3D p = surface.evaluation(u, v);
                vb.add(new Vertex(p.getX(), p.getY(), p.getZ(), c));
            }
        }

        int edgeStart = 0;
        int edgeCount = 0;

        for (int j = 0; j < rows; j++) {
            int rowStart = j * cols;
            for (int i = 0; i < cols - 1; i++) {
                addEdge(rowStart + i, rowStart + i + 1);
                edgeCount++;
            }
        }
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows - 1; j++) {
                addEdge(j * cols + i, (j + 1) * cols + i);
                edgeCount++;
            }
        }
        pb.add(new Part(TopologyType.LINES, edgeStart, edgeCount));
    }
}
