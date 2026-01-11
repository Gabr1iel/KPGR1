package cz.algone.model.solid;

import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;
import javafx.scene.paint.Color;

public class Arrow extends Solid{

    public Arrow() {
        // Naplnit VB
        vb.add(new Point3D(0, 0, 0)); // 0
        vb.add(new Point3D(0.8, 0, 0)); // 1
        vb.add(new Point3D(0.8, -0.2, 0)); // 2
        vb.add(new Point3D(1, 0, 0)); // 3
        vb.add(new Point3D(0.8, 0.2, 0)); // 4

        // Naplnit IB
        ib.add(0);
        ib.add(1);
        ib.add(4);
        ib.add(2);
        ib.add(2);
        ib.add(3);
        ib.add(4);
        ib.add(3);

        color = new ColorPair(Color.valueOf("0xffff00"), null);
    }
}
