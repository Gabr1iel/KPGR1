package cz.algone.model.models3D.axis;

import cz.algone.model.models3D.wiredSolids.Solid;
import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;
import javafx.scene.paint.Color;

public class AxisX extends Solid {
    public AxisX() {
        vb.add(new Point3D(0, 0, 0));
        vb.add(new Point3D(1, 0, 0));

        ib.add(0);
        ib.add(1);

        color = new ColorPair(Color.valueOf("0xff0000"), null);
    }
}
