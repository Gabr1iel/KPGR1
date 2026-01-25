package cz.algone.model.models3D.cubic;

import cz.algone.transforms.Vec3D;

public interface IParametricSurface {
    Vec3D evaluation(double u, double v);
}
