package cz.algone.algorithm.rasterizer.polygon;

import cz.algone.model.Line;
import cz.algone.model.Point;
import cz.algone.model.Polygon;
import cz.algone.raster.RasterCanvas;
import cz.algone.algorithm.rasterizer.Rasterizer;
import cz.algone.util.color.ColorPair;

public class PolygonRasterizer implements Rasterizer<Polygon> {
    private Rasterizer<Line> lineRasterizer;

    public PolygonRasterizer(Rasterizer<Line> lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    @Override
    public void rasterize(Polygon polygon) {
        if (polygon.getSize() < 3) return;

        for (int indexA = 0; indexA < polygon.getSize(); indexA++) {
            int indexB = indexA + 1;

            //Pro spojení poslendího a prvního bodu
            if (indexB == polygon.getSize())
                indexB = 0;

            //Vykreslení previewPointu z dragged listeneru
            if (polygon.getPreviewPoint() != null && indexB == 0)
                polygon.getPoints().set(indexA, polygon.getPreviewPoint());

            Point a = polygon.getPointByIndex(indexA);
            Point b = polygon.getPointByIndex(indexB);

            lineRasterizer.rasterize(new Line(a, b, polygon.getColors()));
        }
    }

    @Override
    public void setup(RasterCanvas raster) {
        lineRasterizer.setup(raster);
    }
}
