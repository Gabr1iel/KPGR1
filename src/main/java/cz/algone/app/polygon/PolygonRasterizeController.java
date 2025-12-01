package cz.algone.app.polygon;

import cz.algone.app.RasterizeController;
import cz.algone.model.Point;
import cz.algone.model.Polygon;
import cz.algone.raster.RasterCanvas;
import cz.algone.rasterizer.Rasterizer;
import cz.algone.rasterizer.polygon.PolygonRasterizer;
import cz.algone.util.color.ColorPair;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

public class PolygonRasterizeController implements RasterizeController {
    private RasterCanvas raster;
    private Canvas canvas;
    private Polygon polygon;
    private ColorPair colors;
    private int nearestPointIndex = -1;
    private final PolygonRasterizer polygonRasterizer = new PolygonRasterizer();

    @Override
    public void setup(RasterCanvas raster, Rasterizer lineRasterizer) {
        this.raster = raster;
        this.canvas = raster.getCanvas();
        polygon = new Polygon();
        polygonRasterizer.setupPolygonRasterizer(lineRasterizer);
        this.colors = new ColorPair(Color.valueOf("#000000"), null);
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) //Získání indexu nebližšího bodu
                nearestPointIndex = polygon.getNearestPoint((int) e.getX(), (int) e.getY());
            drawScene();
        });
        canvas.setOnMouseDragged(e -> {
            if (!e.isSecondaryButtonDown())
                polygon.setPreviewPoint(new Point((int) e.getX(), (int) e.getY()));
            else //Update nejbližšího bodu
                polygon.setPointByIndex(nearestPointIndex, (int) e.getX(), (int) e.getY());
            drawScene();
        });
        canvas.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.PRIMARY)
                polygon.addPoint(new Point((int) e.getX(), (int) e.getY()));
            polygon.setPreviewPoint(null);
            nearestPointIndex = -1;
            drawScene();
        });
    }

    @Override
    public void drawScene() {
        raster.clear();
        polygonRasterizer.rasterize(polygon, colors);
    }

    @Override
    public void clearRaster() {
        raster.clear();
        polygon = new Polygon();
    }

    @Override
    public void setColors(ColorPair colors) {
        this.colors = colors;
    }
}
