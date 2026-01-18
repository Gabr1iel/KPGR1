package cz.algone.model.models3D.solids;

import cz.algone.model.models3D.Solid;
import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;
import javafx.scene.paint.Color;

public class Cuboid extends Solid {
    public Cuboid() {
        // vrcholy
        vb.add(new Point3D(0,0,0)); // 0
        vb.add(new Point3D(1,0,0)); // 1
        vb.add(new Point3D(1,1,0)); // 2
        vb.add(new Point3D(0,1,0)); // 3

        vb.add(new Point3D(0,0,1)); // 4
        vb.add(new Point3D(1,0,1)); // 5
        vb.add(new Point3D(1,1,1)); // 6
        vb.add(new Point3D(0,1,1)); // 7

        // spodní
        edge(0,1); edge(1,2); edge(2,3); edge(3,0);
        // horní
        edge(4,5); edge(5,6); edge(6,7); edge(7,4);
        // svislé
        edge(0,4); edge(1,5); edge(2,6); edge(3,7);

        color = new ColorPair(Color.LIGHTBLUE, null);
    }

    private void edge(int a, int b) {
        ib.add(a);
        ib.add(b);
    }
}
