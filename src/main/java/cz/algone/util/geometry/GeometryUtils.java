package cz.algone.util.geometry;

import cz.algone.model.Point;

public class GeometryUtils {
    /** pro bod px, py zjistí vzdálenost od úsečky [A,B] */
    public static double distancePointToSegment(int px, int py, Point a, Point b) {
        double ax = a.x();
        double ay = a.y();
        double bx = b.x();
        double by = b.y();

        double vx = bx - ax;
        double vy = by - ay;
        double wx = px - ax;
        double wy = py - ay;

        double len2 = vx * vx + vy * vy;
        double t;

        if (len2 == 0) {
            // a a b jsou stejný bod
            t = 0;
        } else {
            t = (wx * vx + wy * vy) / len2;
            if (t < 0) t = 0;
            else if (t > 1) t = 1;
        }

        double projX = ax + t * vx;
        double projY = ay + t * vy;

        double dx = px - projX;
        double dy = py - projY;

        return Math.sqrt(dx * dx + dy * dy);
    }
}
