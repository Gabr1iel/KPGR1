package cz.algone.algorithm.clip3D;

import cz.algone.objectData.Vertex;
import cz.algone.transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Ořezání primitiv v homogenním clip space (po násobení proj. maticí, před dehomogenizací).
 *
 * Frustum clip podmínky pro bod (x,y,z,w):
 *   -w ≤ x ≤ w   (LEFT / RIGHT)
 *   -w ≤ y ≤ w   (BOTTOM / TOP)
 *    0 ≤ z ≤ w   (NEAR / FAR)
 *
 * Znaménkové vzdálenosti d > 0 = uvnitř, d ≤ 0 = vně.
 *
 * Pracuje s {@link Vertex} – při ořezání interpoluje všechny atributy
 * (barva, UV, w) společně s pozicí v homogenním prostoru.
 */
public class Clipper3D {

    private static final int PLANES = 6;

    private static double signedDist(Vertex v, int plane) {
        Point3D p = v.getPosition();
        double x = p.getX(), y = p.getY(), z = p.getZ(), w = p.getW();
        return switch (plane) {
            case 0 ->  x + w;  // LEFT:   x >= -w
            case 1 -> -x + w;  // RIGHT:  x <=  w
            case 2 ->  y + w;  // BOTTOM: y >= -w
            case 3 -> -y + w;  // TOP:    y <=  w
            case 4 ->  z;      // NEAR:   z >=  0
            case 5 ->  w - z;  // FAR:    z <=  w
            default -> throw new IllegalArgumentException();
        };
    }

    private static Vertex lerp(Vertex a, Vertex b, double t) {
        return a.mul(1 - t).add(b.mul(t));
    }

    /**
     * Ořeže úsečku p1–p2 v clip space (Liang-Barsky).
     * Vrací {p1', p2'} nebo null pokud je celá mimo frustum.
     */
    public static Vertex[] clipLine(Vertex p1, Vertex p2) {
        double tMin = 0, tMax = 1;

        for (int plane = 0; plane < PLANES; plane++) {
            double d1 = signedDist(p1, plane);
            double d2 = signedDist(p2, plane);

            if (d1 < 0 && d2 < 0) return null; // celá mimo tuto rovinu

            if (d1 < 0 || d2 < 0) {
                double t = d1 / (d1 - d2);
                if (d1 < 0) tMin = Math.max(tMin, t); // p1 venku → vstup
                else        tMax = Math.min(tMax, t); // p2 venku → výstup
            }
        }

        if (tMin > tMax) return null;
        return new Vertex[]{lerp(p1, p2, tMin), lerp(p1, p2, tMax)};
    }

    /**
     * Ořeže trojúhelník a–b–c v clip space (Sutherland-Hodgman, 6 rovin).
     * Vrací seznam trojúhelníků jako fan od prvního vrcholu polygonu.
     * Prázdný seznam = trojúhelník je celý mimo frustum.
     */
    public static List<Vertex[]> clipTriangle(Vertex a, Vertex b, Vertex c) {
        List<Vertex> poly = new ArrayList<>(List.of(a, b, c));

        for (int plane = 0; plane < PLANES; plane++) {
            if (poly.isEmpty()) return List.of();
            List<Vertex> next = new ArrayList<>();

            for (int i = 0; i < poly.size(); i++) {
                Vertex curr = poly.get(i);
                Vertex prev = poly.get((i - 1 + poly.size()) % poly.size());
                double dCurr = signedDist(curr, plane);
                double dPrev = signedDist(prev, plane);

                if (dCurr >= 0) {                 // curr uvnitř
                    if (dPrev < 0)                // prev venku → přidej průsečík
                        next.add(lerp(prev, curr, dPrev / (dPrev - dCurr)));
                    next.add(curr);
                } else if (dPrev >= 0) {          // curr venku, prev uvnitř → přidej průsečík
                    next.add(lerp(prev, curr, dPrev / (dPrev - dCurr)));
                }
            }
            poly = next;
        }

        if (poly.size() < 3) return List.of();

        // Triangulace fanouškem od prvního vrcholu
        List<Vertex[]> triangles = new ArrayList<>();
        for (int i = 1; i < poly.size() - 1; i++)
            triangles.add(new Vertex[]{poly.get(0), poly.get(i), poly.get(i + 1)});
        return triangles;
    }
}
