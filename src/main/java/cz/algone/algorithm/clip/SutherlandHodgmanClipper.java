package cz.algone.algorithm.clip;

import cz.algone.model.models2D.Point;
import cz.algone.raster.ImageBuffer;
import cz.algone.util.geometry.Geometry2D;
import java.util.ArrayList;
import java.util.List;

public class SutherlandHodgmanClipper implements IClipper {
    @Override
    public List<Point> clip(List<Point> subject, List<Point> clipper) {
        boolean ccw = Geometry2D.isCCW(clipper);
        List<Point> output = new ArrayList<>(subject);

        int clipperSize = clipper.size();
        for (int i = 0; i < clipperSize; i++) {
            Point A = clipper.get(i);
            Point B = clipper.get((i + 1) % clipperSize);

            List<Point> input = output;
            output = new ArrayList<>();
            if (input.isEmpty()) break;

            Point PreviousPoint = input.getLast();

            for (Point CurrentPoint : input) {
                boolean CurrentIn = inside(CurrentPoint, A, B, ccw);
                boolean PreviousIn = inside(PreviousPoint, A, B, ccw);

                if (CurrentIn) {
                    if (!PreviousIn) {
                        output.add(intersection(PreviousPoint, CurrentPoint, A, B));
                    }
                    output.add(CurrentPoint);
                } else if (PreviousIn) {
                    output.add(intersection(PreviousPoint, CurrentPoint, A, B));
                }

                PreviousPoint = CurrentPoint;
            }
        }
        return cleanup(output);
    }
    /** Vrací true pokud bod p leží uvnitř poloroviny dané hranou a→b a orientací ccw. */
    private boolean inside(Point p, Point a, Point b, boolean ccw) {
        long cr = Geometry2D.cross(a, b, p);
        return ccw ? cr >= 0 : cr <= 0;
    }

    private Point intersection(Point s, Point e, Point a, Point b) {
        return Geometry2D.intersectLines(s, e, a, b);
    }
    /** Odstraní z polygonu po sobě jdoucí duplicitní body a uzavírací duplikát. */
    private List<Point> cleanup(List<Point> pts) {
        if (pts.size() < 2) return pts;
        List<Point> out = new ArrayList<>();
        Point prev = null;
        for (Point p : pts) {
            if (prev == null || prev.x() != p.x() || prev.y() != p.y()) {
                out.add(p);
            }
            prev = p;
        }
        if (out.size() > 1) {
            Point first = out.getFirst();
            Point last = out.getLast();
            if (first.x() == last.x() && first.y() == last.y()) {
                out.removeLast();
            }
        }
        return out;
    }

    @Override
    public void setup(ImageBuffer imageBuffer) {}
}
