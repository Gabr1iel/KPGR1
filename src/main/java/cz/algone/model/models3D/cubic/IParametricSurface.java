package cz.algone.model.models3D.wiredSolids.cubic;

import cz.algone.transforms.Vec3D;

/** Interface pro kubiku plochy */
public interface IParametricSurface {
    Vec3D evaluation(double u, double v);
}
