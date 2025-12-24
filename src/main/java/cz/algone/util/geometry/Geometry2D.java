package cz.algone.util.geometry;

import cz.algone.model.Point;

public final class Geometry2D {
    private Geometry2D() {}

    // signed area (v první implementaci se nedělil výsledek 2!)
    public static double signedArea(java.util.List<Point> pts) {
        if (pts == null || pts.size() < 3) return 0;
        long sum = 0;
        for (int i = 0; i < pts.size(); i++) {
            Point a = pts.get(i);
            Point b = pts.get((i + 1) % pts.size());
            sum += (long) a.getX() * b.getY() - (long) b.getX() * a.getY();
        }
        return sum / 2.0;
    }

    public static boolean isCCW(java.util.List<Point> pts) {
        return signedArea(pts) > 0;
    }

    // cross((b-a),(p-a))
    public static long cross(Point a, Point b, Point p) {
        long abx = (long) b.getX() - a.getX();
        long aby = (long) b.getY() - a.getY();
        long apx = (long) p.getX() - a.getX();
        long apy = (long) p.getY() - a.getY();
        return abx * apy - aby * apx;
    }

    public static boolean isConvex(java.util.List<Point> pts) {
        if (pts == null || pts.size() < 4) return false; // pro zadání chceš aspoň 5, ale convex check obecně
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


    public static Point intersectLineWithSegment(Point s, Point e, Point a, Point b) {
        // průsečík přímek (s->e) a (a->b)
        // Použijeme parametrickou formu + determinant
        double x1 = s.getX(), y1 = s.getY();
        double x2 = e.getX(), y2 = e.getY();
        double x3 = a.getX(), y3 = a.getY();
        double x4 = b.getX(), y4 = b.getY();

        double den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (Math.abs(den) < 1e-9) {
            // rovnoběžné/kolineární → vrať třeba end (aby to nespadlo), nebo null
            return new Point((int) Math.round(x2), (int) Math.round(y2));
        }

        double px = ((x1*y2 - y1*x2) * (x3 - x4) - (x1 - x2) * (x3*y4 - y3*x4)) / den;
        double py = ((x1*y2 - y1*x2) * (y3 - y4) - (y1 - y2) * (x3*y4 - y3*x4)) / den;

        return new Point((int) Math.round(px), (int) Math.round(py));
    }
}
