package cz.algone.model.models3D.cubic;

import cz.algone.transforms.Vec3D;

/** Parametrická křivka definovaná parametrem t ∈ ⟨0, 1⟩. */
public interface IParametricCubic {
    Vec3D evaluate(double t);
}
