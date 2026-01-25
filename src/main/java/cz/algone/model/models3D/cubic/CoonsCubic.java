package cz.algone.model.models3D.cubic;

import cz.algone.transforms.Vec3D;

public class CoonsCubic implements IParametricCubic {
    private final HermiteFergusonCubic hermite;

    public CoonsCubic() {
        Vec3D p0 = new Vec3D(-1, 0, 0.2);
        Vec3D p1 = new Vec3D(1, 0, 0.4);
        Vec3D t0 = new Vec3D(0.3, -1, 0.9).sub(p0).mul(0.5);
        Vec3D t1 = new Vec3D(1, 0, 0.4).sub(new Vec3D(-0.3, 1, 0.6)).mul(0.5);
        hermite = new HermiteFergusonCubic(p0, p1, t0, t1);
    }

    @Override
    public Vec3D evaluate(double t) {
        return hermite.evaluate(t);
    }
}
