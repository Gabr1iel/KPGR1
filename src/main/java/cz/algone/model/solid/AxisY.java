package cz.algone.model.solid;

import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;
import javafx.scene.paint.Color;

public class AxisY extends Solid{
    public AxisY() {
        vb.add(new Point3D(0, 0, 0));
        vb.add(new Point3D(0, 1, 0));

        ib.add(0);
        ib.add(1);

        color = new ColorPair(Color.valueOf("0x00ff00"), null);
    }
}
