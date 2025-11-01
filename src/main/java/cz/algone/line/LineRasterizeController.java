package cz.algone.line;

import cz.algone.controller.RasterizeController;
import cz.algone.raster.RasterCanvas;
import cz.algone.rasterize.Rasterizer;
import javafx.scene.canvas.Canvas;

public class LineRasterizeController implements RasterizeController<Line> {
    private RasterCanvas raster;
    private Canvas canvas;
    private Rasterizer<Line> rasterizer;
    private Line line;

    @Override
    public void setup(RasterCanvas raster, Rasterizer<Line> rasterizer) {
        this.rasterizer = rasterizer;
        this.raster = raster;
        this.canvas = raster.getCanvas();
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> {
            line = new Line((int) e.getX(), (int) e.getY(), (int) e.getX(), (int) e.getY());
        });
        canvas.setOnMouseDragged(e -> {
            if (line == null) {return;}
            line.setX2((int) e.getX());
            line.setY2((int) e.getY());
            drawScene();
        });
        canvas.setOnMouseReleased(e -> {
            if (line == null) {return;}
            line.setX2((int) e.getX());
            line.setY2((int) e.getY());
            drawScene();
        });
    }

    @Override
    public void drawScene() {
        raster.clear();
        rasterizer.rasterize(line);
    }

    @Override
    public void clearRaster() {
        raster.clear();
        line = null;
    }
}
