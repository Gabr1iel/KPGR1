package cz.algone.model.models3D.axis;

import cz.algone.model.models3D.wiredSolids.Solid;
import cz.algone.transforms.Col;
import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;

public class AxisY extends Solid {
    public AxisY() {
        vb.add(new Point3D(0, 0, 0));
        vb.add(new Point3D(0, 1, 0));

        ib.add(0);
        ib.add(1);

        color = new ColorPair(new Col(0, 255, 0), null);
    }
}
