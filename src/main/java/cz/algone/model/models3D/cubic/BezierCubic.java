package cz.algone.model.models3D.wiredSolids.cubic;

import cz.algone.transforms.Vec3D;

public class BezierCubic implements IParametricCubic {
    private final Vec3D p0, p1, p2, p3;

    public BezierCubic() {
        this.p0 = new Vec3D(-1, 0, 0.2);
        this.p1 = new Vec3D(-0.3, 1, 0.6);
        this.p2 = new Vec3D(0.3, -1, 0.9);
        this.p3 = new Vec3D(1, 0, 0.4);
    }

    @Override
    public Vec3D evaluate(double t) {
        double u = 1 - t;
        return p0.mul(u*u*u)
                .add(p1.mul(3*u*u*t))
                .add(p2.mul(3*u*t*t))
                .add(p3.mul(t*t*t));
    }
}
