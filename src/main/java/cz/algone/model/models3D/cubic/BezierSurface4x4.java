package cz.algone.model.models3D.wiredSolids.cubic;

import cz.algone.transforms.Vec3D;

public class BezierSurface4x4 implements IParametricSurface {
    private final Vec3D[][] p = new Vec3D[4][4];

    public BezierSurface4x4(Vec3D[][] control) {
        if (control.length != 4 || control[0].length != 4) {
            throw new IllegalArgumentException("Expected 4x4 control points.");
        }
        for (int i = 0; i < 4; i++) {
            if (control[i].length != 4) throw new IllegalArgumentException("Expected 4x4 control points.");
            System.arraycopy(control[i], 0, p[i], 0, 4);
        }
    }

    @Override
    public Vec3D evaluation(double u, double v) {
        double[] bu = bernstein3(u);
        double[] bv = bernstein3(v);

        Vec3D sum = new Vec3D(0, 0, 0);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sum = sum.add(p[i][j].mul(bu[i] * bv[j]));
            }
        }
        return sum;
    }

    private static double[] bernstein3(double t) {
        double u = 1 - t;
        // B0..B3 pro kubiku
        return new double[] {
                u*u*u,
                3*u*u*t,
                3*u*t*t,
                t*t*t
        };
    }
}
