package cz.algone.algorithm.clip;

import cz.algone.model.Point;
import cz.algone.raster.RasterCanvas;
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
                        //Současný bod uvnitř ale předešlý ne ->
                        // vstup dovnitř (přidá se průsečík)
                        output.add(intersection(PreviousPoint, CurrentPoint, A, B));
                    }
                    // přidá koncový bod
                    output.add(CurrentPoint);
                } else if (PreviousIn) {
                    //Předešlý bod uvnitř ale současný ne -> výstup (přidá se pouze průsečík)
                    output.add(intersection(PreviousPoint, CurrentPoint, A, B));
                }

                PreviousPoint = CurrentPoint;
            }
        }
        return cleanup(output);
    }
    /** Použije metodu {@link Geometry2D#cross} pro zjištění polohy bodu vůči hraně,
     * díky parametru ccw je možný zajistit správnost pro obě orientace*/
    private boolean inside(Point p, Point a, Point b, boolean ccw) {
        long cr = Geometry2D.cross(a, b, p);
        return ccw ? cr >= 0 : cr <= 0;
    }

    private Point intersection(Point s, Point e, Point a, Point b) {
        return Geometry2D.intersectLines(s, e, a, b);
    }
    /** Zbavuje clipped polygon duplicitních bodů
     *  získaných ořezáváním pomocí Sutherland algoritmu */
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
        // pokud první==poslední, poslední odstraní
        if (out.size() > 1) {
            Point first = out.getFirst();
            Point last = out.getLast();
            if (first.getX() == last.getX() && first.getY() == last.getY()) {
                out.removeLast();
            }
        }
        return out;
    }

    //Ignorovaná metoda
    @Override
    public void setup(RasterCanvas raster) {}
}
