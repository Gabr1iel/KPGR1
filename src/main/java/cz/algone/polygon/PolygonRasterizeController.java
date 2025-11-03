package cz.algone.polygon;

import cz.algone.controller.RasterizeController;
import cz.algone.model.Point;
import cz.algone.raster.RasterCanvas;
import cz.algone.rasterize.Rasterizer;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;

public class PolygonRasterizeController implements RasterizeController {
    private RasterCanvas raster;
    private Canvas canvas;
    private Polygon polygon;
    private int nearestPointIndex = -1;
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
            if (!e.isSecondaryButtonDown()) {
                System.out.println("Levé");
                polygon.addPoint(new Point((int) e.getX(), (int) e.getY()));
            }else{
                System.out.println("Pravé");
                nearestPointIndex = polygon.getNearestPoint((int) e.getX(), (int) e.getY());}
            drawScene();
        });
        canvas.setOnMouseDragged(e -> {
            if (!e.isSecondaryButtonDown()) {
                System.out.println("levé");
                polygon.setPreviewPoint(new Point((int) e.getX(), (int) e.getY()));
            }else{
                System.out.println("pravé");
                System.out.println(nearestPointIndex);
                polygon.setPointByIndex(nearestPointIndex, (int) e.getX(), (int) e.getY());}
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
        polygonRasterizer.rasterize(polygon);
    }

    @Override
    public void clearRaster() {
        raster.clear();
        polygon = new Polygon();
    }
}
