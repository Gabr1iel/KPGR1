package cz.algone.model.models3D.wiredSolids.solids;

import cz.algone.model.models3D.wiredSolids.Solid;
import cz.algone.transforms.Point3D;
import cz.algone.transforms.Vec3D;
import cz.algone.transforms.Col;
import cz.algone.util.color.ColorPair;

public class Cylinder extends Solid {
    public Cylinder(int segments) {
        double h = 1.0;
        pivot = new Vec3D(0, 0, 0.5);

        // spodní a horní kruh
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double x = Math.cos(angle);
            double y = Math.sin(angle);

            vb.add(new Point3D(x, y, 0));   // dole
            vb.add(new Point3D(x, y, h));   // nahoře
        }

        // hrany
        for (int i = 0; i < segments; i++) {
            int next = (i + 1) % segments;

            int a = i * 2;
            int b = next * 2;
            int c = a + 1;
            int d = b + 1;

            // spodní kruh
            edge(a, b);
            // horní kruh
            edge(c, d);
            // svislá
            edge(a, c);
        }

        color = new ColorPair(new Col(173, 255, 47), null);
    }

    private void edge(int a, int b) {
        ib.add(a);
        ib.add(b);
    }
}
