package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.model.models3D.cubic.IParametricCubic;
import cz.algone.transforms.Point3D;
import cz.algone.transforms.Vec3D;

public class CurveSolid extends Solid {
    private IParametricCubic curve;
    private int steps = 40;

    public CurveSolid(IParametricCubic curve) {
        this.curve = curve;
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

    public int getSteps() {return steps;}

    public void rebuild() {
        getVb().clear();
        getIb().clear();

        //body
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            Vec3D p = curve.evaluate(t);
            getVb().add(new Point3D(p.getX(), p.getY(), p.getZ(), 1.0));
        }

        //úsečky
        for (int i = 0; i < steps; i++) {
            getIb().add(i);
            getIb().add(i + 1);
        }
    }
}
