package cz.algone.algorithmController.shape.rectangle;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.rasterizer.Rasterizer;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.model.Point;
import cz.algone.model.Polygon;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.color.ColorPair;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;

public class RectangleShapeController implements ShapeController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.POLYGON;
    private Point point1;
    private RasterCanvas raster;
    private Canvas canvas;
    private Polygon polygon;
    private ColorPair colors;
    private Rasterizer polygonRasterizer;

    @Override
    public void setup(RasterCanvas raster, IAlgorithm polygonRasterizer) {
        this.raster = raster;
        this.canvas = raster.getCanvas();
        this.polygonRasterizer = (Rasterizer) polygonRasterizer;
        polygon = new Polygon(colors);
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                point1 = new Point((int) event.getX(), (int) event.getY());
            }
        });
        canvas.setOnMouseDragged(event -> {
            if (point1 == null) return;
            createRectangle(point1, new Point((int) event.getX(), (int) event.getY()));
            drawScene();
        });
        canvas.setOnMouseReleased(event -> {
            if (point1 == null) return;
            createRectangle(point1, new Point((int) event.getX(), (int) event.getY()));
            drawScene();
        });
    }

    private void createRectangle(Point point1, Point point3) {
        polygon = new Polygon(colors);
        polygon.addPoint(point1);
        polygon.addPoint(new Point(point3.getX(), point1.getY()));
        polygon.addPoint(point3);
        polygon.addPoint(new Point(point1.getX(), point3.getY()));
    }

    @Override
    public void drawScene() {
        raster.clear();
        polygonRasterizer.rasterize(polygon);
    }

    @Override
    public void clearRaster() {
        raster.clear();
        polygon = new Polygon(colors);
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    @Override
    public void setColors(ColorPair colors) {
        this.colors = colors;
    }
}
