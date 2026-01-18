package cz.algone.model.models3D;

import cz.algone.model.models3D.solids.Cuboid;
import cz.algone.model.models3D.solids.Cylinder;
import cz.algone.model.models3D.solids.Tetrahedron;

import java.util.HashMap;
import java.util.Map;

public class SolidsCollection {
    public final Map<SolidAlias, Solid> solidsMap = new HashMap<>();
    private final Cuboid cuboid;
    private final Tetrahedron tetrahedron;
    private final Cylinder cylinder;

    public SolidsCollection() {
        this.cuboid = new Cuboid();
        this.tetrahedron = new Tetrahedron();
        this.cylinder = new Cylinder(12);

        setSolidsMap();
    }

    private void setSolidsMap() {
        solidsMap.put(SolidAlias.CUBOID, cuboid);
        solidsMap.put(SolidAlias.TETRAHEDRON, tetrahedron);
        solidsMap.put(SolidAlias.CYLINDER, cylinder);
    }
}
