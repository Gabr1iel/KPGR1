package cz.algone.model.models3D.wiredSolids.cubic;

import cz.algone.transforms.Vec3D;

public class HermiteFergusonCubic implements IParametricCubic {
    private final Vec3D p0, p1, t0, t1;

    public HermiteFergusonCubic(Vec3D p0, Vec3D p1, Vec3D t0, Vec3D t1) {
        this.p0 = p0; this.p1 = p1; this.t0 = t0; this.t1 = t1;
    }

    public HermiteFergusonCubic() {
        this(new Vec3D(-1, 0, 0.2), new Vec3D(-0.3, 1, 0.6), new Vec3D(0.3, -1, 0.9), new Vec3D(1, 0, 0.4));
    }

    @Override
    public Vec3D evaluate(double t) {
        double t2 = t * t;
        double t3 = t2 * t;

        double h00 = 2*t3 - 3*t2 + 1;
        double h10 = t3 - 2*t2 + t;
        double h01 = -2*t3 + 3*t2;
        double h11 = t3 - t2;

        return p0.mul(h00)
                .add(t0.mul(h10))
                .add(p1.mul(h01))
                .add(t1.mul(h11));
    }
}
