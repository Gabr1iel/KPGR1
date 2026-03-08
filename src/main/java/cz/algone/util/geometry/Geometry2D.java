package cz.algone.util.geometry;

import cz.algone.model.models2D.Point;
import java.util.List;

public final class Geometry2D {
    private Geometry2D() {}
    /** vezme dva {@link Point} vedle sebe a odečte souřadnice prvního od druhého ->
     * > 0 body jsou CCW, < 0 body jsou CW, = 0 degenerace (nulová plocha) */
    public static double signedArea(List<Point> pts) {
        if (pts == null || pts.size() < 3) return 0;
        long sum = 0;
        for (int i = 0; i < pts.size(); i++) {
            Point a = pts.get(i);
            Point b = pts.get((i + 1) % pts.size());
            sum += (long) a.x() * b.y() - (long) b.x() * a.y();
        }
        return sum / 2.0;
    }
    /** použije metodu {@link Geometry2D#signedArea} pro zjištění orientace,
     *  vrací CCW -> true, CW -> false */
    public static boolean isCCW(List<Point> pts) {
        return signedArea(pts) > 0;
    }
    /** vezme úsečku [A,B] a zjistí kde se od ni nachází bod p,
     * > 0 p je vlevo, < 0 p je vpravo, = 0 p je kolineární */
    public static long cross(Point a, Point b, Point p) {
        long abx = (long) b.x() - a.x();
        long aby = (long) b.y() - a.y();
        long apx = (long) p.x() - a.x();
        long apy = (long) p.y() - a.y();
        return abx * apy - aby * apx;
    }
    /** pro tři po sobě jdoucí body spočítá {@link Geometry2D#cross},
     * pokud znaménko zůstane stejné -> je konvexní */
    public static boolean isConvex(List<Point> pts) {
        if (pts == null || pts.size() < 4) return false;
        int n = pts.size();

        int sign = 0;
        for (int i = 0; i < n; i++) {
            Point a = pts.get(i);
            Point b = pts.get((i + 1) % n);
            Point c = pts.get((i + 2) % n);
            long cr = cross(a, b, c);
            if (cr == 0) continue;

            int s = cr > 0 ? 1 : -1;
            if (sign == 0) sign = s;
            else if (sign != s) return false;
        }
        return true;
    }

    /** použitím parametrické formy a determinantu zjišťuje průsečík přímek
     * [S,E] a [A,B] */
    public static Point intersectLines(Point s, Point e, Point a, Point b) {
        // průsečík přímek (s->e) a (a->b)
        // Použije parametrickou formu + determinant
        double x1 = s.x(), y1 = s.y();
        double x2 = e.x(), y2 = e.y();
        double x3 = a.x(), y3 = a.y();
        double x4 = b.x(), y4 = b.y();

        double den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (Math.abs(den) < 1e-9) {
            // rovnoběžné/kolineární → vrátí end (aby to nespadlo)
            return new Point((int) Math.round(x2), (int) Math.round(y2));
        }

        double px = ((x1*y2 - y1*x2) * (x3 - x4) - (x1 - x2) * (x3*y4 - y3*x4)) / den;
        double py = ((x1*y2 - y1*x2) * (y3 - y4) - (y1 - y2) * (x3*y4 - y3*x4)) / den;

        return new Point((int) Math.round(px), (int) Math.round(py));
    }
}
