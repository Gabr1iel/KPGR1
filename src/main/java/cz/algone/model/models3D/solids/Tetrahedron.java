package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;
import javafx.scene.paint.Color;

public class Tetrahedron extends Solid {
    public Tetrahedron() {
        vb.add(new Point3D(0, 0, 0));      // 0
        vb.add(new Point3D(1, 0, 0));      // 1
        vb.add(new Point3D(0.5, 0.866, 0));// 2
        vb.add(new Point3D(0.5, 0.288, 0.816)); // 3 (nahoru)

        // hrany
        addEdge(0,1);
        addEdge(1,2);
        addEdge(2,0);

        addEdge(0,3);
        addEdge(1,3);
        addEdge(2,3);

        color = new ColorPair(Color.ORANGE, null);
    }

    private void addEdge(int a, int b) {
        ib.add(a);
        ib.add(b);
    }
}
