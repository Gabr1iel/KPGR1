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
            int x2 = (int) e.getX();
            int y2 = (int) e.getY();

            if (e.isShiftDown()) {
                int lengthX = Math.abs((int) e.getX() - line.getX1());
                int lengthY = Math.abs((int) e.getY() - line.getY1());
                double ratio = Math.abs(lengthX / lengthY);

                if (ratio > 2) {
                    y2 = line.getY1();
                } else if (ratio < 0.5) {
                    x2 = line.getX1();
                } else {
                    int signX = ((int) e.getX() - line.getX1()) < 0 ? -1 : 1;
                    int signY = ((int) e.getY() - line.getY1()) < 0 ? -1 : 1;
                    int d = Math.min(lengthX, lengthY);
                    x2 = line.getX1() + signX * d;
                    y2 = line.getY1() + signY * d;
                }
            }
            line.setX2(x2);
            line.setY2(y2);
            drawScene();
        });
        canvas.setOnMouseReleased(e -> {
            if (line == null) {return;}
            if (!e.isShiftDown()) {
                line.setX2((int) e.getX());
                line.setY2((int) e.getY());
            }
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
