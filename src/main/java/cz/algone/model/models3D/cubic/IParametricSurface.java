package cz.algone.model.models3D.cubic;

import cz.algone.transforms.Vec3D;

/** Parametrická plocha definovaná dvěma parametry u, v ∈ ⟨0, 1⟩. */
public interface IParametricSurface {
    Vec3D evaluation(double u, double v);
}
