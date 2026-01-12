package cz.algone.model.solid;

import cz.algone.transforms.Point3D;
import cz.algone.util.color.ColorPair;
import javafx.scene.paint.Color;

public class Cylinder extends Solid {
    public Cylinder(int segments) {
        double h = 1.0;

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

        color = new ColorPair(Color.GREENYELLOW, null);
    }

    private void edge(int a, int b) {
        ib.add(a);
        ib.add(b);
    }
}
