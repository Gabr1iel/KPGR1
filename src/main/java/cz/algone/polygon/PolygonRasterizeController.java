package cz.algone.polygon;

import cz.algone.controller.RasterizeController;
import cz.algone.model.Point;
import cz.algone.raster.RasterCanvas;
import cz.algone.rasterize.Rasterizer;
import javafx.scene.canvas.Canvas;

public class PolygonRasterizeController implements RasterizeController {
    private RasterCanvas raster;
    private Canvas canvas;
    private Polygon polygon;
    private final PolygonRasterizer polygonRasterizer = new PolygonRasterizer();

    @Override
    public void setup(RasterCanvas raster, Rasterizer lineRasterizer) {
        this.raster = raster;
        this.canvas = raster.getCanvas();
        polygon = new Polygon();
        polygonRasterizer.setupPolygonRasterizer(lineRasterizer);
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> {
            polygon.addPoint(new Point((int) e.getX(), (int) e.getY()));
            drawScene();
        });
        canvas.setOnMouseDragged(e -> {
            polygon.setPreviewPoint(new Point((int) e.getX(), (int) e.getY()));
            drawScene();
        });
        canvas.setOnMouseReleased(e -> {
            polygon.addPoint(new Point((int) e.getX(), (int) e.getY()));
            polygon.setPreviewPoint(null);
            drawScene();
        });
    }

    @Override
    public void drawScene() {
        raster.clear();
        polygonRasterizer.rasterize(polygon);
    }

    @Override
    public void clearRaster() {
        raster.clear();
        polygon = new Polygon();
    }
}
