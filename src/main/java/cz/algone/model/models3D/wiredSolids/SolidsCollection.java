package cz.algone.model.models3D.wiredSolids;

import cz.algone.common.enumAlias.SolidAlias;
import cz.algone.model.models3D.wiredSolids.cubic.BezierCubic;
import cz.algone.model.models3D.wiredSolids.cubic.BezierSurface4x4;
import cz.algone.model.models3D.wiredSolids.solids.*;
import cz.algone.transforms.Vec3D;

import java.util.HashMap;
import java.util.Map;

public class SolidsCollection {
    public final Map<SolidAlias, Solid> solidsMap = new HashMap<>();
    private final Cuboid cuboid;
    private final Tetrahedron tetrahedron;
    private final Cylinder cylinder;
    private final CurveSolid curve;
    private final SurfaceSolid surface;

    public SolidsCollection() {
        this.cuboid = new Cuboid();
        this.tetrahedron = new Tetrahedron();
        this.cylinder = new Cylinder(12);

        this.curve = new CurveSolid(new BezierCubic());
        Vec3D[][] cp = new Vec3D[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double x = -1.5 + i * 1.0;
                double y = -1.5 + j * 1.0;
                double z = 0.3 * Math.sin(i * 1.1) * Math.cos(j * 1.1);
                cp[i][j] = new Vec3D(x, y, z);
            }
        }
        this.surface = new SurfaceSolid(new BezierSurface4x4(cp));

        setSolidsMap();
    }

    private void setSolidsMap() {
        solidsMap.put(SolidAlias.CUBOID, cuboid);
        solidsMap.put(SolidAlias.TETRAHEDRON, tetrahedron);
        solidsMap.put(SolidAlias.CYLINDER, cylinder);
        solidsMap.put(SolidAlias.CURVE, curve);
        solidsMap.put(SolidAlias.SURFACE, surface);
    }
}
