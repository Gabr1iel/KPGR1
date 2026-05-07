package cz.algone.algorithm.fill.scanline;

import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithm.fill.pattern.IPattern;
import cz.algone.algorithm.rasterizer.IRasterizer;
import cz.algone.algorithm.rasterizer.polygon.PolygonRasterizer;
import cz.algone.model.models2D.Model;
import cz.algone.model.models2D.Point;
import cz.algone.model.models2D.Polygon;
import cz.algone.raster.ImageBuffer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanlineFill implements IFill<Model> {
    private ImageBuffer imageBuffer;
    private final PolygonRasterizer polygonRasterizer;
    private IPattern pattern = null;

    public ScanlineFill(IRasterizer<Polygon> polygonRasterizer) {
        this.polygonRasterizer = (PolygonRasterizer) polygonRasterizer;
    }

    @Override
    public void setup(ImageBuffer raster) {
        this.imageBuffer = raster;
    }

    @Override
    public void fill(Model model, ColorPair colors, int borderColor) {
        if (imageBuffer == null) {
            throw new IllegalStateException("ScanlineFill: raster not set. Call setup() first.");
        }

        if (!(model instanceof Polygon polygon)) {
            return;
        }

        List<Point> points = polygon.getPoints();
        int pixelColor;
        if (points == null || points.size() < 3) {
            return;
        }

        int height = imageBuffer.getHeight();
        int width = imageBuffer.getWidth();
        int fillColor = ColorUtils.interpolateColor(colors.primary(), null, 0);

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : points) {
            int y = p.y();
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }

        minY = Math.max(minY, 0);
        maxY = Math.min(maxY, height - 1);

        for (int y = minY; y <= maxY; y++) {
            List<Double> intersections = computeIntersectionsForScanline(points, y);

            if (intersections.isEmpty()) continue;

            Collections.sort(intersections);

            if (intersections.size() % 2 != 0) {
                continue;
            }

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
                    imageBuffer.setValue(x, y, pixelColor);
                }
            }
        }

        polygonRasterizer.rasterize(polygon);
    }

    /** Vrací seřazený seznam x-průsečíků hran polygonu se scanlinem y. */
    private List<Double> computeIntersectionsForScanline(List<Point> points, int currentLineY) {
        List<Double> intersections = new ArrayList<>();
        int pointsCount = points.size();

        for (int i = 0; i < pointsCount; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % pointsCount);

            int x1 = p1.x();
            int y1 = p1.y();
            int x2 = p2.x();
            int y2 = p2.y();

            if (y1 == y2) continue;

            int edgeMinY = Math.min(y1, y2);
            int edgeMaxY = Math.max(y1, y2);

            if (currentLineY < edgeMinY || currentLineY >= edgeMaxY) continue;

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
