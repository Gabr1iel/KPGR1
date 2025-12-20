package cz.algone.algorithm.fill.scanline;

import cz.algone.algorithm.fill.IFill;
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

    @Override
    public void setup(RasterCanvas raster) {
        this.raster = raster;
    }

    @Override
    public void fill(Model model, ColorPair color, int borderColor) {
        if (raster == null) {
            throw new IllegalStateException("ScanlineFill: raster not set. Call setup() first.");
        }

        if (!(model instanceof Polygon polygon)) {
            return;
        }

        List<Point> points = polygon.getPoints();
        if (points == null || points.size() < 3) {
            return; // nic k vyplnění
        }

        int height = raster.getHeight();
        int width = raster.getWidth();

        int fillColor = ColorUtils.interpolateColor(color.primary(), null, 0);

        // --- 1) Najdi minY a maxY polygonu ---
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

        int n = points.size();

        // --- 2) Pro každý řádek y spočítej průsečíky a vyplň ---
        for (int y = minY; y <= maxY; y++) {
            List<Double> intersections = computeIntersectionsForScanline(points, n, y);

            if (intersections.isEmpty()) continue;

            // Řazení průsečíků (implementace algoritmu pro řazení)
            Collections.sort(intersections);

            // Kontrola sudého počtu (bezpečnostní, pro debugging)
            if (intersections.size() % 2 != 0) {
                // Může se stát u degenerovaných polygonů – tady to jen přeskočíme,
                // případně můžeš logovat.
                continue;
            }

            // Vyplňujeme po dvojicích
            for (int i = 0; i + 1 < intersections.size(); i += 2) {
                int xStart = (int) Math.ceil(intersections.get(i));
                int xEnd   = (int) Math.floor(intersections.get(i + 1));

                if (xEnd < 0 || xStart >= width) continue;

                if (xStart < 0)         xStart = 0;
                if (xEnd >= width)      xEnd = width - 1;

                for (int x = xStart; x <= xEnd; x++) {
                    raster.setPixel(x, y, fillColor);
                }
            }
        }

        // --- 3) Obtažení polygonu barvou hranice ---
        drawBorder(points, borderColor, width, height);
    }

    /**
     * Spočítá průsečíky hran polygonu s danou scanline y.
     * Používá standardní konvenci:
     *  - horizontální hrany ignorujeme
     *  - hrana je aktivní pro y v intervalu <yMin, yMax)
     */
    private List<Double> computeIntersectionsForScanline(List<Point> points, int n, int y) {
        List<Double> intersections = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % n); // uzavřený polygon

            int x1 = p1.getX();
            int y1 = p1.getY();
            int x2 = p2.getX();
            int y2 = p2.getY();

            // Ignoruj horizontální hrany – nemají jedinečný průsečík se scanline
            if (y1 == y2) continue;

            int edgeMinY = Math.min(y1, y2);
            int edgeMaxY = Math.max(y1, y2);

            // Hrana je aktivní pro y v <edgeMinY, edgeMaxY)
            if (y < edgeMinY || y >= edgeMaxY) continue;

            // Lineární interpolace X na daném Y
            double t = (double) (y - y1) / (double) (y2 - y1);
            double x = x1 + t * (x2 - x1);

            intersections.add(x);
        }

        return intersections;
    }

    /**
     * Obtažení polygonu – jednoduchý Bresenham pro každou hranu.
     * Tím splníme "obtažení barvou hranice".
     */
    private void drawBorder(List<Point> points, int borderColor, int width, int height) {
        int n = points.size();
        if (n < 2) return;

        for (int i = 0; i < n; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % n);
            drawLineBresenham(p1.getX(), p1.getY(), p2.getX(), p2.getY(), borderColor, width, height);
        }
    }

    private void drawLineBresenham(int x1, int y1, int x2, int y2, int color, int width, int height) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        int x = x1;
        int y = y1;

        while (true) {
            if (x >= 0 && x < width && y >= 0 && y < height) {
                raster.setPixel(x, y, color);
            }

            if (x == x2 && y == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x += sx; }
            if (e2 <  dx) { err += dx; y += sy; }
        }
    }
}
