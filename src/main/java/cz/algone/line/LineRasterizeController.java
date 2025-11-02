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
            if (e.isShiftDown())
                rasterizeLineWithShift((int) e.getX(), (int) e.getY());
            else {
                line.setX2((int) e.getX());
                line.setY2((int) e.getY());
            }
            drawScene();
        });
        canvas.setOnMouseReleased(e -> {
            if (line == null) {return;}
            //Zabrání překreslení čáry vytvořené
            if (!e.isShiftDown()) {
                line.setX2((int) e.getX());
                line.setY2((int) e.getY());
            }
            drawScene();
        });
    }

    public void rasterizeLineWithShift(int x2, int y2) {
        int lengthX = Math.abs(x2 - line.getX1());
        int lengthY = Math.abs(y2 - line.getY1());
        double ratio = Math.abs(lengthX / lengthY);

        //Zarovnání na vertikální čáru, pro úhly větší jak cca 70
        if (ratio > 2) {
            y2 = line.getY1();
        //Zarovnání na horizontální čáru, pro úhly menší jak cca 26
        } else if (ratio < 0.5) {
            x2 = line.getX1();
        //Vykreslení čáry s úhlem 45
        } else {
            int signX = (x2 - line.getX1()) < 0 ? -1 : 1;
            int signY = (y2 - line.getY1()) < 0 ? -1 : 1;
            int d = Math.min(lengthX, lengthY);
            x2 = line.getX1() + signX * d;
            y2 = line.getY1() + signY * d;
        }
        line.setX2(x2);
        line.setY2(y2);
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
