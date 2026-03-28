package cz.algone.model.models3D.wiredSolids;

import cz.algone.common.enumAlias.SolidAlias;
import cz.algone.model.models3D.wiredSolids.cubic.BezierCubic;
import cz.algone.model.models3D.wiredSolids.cubic.BezierSurface4x4;
import cz.algone.model.models3D.wiredSolids.solids.*;
import cz.algone.transforms.Col;
import cz.algone.transforms.Vec3D;

import java.util.HashMap;
import java.util.Map;

public class SolidsCollection {
    public final Map<SolidAlias, Solid> solidsMap = new HashMap<>();

    public SolidsCollection() {
        solidsMap.put(SolidAlias.CUBOID,        new Cuboid());
        solidsMap.put(SolidAlias.TETRAHEDRON,   new Tetrahedron());
        solidsMap.put(SolidAlias.CYLINDER,      new Cylinder(12));
        solidsMap.put(SolidAlias.SPHERE,        new Sphere(12, 16));
        solidsMap.put(SolidAlias.CONE,          new Cone(12));
        solidsMap.put(SolidAlias.TRUNCATED_CONE,new TruncatedCone(12, 1.0, 0.5, 1.0));
        solidsMap.put(SolidAlias.ICOSAHEDRON,   new Icosahedron());
        solidsMap.put(SolidAlias.D100,          new D100());

        Vec3D[][] cp = new Vec3D[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                cp[i][j] = new Vec3D(-1.5 + i, -1.5 + j, 0.3 * Math.sin(i * 1.1) * Math.cos(j * 1.1));

        solidsMap.put(SolidAlias.CURVE,   new CurveSolid(new BezierCubic()));
        solidsMap.put(SolidAlias.SURFACE, new SurfaceSolid(new BezierSurface4x4(cp)));
        solidsMap.put(SolidAlias.LIGHT,   new LightSolid());
    }
}
