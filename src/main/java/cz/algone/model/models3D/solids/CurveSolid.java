package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.model.models3D.cubic.IParametricCubic;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.shader.ShaderConstant;
import cz.algone.transforms.Col;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;

public class CurveSolid extends Solid {
    private IParametricCubic curve;
    private int steps = 40;

    public CurveSolid(IParametricCubic curve) {
        this.curve = curve;
        color = new ColorPair(new Col(0, 128, 0), null);
        shader = new ShaderConstant(color.primary());
        rebuild();
    }

    public void setCurve(IParametricCubic curve) {
        this.curve = curve;
        rebuild();
    }

    public void setSteps(int steps) {
        this.steps = steps;
        rebuild();
    }

    public int getSteps() { return steps; }

    public void rebuild() {
        vb.clear();
        ib.clear();
        pb.clear();

        Col c = color.primary();
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            Vec3D p = curve.evaluate(t);
            vb.add(new Vertex(p.getX(), p.getY(), p.getZ(), c));
        }

        int edgeStart = 0;
        for (int i = 0; i < steps; i++) {
            addEdge(i, i + 1);
        }
        pb.add(new Part(TopologyType.LINES, edgeStart, steps));
    }
}
