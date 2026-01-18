package cz.algone.model.models3D.axis;

import cz.algone.model.models3D.Solid;
import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;
import javafx.scene.paint.Color;

public class AxisZ extends Solid {
    public AxisZ() {
        vb.add(new Point3D(0, 0, 0));
        vb.add(new Point3D(0, 0, 1));

        ib.add(0);
        ib.add(1);

        color = new ColorPair(Color.valueOf("0x0000ff"), null);
    }
}
