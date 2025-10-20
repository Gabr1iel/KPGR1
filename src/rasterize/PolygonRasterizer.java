package rasterize;

import model.Line;
import model.Point;
import model.Polygon;

public class PolygonRasterizer {
    private LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {
        if (polygon.getSize() < 3) return;

        for (int indexA = 0; indexA < polygon.getSize(); indexA++) {
            int indexB = indexA + 1;

            if (indexB ==polygon.getSize())
                indexB = 0;

            Point a = polygon.getPointByIndex(indexA);
            Point b = polygon.getPointByIndex(indexB);

            lineRasterizer.rasterize(new Line(a, b));
        }
    }
}
