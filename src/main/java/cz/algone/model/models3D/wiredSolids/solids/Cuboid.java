package cz.algone.model.models3D.wiredSolids.solids;

import cz.algone.model.models3D.wiredSolids.Solid;
import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;
import javafx.scene.paint.Color;

public class Cuboid extends Solid {
    public Cuboid() {
        double h = 0.5;

        // vrcholy
        vb.add(new Point3D(-h,-h,-h));
        vb.add(new Point3D( h,-h,-h));
        vb.add(new Point3D( h, h,-h));
        vb.add(new Point3D(-h, h,-h));

        vb.add(new Point3D(-h,-h, h));
        vb.add(new Point3D( h,-h, h));
        vb.add(new Point3D( h, h, h));
        vb.add(new Point3D(-h, h, h));

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
