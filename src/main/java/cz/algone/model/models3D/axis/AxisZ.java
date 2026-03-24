package cz.algone.model.models3D.axis;

import cz.algone.model.models3D.wiredSolids.Solid;
import cz.algone.transforms.Col;
import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;

public class AxisZ extends Solid {
    public AxisZ() {
        vb.add(new Point3D(0, 0, 0));
        vb.add(new Point3D(0, 0, 1));

        ib.add(0);
        ib.add(1);

        color = new ColorPair(new Col(0, 0, 255), null);
    }
}
