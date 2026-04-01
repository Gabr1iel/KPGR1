package cz.algone.model.models3D.cubic;

import cz.algone.transforms.Vec3D;

/** Interface pro kubiky křivky */
public interface IParametricCubic {
    Vec3D evaluate(double t); //t = <0,1>
}
