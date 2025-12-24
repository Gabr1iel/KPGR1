package cz.algone.algorithm.clip;

import cz.algone.model.Point;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.geometry.Geometry2D;

import java.util.ArrayList;
import java.util.List;

public class SutherlandHodgmanClipper implements IClipper {
    @Override
    public List<Point> clip(List<Point> subject, List<Point> clipper) {
        if (subject == null || subject.size() < 3) return List.of();
        if (clipper == null || clipper.size() < 3) return List.of();

        // pro zadání: clipper konvexní, min 5 bodů → validace může být mimo
        boolean ccw = Geometry2D.isCCW(clipper);

        List<Point> output = new ArrayList<>(subject);

        int m = clipper.size();
        for (int i = 0; i < m; i++) {
            Point A = clipper.get(i);
            Point B = clipper.get((i + 1) % m);

            List<Point> input = output;
            output = new ArrayList<>();
            if (input.isEmpty()) break;

            Point S = input.get(input.size() - 1);

            for (Point E : input) {
                boolean Ein = inside(E, A, B, ccw);
                boolean Sin = inside(S, A, B, ccw);

                if (Ein) {
                    if (!Sin) {
                        // vstup zvenku dovnitř → přidej průsečík
                        output.add(intersection(S, E, A, B));
                    }
                    // přidej koncový bod
                    output.add(E);
                } else if (Sin) {
                    // odchod ven → přidej průsečík
                    output.add(intersection(S, E, A, B));
                }

                S = E;
            }
        }

        // volitelně: odstranit duplicitní body vedle sebe
        return cleanup(output);
    }

    private boolean inside(Point p, Point a, Point b, boolean ccw) {
        long cr = Geometry2D.cross(a, b, p);
        return ccw ? cr >= 0 : cr <= 0;
    }

    private Point intersection(Point s, Point e, Point a, Point b) {
        return Geometry2D.intersectLineWithSegment(s, e, a, b);
    }

    private List<Point> cleanup(List<Point> pts) {
        if (pts.size() < 2) return pts;
        List<Point> out = new ArrayList<>();
        Point prev = null;
        for (Point p : pts) {
            if (prev == null || prev.getX() != p.getX() || prev.getY() != p.getY()) {
                out.add(p);
            }
            prev = p;
        }
        // pokud první==poslední, poslední pryč (aby to sedělo na tvůj model)
        if (out.size() > 1) {
            Point first = out.get(0);
            Point last = out.get(out.size() - 1);
            if (first.getX() == last.getX() && first.getY() == last.getY()) {
                out.remove(out.size() - 1);
            }
        }
        return out;
    }

    //Ignorovaná metoda
    @Override
    public void setup(RasterCanvas raster) {}
}
