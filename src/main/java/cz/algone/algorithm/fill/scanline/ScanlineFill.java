package cz.algone.algorithm.fill.scanline;

import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithm.fill.pattern.IPattern;
import cz.algone.algorithm.rasterizer.Rasterizer;
import cz.algone.algorithm.rasterizer.line.LineRasterizerBresenham;
import cz.algone.algorithm.rasterizer.polygon.PolygonRasterizer;
import cz.algone.model.Line;
import cz.algone.model.Model;
import cz.algone.model.Point;
import cz.algone.model.Polygon;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanlineFill implements IFill {
    private RasterCanvas raster;
    private PolygonRasterizer polygonRasterizer;
    private IPattern pattern = null;

    public ScanlineFill(Rasterizer<Polygon> polygonRasterizer) {
        this.polygonRasterizer = (PolygonRasterizer) polygonRasterizer;
    }

    @Override
    public void setup(RasterCanvas raster) {
        this.raster = raster;
    }

    @Override
    public void fill(Model model, ColorPair colors, int borderColor) {
        if (raster == null) {
            throw new IllegalStateException("ScanlineFill: raster not set. Call setup() first.");
        }

        if (!(model instanceof Polygon polygon)) {
            return;
        }

        List<Point> points = polygon.getPoints();
        int pixelColor;
        if (points == null || points.size() < 3) {
            return; // nic k vyplnění
        }

        int height = raster.getHeight();
        int width = raster.getWidth();
        int fillColor = ColorUtils.interpolateColor(colors.primary(), null, 0);

        // --- 1) Najít minY a maxY polygonu ---
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : points) {
            int y = p.getY();
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }

        // Ořez na rozměr rastru
        minY = Math.max(minY, 0);
        maxY = Math.min(maxY, height - 1);

        // --- 2) Pro každý řádek y spočítat průsečíky a vyplnit polygon ---
        for (int y = minY; y <= maxY; y++) {
            List<Double> intersections = computeIntersectionsForScanline(points, y);

            if (intersections.isEmpty()) continue;

            // Řazení průsečíků
            Collections.sort(intersections);

            // Kontrola sudého počtu
            if (intersections.size() % 2 != 0) {
                continue;
            }

            // Vyplňuje po dvojicích
            for (int i = 0; i + 1 < intersections.size(); i += 2) {
                int xStart = (int) Math.ceil(intersections.get(i));
                int xEnd   = (int) Math.floor(intersections.get(i + 1));

                if (xEnd < 0 || xStart >= width) continue;

                if (xStart < 0)         xStart = 0;
                if (xEnd >= width)      xEnd = width - 1;

                for (int x = xStart; x <= xEnd; x++) {
                    if (pattern != null)
                        pixelColor = pattern.colorAt(x, y, colors);
                    else
                        pixelColor = fillColor;
                    raster.setPixel(x, y, pixelColor);
                }
            }
        }

        // --- 3) Obtažení polygonu barvou hranice ---
        polygonRasterizer.rasterize(polygon);
    }

    /**
     * Spočítá průsečíky hran polygonu s danou scanline y.
     * Používá standardní konvenci:
     *  - horizontální hrany ignorujeme
     *  - hrana je aktivní pro y v intervalu <yMin, yMax)
     */
    private List<Double> computeIntersectionsForScanline(List<Point> points, int currentLineY) {
        List<Double> intersections = new ArrayList<>();
        int pointsCount = points.size();

        for (int i = 0; i < pointsCount; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % pointsCount); // uzavřený polygon

            int x1 = p1.getX();
            int y1 = p1.getY();
            int x2 = p2.getX();
            int y2 = p2.getY();

            // Ignoruje horizontální hrany
            if (y1 == y2) continue;

            int edgeMinY = Math.min(y1, y2);
            int edgeMaxY = Math.max(y1, y2);

            // Hrana je aktivní pro y v <edgeMinY, edgeMaxY)
            if (currentLineY < edgeMinY || currentLineY >= edgeMaxY) continue;

            // Lineární interpolace X na daném Y
            double t = (double) (currentLineY - y1) / (double) (y2 - y1);
            double x = x1 + t * (x2 - x1);

            intersections.add(x);
        }

        return intersections;
    }

    @Override
    public void setPattern(IPattern pattern) {
        this.pattern = pattern;
    }
}
